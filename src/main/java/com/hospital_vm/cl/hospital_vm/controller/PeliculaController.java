package com.hospital_vm.cl.hospital_vm.controller;

import com.hospital_vm.cl.hospital_vm.model.Pelicula;
import com.hospital_vm.cl.hospital_vm.service.PeliculaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.List;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

  @Autowired
  private PeliculaService peliculaService;

  @GetMapping
  public ResponseEntity<CollectionModel<EntityModel<Pelicula>>> listar() {

    List<Pelicula> peliculas = peliculaService.findAll();

    if (peliculas.isEmpty()) {
      CollectionModel<EntityModel<Pelicula>> collectionModel = CollectionModel.of(
          java.util.Collections.emptyList(),
          linkTo(methodOn(PeliculaController.class).listar()).withSelfRel());
      return ResponseEntity.noContent().build();
    }

    List<EntityModel<Pelicula>> peliculasConLinks = peliculas.stream()
        .map(pelicula -> EntityModel.of(pelicula,
            linkTo(methodOn(PeliculaController.class).buscar(pelicula.getId().longValue())).withSelfRel(),
            linkTo(methodOn(PeliculaController.class).listar()).withRel("collection")))
        .collect(Collectors.toList());

    CollectionModel<EntityModel<Pelicula>> collectionModel = CollectionModel.of(
        peliculasConLinks,
        linkTo(methodOn(PeliculaController.class).listar()).withSelfRel(),
        linkTo(methodOn(PeliculaController.class).guardar(null)).withRel("create"));

    return ResponseEntity.ok(collectionModel);
  }

  @PostMapping
  public ResponseEntity<EntityModel<Pelicula>> guardar(@RequestBody Pelicula paciente) {
    Pelicula peliculaGuardada = peliculaService.save(paciente);

    EntityModel<Pelicula> peliculaConLinks = EntityModel.of(peliculaGuardada,
        linkTo(methodOn(PeliculaController.class).buscar(peliculaGuardada.getId().longValue())).withSelfRel(),
        linkTo(methodOn(PeliculaController.class).listar()).withRel("collection"));

    return ResponseEntity
        .created(peliculaConLinks.getRequiredLink(org.springframework.hateoas.IanaLinkRelations.SELF).toUri())
        .body(peliculaConLinks);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<Pelicula>> buscar(@PathVariable long id) {
    try {
      Pelicula pelicula = peliculaService.findById(id);

      EntityModel<Pelicula> peliculaConLinks = EntityModel.of(pelicula,
          linkTo(methodOn(PeliculaController.class).buscar(id)).withSelfRel(),
          linkTo(methodOn(PeliculaController.class).listar()).withRel("collection"),
          linkTo(methodOn(PeliculaController.class).actualizar(pelicula.getId(), null)).withRel("update"),

          linkTo(methodOn(PeliculaController.class).eliminar(id)).withRel("delete"));

      return ResponseEntity.ok(peliculaConLinks);
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<EntityModel<Pelicula>> actualizar(@PathVariable Integer id,
      @RequestBody Pelicula peliculaActualizada) {
    try {
      peliculaActualizada.setId(id);

      Pelicula peliculaExistente = peliculaService.findById(Long.valueOf(id));

      peliculaExistente.setTitulo(peliculaActualizada.getTitulo());
      peliculaExistente.setDirector(peliculaActualizada.getDirector());
      peliculaExistente.setGenero(peliculaActualizada.getGenero());
      peliculaExistente.setFechaEstreno(peliculaActualizada.getFechaEstreno());
      peliculaExistente.setSinopsis(peliculaActualizada.getSinopsis());
      peliculaExistente.setDuracion(peliculaActualizada.getDuracion());
      peliculaExistente.setCalificacion(peliculaActualizada.getCalificacion());

      Pelicula peliculaGuardada = peliculaService.save(peliculaExistente);

      EntityModel<Pelicula> peliculaConLinks = EntityModel.of(peliculaGuardada,
          linkTo(methodOn(PeliculaController.class).buscar(peliculaGuardada.getId().longValue())).withSelfRel(),
          linkTo(methodOn(PeliculaController.class).listar()).withRel("collection"));

      return ResponseEntity.ok(peliculaConLinks);

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      peliculaService.delete(id);
      return ResponseEntity.noContent().build();

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    } catch (org.springframework.dao.EmptyResultDataAccessException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}
