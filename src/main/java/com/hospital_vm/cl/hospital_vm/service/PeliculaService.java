package com.hospital_vm.cl.hospital_vm.service;

import com.hospital_vm.cl.hospital_vm.model.Pelicula;
import com.hospital_vm.cl.hospital_vm.repository.PeliculaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PeliculaService {

    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> findAll() {
        return peliculaRepository.findAll();
    }

    public Pelicula findById(long id) {
        return peliculaRepository.findById(id).get();
    }

    public Pelicula save(Pelicula paciente) {
        return peliculaRepository.save(paciente);
    }

    public void delete(Long id) {
        peliculaRepository.deleteById(id);
    }
}
