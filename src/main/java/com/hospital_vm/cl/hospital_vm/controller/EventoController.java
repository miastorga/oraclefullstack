package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital_vm.cl.hospital_vm.model.Evento;
import com.hospital_vm.cl.hospital_vm.model.EventoDTO;
import com.hospital_vm.cl.hospital_vm.service.EventoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/eventos")
public class EventoController {

  @Autowired
  private EventoService eventoService;

  @GetMapping
  public ResponseEntity<List<Evento>> listar() {

    List<Evento> eventos = eventoService.findAll();
    if (eventos.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(eventos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Evento>> buscar(@PathVariable long id) {
    try {
      Optional<Evento> eventos = Optional.ofNullable(eventoService.findById(id));
      return ResponseEntity.ok(eventos);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<Evento> guardar(@Valid @RequestBody EventoDTO eventoDTO) {
    Evento clienteNuevo = eventoService.crearDesdeDTO(eventoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(clienteNuevo);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Evento> actualizarEvento(@PathVariable Long id, @RequestBody EventoDTO eventoActualizado) {
    try {
      Evento eventoExistente = eventoService.findById(id);
      if (eventoExistente == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      eventoExistente.setNombre(eventoActualizado.getNombre());
      eventoExistente.setFecha(eventoActualizado.getFecha());
      eventoExistente.setUbicacion(eventoActualizado.getUbicacion());
      eventoExistente.setDescripcion(eventoActualizado.getDescripcion());

      Evento eventoActualizadoEnBD = eventoService.save(eventoExistente);
      return new ResponseEntity<>(eventoActualizadoEnBD, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      eventoService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

}
