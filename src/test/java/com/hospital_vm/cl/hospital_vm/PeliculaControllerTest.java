package com.hospital_vm.cl.hospital_vm;

import com.hospital_vm.cl.hospital_vm.controller.PeliculaController;
import com.hospital_vm.cl.hospital_vm.model.Pelicula;
import com.hospital_vm.cl.hospital_vm.service.PeliculaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PeliculaController.class)
@DisplayName("Pruebas Unitarias para PeliculaController")
public class PeliculaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PeliculaService peliculaService;

  @Autowired
  private ObjectMapper objectMapper;

  private Pelicula createSamplePelicula(Integer id, String titulo) {
    Pelicula pelicula = new Pelicula();
    pelicula.setId(id);
    pelicula.setTitulo(titulo);
    pelicula.setDirector("Director " + id);
    pelicula.setGenero("Genero " + id);
    pelicula.setFechaEstreno(new Date());
    pelicula.setSinopsis("Sinopsis " + id);
    pelicula.setDuracion(100.0 + id);
    pelicula.setCalificacion(7.0 + id / 2.0);
    return pelicula;
  }

  @Test
  @DisplayName("GET /peliculas - Debería retornar lista de películas y estado 200 OK")
  void listar_RetornaListaCuandoExistenPeliculas() throws Exception {
    List<Pelicula> peliculasMock = Arrays.asList(
        createSamplePelicula(1, "Pelicula 1"),
        createSamplePelicula(2, "Pelicula 2"));

    when(peliculaService.findAll()).thenReturn(peliculasMock);

    mockMvc.perform(get("/peliculas")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.size()").value(peliculasMock.size())).andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].titulo").value("Pelicula 1"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].titulo").value("Pelicula 2"));

    verify(peliculaService, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /peliculas - Debería retornar estado 204 No Content cuando no hay películas")
  void listar_RetornaNoContentCuandoNoHayPeliculas() throws Exception {
    List<Pelicula> peliculasMockVacia = Arrays.asList();

    when(peliculaService.findAll()).thenReturn(peliculasMockVacia);

    mockMvc.perform(get("/peliculas")).andExpect(status().isNoContent());
    verify(peliculaService, times(1)).findAll();
  }

  @Test
  @DisplayName("POST /peliculas - Debería guardar película y retornar estado 201 Created")
  void guardar_DeberiaGuardarPeliculaYRetornarCreated() throws Exception {
    Pelicula nuevaPelicula = createSamplePelicula(null, "Nueva Pelicula");
    Pelicula peliculaGuardada = createSamplePelicula(10, "Nueva Pelicula");
    when(peliculaService.save(any(Pelicula.class))).thenReturn(peliculaGuardada);

    String nuevaPeliculaJson = objectMapper.writeValueAsString(nuevaPelicula);

    mockMvc.perform(post("/peliculas").contentType(MediaType.APPLICATION_JSON).content(nuevaPeliculaJson))
        .andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.titulo").value("Nueva Pelicula"));

    verify(peliculaService, times(1)).save(any(Pelicula.class));
  }

  @Test
  @DisplayName("GET /peliculas/{id} - Debería retornar película y estado 200 OK cuando existe")
  void buscar_RetornaPeliculaYOkCuandoExiste() throws Exception {
    Long testIdLong = 1L;
    Integer testIdInteger = testIdLong.intValue();
    Pelicula peliculaMock = createSamplePelicula(testIdInteger, "Pelicula Existente");

    when(peliculaService.findById(testIdLong)).thenReturn(peliculaMock);

    mockMvc.perform(get("/peliculas/{id}", testIdLong)).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.present").value(true))
        .andExpect(jsonPath("$.value.id").value(testIdInteger))
        .andExpect(jsonPath("$.value.titulo").value("Pelicula Existente"));

    verify(peliculaService, times(1)).findById(eq(testIdLong));
  }

  @Test
  @DisplayName("GET /peliculas/{id} - Debería retornar estado 404 Not Found cuando no existe")
  void buscar_RetornaNotFoundCuandoNoExiste() throws Exception {
    Long testIdLong = 99L;
    when(peliculaService.findById(testIdLong))
        .thenThrow(new NoSuchElementException("No movie found with id " + testIdLong));

    mockMvc.perform(get("/peliculas/{id}", testIdLong))
        .andExpect(status().isNotFound());
    verify(peliculaService, times(1)).findById(eq(testIdLong));
  }

  @Test
  @DisplayName("PUT /peliculas/{id} - Debería actualizar película y retornar estado 200 OK cuando existe")
  void actualizar_DeberiaActualizarPeliculaYRetornarOkCuandoExiste() throws Exception {
    Integer testIdInteger = 1;
    Long testIdLong = Long.valueOf(testIdInteger);
    Pelicula peliculaExistente = createSamplePelicula(testIdInteger, "Pelicula Original");

    Pelicula datosActualizados = createSamplePelicula(null, "Pelicula Actualizada");
    Pelicula peliculaGuardada = createSamplePelicula(testIdInteger, "Pelicula Actualizada");
    peliculaGuardada.setDirector(datosActualizados.getDirector());
    peliculaGuardada.setGenero(datosActualizados.getGenero());
    peliculaGuardada.setFechaEstreno(datosActualizados.getFechaEstreno());
    peliculaGuardada.setSinopsis(datosActualizados.getSinopsis());
    peliculaGuardada.setDuracion(datosActualizados.getDuracion());
    peliculaGuardada.setCalificacion(datosActualizados.getCalificacion());

    when(peliculaService.findById(testIdLong)).thenReturn(peliculaExistente);
    when(peliculaService.save(any(Pelicula.class))).thenReturn(peliculaGuardada);

    String datosActualizadosJson = objectMapper.writeValueAsString(datosActualizados);

    mockMvc.perform(put("/peliculas/{id}", testIdInteger).contentType(MediaType.APPLICATION_JSON)
        .content(datosActualizadosJson))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(testIdInteger))
        .andExpect(jsonPath("$.titulo").value("Pelicula Actualizada"))
        .andExpect(jsonPath("$.director").value(datosActualizados.getDirector()));
    verify(peliculaService, times(1)).findById(eq(testIdLong));
    verify(peliculaService, times(1)).save(any(Pelicula.class));
  }

  @Test
  @DisplayName("PUT /peliculas/{id} - Debería retornar estado 404 Not Found cuando la película no existe")
  void actualizar_RetornaNotFoundCuandoPeliculaNoExiste() throws Exception {
    Integer testIdInteger = 99;
    Long testIdLong = Long.valueOf(testIdInteger);
    when(peliculaService.findById(testIdLong))
        .thenThrow(new NoSuchElementException("No movie found with id " + testIdLong));

    Pelicula datosActualizados = createSamplePelicula(null, "Datos Dummy");
    String datosActualizadosJson = objectMapper.writeValueAsString(datosActualizados);

    mockMvc.perform(put("/peliculas/{id}", testIdInteger)
        .contentType(MediaType.APPLICATION_JSON)
        .content(datosActualizadosJson))
        .andExpect(status().isNotFound());
    verify(peliculaService, times(1)).findById(eq(testIdLong));
    verify(peliculaService, never()).save(any(Pelicula.class));
  }

  @Test
  @DisplayName("DELETE /peliculas/{id} - Debería eliminar película y retornar estado 204 No Content")
  void eliminar_DeberiaEliminarPeliculaYRetornarNoContent() throws Exception {
    Long idToDeleteLong = 1L;

    mockMvc.perform(delete("/peliculas/{id}", idToDeleteLong)).andExpect(status().isNoContent());
    verify(peliculaService, times(1)).delete(eq(idToDeleteLong));
  }

  @Test
  @DisplayName("DELETE /peliculas/{id} - Debería retornar estado 404 Not Found cuando la película a eliminar no existe")
  void eliminar_RetornaNotFoundCuandoPeliculaNoExiste() throws Exception {
    Long idToDeleteLong = 99L;
    doThrow(new org.springframework.dao.EmptyResultDataAccessException(1)).when(peliculaService)
        .delete(eq(idToDeleteLong));

    mockMvc.perform(delete("/peliculas/{id}", idToDeleteLong))
        .andExpect(status().isNotFound());
    verify(peliculaService, times(1)).delete(eq(idToDeleteLong));
  }
}