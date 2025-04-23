package com.hospital_vm.cl.hospital_vm;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hospital_vm.cl.hospital_vm.model.Pelicula;
import com.hospital_vm.cl.hospital_vm.repository.PeliculaRepository;
import com.hospital_vm.cl.hospital_vm.service.PeliculaService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias para PeliculaService")
public class PeliculaServiceTest {

  @Mock
  private PeliculaRepository peliculaRepository;

  @InjectMocks
  private PeliculaService peliculaService;

  @Test
  @DisplayName("Debería retornar todas las películas")
  void testFindAll() {

    Pelicula pelicula1 = new Pelicula();
    pelicula1.setId(1);
    pelicula1.setTitulo("Película A");
    pelicula1.setDirector("Director X");
    pelicula1.setGenero("Ciencia Ficción");
    pelicula1.setFechaEstreno(new Date());
    pelicula1.setSinopsis("Sinopsis de Película A");
    pelicula1.setDuracion(120.5);
    pelicula1.setCalificacion(8.5);

    Pelicula pelicula2 = new Pelicula();
    pelicula2.setId(2);
    pelicula2.setTitulo("Película B");
    pelicula2.setDirector("Director Y");
    pelicula2.setGenero("Drama");
    pelicula2.setFechaEstreno(new Date());
    pelicula2.setSinopsis("Sinopsis de Película B");
    pelicula2.setDuracion(95.0);
    pelicula2.setCalificacion(7.0);

    List<Pelicula> listaPeliculasMock = Arrays.asList(pelicula1, pelicula2);

    when(peliculaRepository.findAll()).thenReturn(listaPeliculasMock);

    // Act
    List<Pelicula> resultado = peliculaService.findAll();

    // Assert
    assertThat(resultado)
        .isNotNull()
        .hasSize(2)
        .isEqualTo(listaPeliculasMock); // Compara que las listas sean iguales (campos y valores)

    verify(peliculaRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Debería retornar una película por ID")
  void testFindById() {
    // Arrange
    Integer testIdInteger = 1;
    Long testIdLong = Long.valueOf(testIdInteger); // ID en formato Long/long esperado por el servicio

    Pelicula peliculaMock = new Pelicula();
    peliculaMock.setId(testIdInteger);
    peliculaMock.setTitulo("Película de Prueba");
    peliculaMock.setDirector("Director Famoso");
    peliculaMock.setGenero("Drama");
    peliculaMock.setFechaEstreno(new Date(1609459200000L)); // Ejemplo: 1 Ene 2021
    peliculaMock.setSinopsis("Sinopsis completa de la película de prueba.");
    peliculaMock.setDuracion(135.0);
    peliculaMock.setCalificacion(9.2);

    // Configurar el mock para que retorne la película cuando se busque por el ID
    // (Long)
    when(peliculaRepository.findById(testIdLong)).thenReturn(Optional.of(peliculaMock));

    // Act
    Pelicula resultado = peliculaService.findById(testIdLong); // Llamar al servicio con el ID Long

    // Assert
    assertThat(resultado).isNotNull();
    assertThat(resultado.getId()).isEqualTo(testIdInteger);
    assertThat(resultado.getTitulo()).isEqualTo("Película de Prueba");
    assertThat(resultado.getDirector()).isEqualTo("Director Famoso");
    assertThat(resultado.getGenero()).isEqualTo("Drama");
    assertThat(resultado.getFechaEstreno()).isNotNull(); // Verificar que no es null
    assertThat(resultado.getSinopsis()).isEqualTo("Sinopsis completa de la película de prueba.");
    assertThat(resultado.getDuracion()).isEqualTo(135.0);
    assertThat(resultado.getCalificacion()).isEqualTo(9.2);

    // Verificar que findById fue llamado en el repositorio con el ID Long correcto
    // una vez
    verify(peliculaRepository, times(1)).findById(eq(testIdLong));
  }

  @Test
  @DisplayName("Debería guardar una película")
  void testSave() {
    // Arrange
    Pelicula nuevaPelicula = new Pelicula();
    nuevaPelicula.setTitulo("Nueva Película");
    nuevaPelicula.setDirector("Otro Director");
    nuevaPelicula.setGenero("Comedia");
    nuevaPelicula.setFechaEstreno(new Date());
    nuevaPelicula.setSinopsis("Sinopsis de la nueva película.");
    nuevaPelicula.setDuracion(100.0);
    nuevaPelicula.setCalificacion(7.5);

    // Simulamos la película *después* de ser guardada (con ID asignado y los mismos
    // datos)
    Pelicula peliculaGuardada = new Pelicula();
    peliculaGuardada.setId(10); // Simulamos que se le asigna el ID 10
    peliculaGuardada.setTitulo("Nueva Película");
    peliculaGuardada.setDirector("Otro Director");
    peliculaGuardada.setGenero("Comedia");
    peliculaGuardada.setFechaEstreno(nuevaPelicula.getFechaEstreno()); // Mantenemos la misma fecha
    peliculaGuardada.setSinopsis("Sinopsis de la nueva película.");
    peliculaGuardada.setDuracion(100.0);
    peliculaGuardada.setCalificacion(7.5);

    when(peliculaRepository.save(any(Pelicula.class))).thenReturn(peliculaGuardada);

    // Act
    Pelicula resultado = peliculaService.save(nuevaPelicula);

    // Assert
    assertThat(resultado).isNotNull();
    assertThat(resultado.getId()).isEqualTo(10); // Verificar el ID asignado simulado
    assertThat(resultado.getTitulo()).isEqualTo("Nueva Película");
    assertThat(resultado.getDirector()).isEqualTo("Otro Director");
    assertThat(resultado.getGenero()).isEqualTo("Comedia");
    assertThat(resultado.getFechaEstreno()).isEqualTo(nuevaPelicula.getFechaEstreno()); // Comparar la fecha
    assertThat(resultado.getSinopsis()).isEqualTo("Sinopsis de la nueva película.");
    assertThat(resultado.getDuracion()).isEqualTo(100.0);
    assertThat(resultado.getCalificacion()).isEqualTo(7.5);

    verify(peliculaRepository, times(1)).save(eq(nuevaPelicula));
  }

  @Test
  @DisplayName("Debería eliminar una película por ID")
  void testDelete() {
    // Arrange
    Integer idToDeleteInteger = 1; // ID en formato Integer del modelo
    Long idToDeleteLong = Long.valueOf(idToDeleteInteger); // ID en formato Long esperado por el servicio

    // No se configura 'when' para métodos void a menos que quieras simular una
    // excepción.

    // Act
    peliculaService.delete(idToDeleteLong); // Llamar al servicio con el ID Long

    // Assert
    // Verificar que el método deleteById fue llamado en el repositorio con el ID
    // Long correcto una vez
    verify(peliculaRepository, times(1)).deleteById(eq(idToDeleteLong));
  }

  // Opcional: Añadir una prueba para el caso donde findById no encuentra la
  // película
  @Test
  @DisplayName("Debería lanzar NoSuchElementException si no encuentra la película por ID")
  void testFindByIdNotFound() {
    // Arrange
    Integer testIdInteger = 99; // ID que no existe en Integer
    Long testIdLong = Long.valueOf(testIdInteger); // ID que no existe en Long

    when(peliculaRepository.findById(testIdLong)).thenReturn(Optional.empty()); // Mock retorna Optional vacío para el
    // ID Long

    // Act & Assert
    // Verificar que al llamar findById con un ID (Long) que no existe, se lanza
    // NoSuchElementException
    assertThrows(java.util.NoSuchElementException.class, () -> {
      peliculaService.findById(testIdLong);
    });

    // Verificar que findById fue llamado en el repositorio con el ID Long correcto
    // una vez
    verify(peliculaRepository, times(1)).findById(eq(testIdLong));
  }
}