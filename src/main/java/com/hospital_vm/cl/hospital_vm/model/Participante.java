package com.hospital_vm.cl.hospital_vm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "PARTICIPANTE")
@NoArgsConstructor
@AllArgsConstructor
public class Participante {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idParticipante;
  @Column(name = "NOMBRE")
  @NotNull
  private String nombre;
  @Column(name = "ESPECIE")
  @NotNull
  private String especie; // Perro, Gato, etc.
  @NotNull
  @Column(name = "RAZA")
  private String raza;
  @ManyToMany(mappedBy = "participantes")
  private List<Evento> eventos;

}