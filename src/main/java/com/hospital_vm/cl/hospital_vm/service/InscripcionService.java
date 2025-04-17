package com.hospital_vm.cl.hospital_vm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital_vm.cl.hospital_vm.model.Evento;
import com.hospital_vm.cl.hospital_vm.model.Inscripcion;
import com.hospital_vm.cl.hospital_vm.model.InscripcionDTO;
import com.hospital_vm.cl.hospital_vm.model.Participante;
import com.hospital_vm.cl.hospital_vm.repository.InscripcionRepository;

@Service
public class InscripcionService {
  @Autowired
  private InscripcionRepository inscripcionRepository;

  @Autowired
  private EventoService eventoService;

  @Autowired
  private ParticipanteService participanteService;

  public List<Inscripcion> findAll() {
    return inscripcionRepository.findAll();
  }

  public Inscripcion crearDesdeDTO(InscripcionDTO inscripcionDTO) {
    Evento evento = eventoService.findById(inscripcionDTO.getEventoId());
    Participante participante = participanteService.findById(inscripcionDTO.getParticipanteId());

    Inscripcion inscripcion = new Inscripcion();
    inscripcion.setEvento(evento);
    inscripcion.setParticipante(participante);
    inscripcion.setFechaInscripcion(inscripcionDTO.getFechaInscripcion());
    return inscripcionRepository.save(inscripcion);
  }

  public Inscripcion findById(long id) {
    return inscripcionRepository.findById(id).get();
  }

  public Inscripcion save(Inscripcion cliente) {
    return inscripcionRepository.save(cliente);
  }

  public void delete(Long id) {
    inscripcionRepository.deleteById(id);
  }
}
