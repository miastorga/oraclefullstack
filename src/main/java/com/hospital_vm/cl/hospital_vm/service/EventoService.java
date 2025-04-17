package com.hospital_vm.cl.hospital_vm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital_vm.cl.hospital_vm.model.Evento;
import com.hospital_vm.cl.hospital_vm.model.EventoDTO;
import com.hospital_vm.cl.hospital_vm.repository.EventoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventoService {

  @Autowired
  private EventoRepository eventoRepository;

  public List<Evento> findAll() {
    return eventoRepository.findAll();
  }

  public Evento crearDesdeDTO(EventoDTO eventoDTO) {
    Evento evento = new Evento();
    evento.setNombre(eventoDTO.getNombre());
    evento.setFecha(eventoDTO.getFecha());
    evento.setUbicacion(eventoDTO.getUbicacion());
    evento.setDescripcion(eventoDTO.getDescripcion());
    return eventoRepository.save(evento);
  }

  public Evento findById(long id) {
    return eventoRepository.findById(id).get();
  }

  public Evento save(Evento cliente) {
    return eventoRepository.save(cliente);
  }

  public void delete(Long id) {
    eventoRepository.deleteById(id);
  }
}
