package com.hospital_vm.cl.hospital_vm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital_vm.cl.hospital_vm.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{
  
}
