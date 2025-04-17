package com.hospital_vm.cl.hospital_vm.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionDTO {
  @NotNull
  private Long eventoId;
  @NotNull
  private Long participanteId;
  @NotNull
  private LocalDate fechaInscripcion;
}
