package com.hospital_vm.cl.hospital_vm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital_vm.cl.hospital_vm.model.Participante;

public interface PaticipanteRepository extends JpaRepository<Participante, Long>{
  
}
