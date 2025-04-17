package com.hospital_vm.cl.hospital_vm.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.hospital_vm.cl.hospital_vm.model.Inscripcion;
import com.hospital_vm.cl.hospital_vm.model.InscripcionDTO;
import com.hospital_vm.cl.hospital_vm.model.InscripcionResponseDTO;
import com.hospital_vm.cl.hospital_vm.model.Participante;
import com.hospital_vm.cl.hospital_vm.repository.PaticipanteRepository;
import com.hospital_vm.cl.hospital_vm.service.EventoService;
import com.hospital_vm.cl.hospital_vm.service.InscripcionService;
import com.hospital_vm.cl.hospital_vm.service.ParticipanteService;

@RestController
@RequestMapping("/incripcion")
public class InscripcionController {

  @Autowired
  private InscripcionService inscripcionService;
  @Autowired
  private EventoService eventoService;
  @Autowired
  private ParticipanteService participantService;

  @GetMapping
  public ResponseEntity<List<InscripcionResponseDTO>> listar() {
    List<Inscripcion> inscripcions = inscripcionService.findAll();
    if (inscripcions.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    List<InscripcionResponseDTO> responseDTOs = inscripcions.stream()
        .map(this::convertToResponseDTO)
        .collect(Collectors.toList());
    return ResponseEntity.ok(responseDTOs);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InscripcionResponseDTO> obtenerInscripcion(@PathVariable Long id) {
    Inscripcion inscripcion = inscripcionService.findById(id);
    if (inscripcion != null) {
      InscripcionResponseDTO responseDTO = convertToResponseDTO(inscripcion);
      return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private InscripcionResponseDTO convertToResponseDTO(Inscripcion inscripcion) {
    InscripcionResponseDTO dto = new InscripcionResponseDTO();
    dto.setIdInscripcion(inscripcion.getIdInscripcion());
    if (inscripcion.getEvento() != null) {
      dto.setEventoId(inscripcion.getEvento().getIdEvento());
    } else {
      dto.setEventoId(null); // O algún otro valor por defecto
    }
    if (inscripcion.getParticipante() != null) {
      dto.setParticipanteId(inscripcion.getParticipante().getIdParticipante());
    } else {
      dto.setParticipanteId(null); // O algún otro valor por defecto
    }
    dto.setFechaInscripcion(inscripcion.getFechaInscripcion());
    return dto;
  }

  @PostMapping
  public ResponseEntity<InscripcionResponseDTO> guardar(@RequestBody InscripcionDTO inscripcionDTO) {
    Inscripcion inscripcion = inscripcionService.crearDesdeDTO(inscripcionDTO);
    var inscripcionResponseDTO = new InscripcionResponseDTO();
    inscripcionResponseDTO.setIdInscripcion(inscripcion.getIdInscripcion());
    inscripcionResponseDTO.setEventoId(inscripcionDTO.getEventoId());
    inscripcionResponseDTO.setParticipanteId(inscripcionDTO.getParticipanteId());
    inscripcionResponseDTO.setFechaInscripcion(inscripcionDTO.getFechaInscripcion());
    return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionResponseDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<InscripcionResponseDTO> actualizar(@PathVariable Integer id,
      @RequestBody InscripcionDTO inscripcionDTO) {
    try {
      Inscripcion inscripcionExistente = inscripcionService.findById(id);
      if (inscripcionExistente == null) {
        return ResponseEntity.notFound().build();
      }

      if (inscripcionDTO.getEventoId() != null) {
        Evento evento = eventoService.findById(inscripcionDTO.getEventoId());
        if (evento == null) {
          return ResponseEntity.badRequest().build(); // O manejar el error de evento no encontrado de otra manera
        }
        inscripcionExistente.setEvento(evento);
      }
      if (inscripcionDTO.getParticipanteId() != null) {
        Participante participante = participantService.findById(inscripcionDTO.getParticipanteId());
        if (participante == null) {
          return ResponseEntity.badRequest().build(); // O manejar el error de participante no encontrado de otra manera
        }
        inscripcionExistente.setParticipante(participante);
      }
      if (inscripcionDTO.getFechaInscripcion() != null) {
        inscripcionExistente.setFechaInscripcion(inscripcionDTO.getFechaInscripcion());
      }

      Inscripcion servicioGuardado = inscripcionService.save(inscripcionExistente);
      InscripcionResponseDTO responseDTO = convertToResponseDTO(servicioGuardado); // Convertir a DTO
      return ResponseEntity.ok(responseDTO);

    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {
    try {
      inscripcionService.delete(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
}
