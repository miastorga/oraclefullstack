package com.hospital_vm.cl.hospital_vm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital_vm.cl.hospital_vm.model.Participante;
import com.hospital_vm.cl.hospital_vm.model.ParticipanteDTO;
import com.hospital_vm.cl.hospital_vm.repository.PaticipanteRepository;

@Service
public class ParticipanteService {
  @Autowired
  private PaticipanteRepository paticipanteRepository;

  public List<Participante> findAll() {
    return paticipanteRepository.findAll();
  }

  public Participante findById(long id) {
    return paticipanteRepository.findById(id).get();
  }

  public Participante save(Participante participante) {
    return paticipanteRepository.save(participante);
  }

  public Participante crearDesdeDTO(ParticipanteDTO participanteDTO) {
    Participante participante = new Participante();
    participante.setNombre(participanteDTO.getNombre());
    participante.setEspecie(participanteDTO.getEspecie());
    participante.setRaza(participanteDTO.getRaza());
    return paticipanteRepository.save(participante);
  }

  public void delete(Long id) {
    paticipanteRepository.deleteById(id);
  }
}
