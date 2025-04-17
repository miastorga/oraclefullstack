package com.hospital_vm.cl.hospital_vm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "INSCRIPCION")
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idInscripcion;

  @ManyToOne(fetch = FetchType.EAGER) // Cambia a FetchType.EAGER
  @JoinColumn(name = "evento_id")
  @JsonIgnore
  private Evento evento;

  @ManyToOne(fetch = FetchType.EAGER) // Cambia a FetchType.EAGER
  @JoinColumn(name = "participante_id")
  @JsonIgnore
  private Participante participante;

  @NotNull
  private LocalDate fechaInscripcion;
}