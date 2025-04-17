package com.hospital_vm.cl.hospital_vm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "EVENTO")
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idEvento;
  @NotNull
  @Size(max = 20)
  @Column(name = "NOMBRE")
  private String nombre;
  @NotNull
  @Column(name = "FECHA")
  private LocalDate fecha;
  @NotNull
  @Column(name = "UBICACION")
  private String ubicacion;
  @NotNull
  @Column(name = "DESCRIPCION")
  @Size(max = 200)
  private String descripcion;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(name = "inscripcion", joinColumns = @JoinColumn(name = "evento_id"), inverseJoinColumns = @JoinColumn(name = "participante_id"))

  private List<Participante> participantes;
}