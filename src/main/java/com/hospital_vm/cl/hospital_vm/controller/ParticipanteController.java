package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital_vm.cl.hospital_vm.model.Participante;
import com.hospital_vm.cl.hospital_vm.model.ParticipanteDTO;
import com.hospital_vm.cl.hospital_vm.service.ParticipanteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/participantes")
public class ParticipanteController {

  @Autowired
  private ParticipanteService participanteService;

  @GetMapping
  public ResponseEntity<List<Participante>> listar() {
    List<Participante> participante = participanteService.findAll();
    if (participante.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(participante);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Optional<Participante>> buscar(@PathVariable long id) {
    try {
      Optional<Participante> cliente = Optional.ofNullable(participanteService.findById(id));
      return ResponseEntity.ok(cliente);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  public ResponseEntity<Participante> guardar(@Valid @RequestBody ParticipanteDTO participanteDTO) {
    Participante nuevoParticipante = participanteService.crearDesdeDTO(participanteDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(nuevoParticipante);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ParticipanteDTO participanteDTO,
      BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(result.getAllErrors());
    }

    Participante participante = participanteService.findById(id);
    if (participante == null) {
      return ResponseEntity.notFound().build();
    }

    participante.setNombre(participanteDTO.getNombre());
    participante.setEspecie(participanteDTO.getEspecie());
    participante.setRaza(participanteDTO.getRaza());

    Participante clienteGuardado = participanteService.save(participante);

    return ResponseEntity.ok(clienteGuardado);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      participanteService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
