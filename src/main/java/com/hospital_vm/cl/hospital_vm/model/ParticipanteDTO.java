package com.hospital_vm.cl.hospital_vm.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ParticipanteDTO {
  @NotNull
  private String nombre;
  @NotNull
  private String especie; // Perro, Gato, etc.
  @NotNull
  private String raza;
}
