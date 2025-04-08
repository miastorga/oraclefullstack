package com.hospital_vm.cl.hospital_vm.controller;

import com.hospital_vm.cl.hospital_vm.model.Pelicula;
import com.hospital_vm.cl.hospital_vm.service.PeliculaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/peliculas")
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping
    public ResponseEntity<List<Pelicula>> listar() {

      List<Pelicula> peliculas = peliculaService.findAll();
      if (peliculas.isEmpty()) {
          return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(peliculas);
    }

    @PostMapping
    public ResponseEntity<Pelicula> guardar(@RequestBody Pelicula paciente) {
        Pelicula productoMuevo = peliculaService.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoMuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pelicula>> buscar(@PathVariable long id) {
        try {
            Optional<Pelicula> paciente = Optional.ofNullable(peliculaService.findById(id));
            return ResponseEntity.ok(paciente);
        } catch ( Exception e ) {
            return  ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelicula> actualizar(@PathVariable Integer id, @RequestBody Pelicula peliculaActualizada) {
        try {
            Pelicula peliculaExistente = peliculaService.findById(id);
             peliculaExistente.setTitulo(peliculaActualizada.getTitulo());
              peliculaExistente.setDirector(peliculaActualizada.getDirector());
              peliculaExistente.setGenero(peliculaActualizada.getGenero());
              peliculaExistente.setFechaEstreno(peliculaActualizada.getFechaEstreno());
              peliculaExistente.setSinopsis(peliculaActualizada.getSinopsis());
              peliculaExistente.setDuracion(peliculaActualizada.getDuracion());
              peliculaExistente.setCalificacion(peliculaActualizada.getCalificacion());

             Pelicula peliculaGuardada = peliculaService.save(peliculaExistente);
        return ResponseEntity.ok(peliculaGuardada);
        } catch ( Exception e ) {
            return  ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            peliculaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch ( Exception e ) {
            return  ResponseEntity.notFound().build();
        }
    }
}
