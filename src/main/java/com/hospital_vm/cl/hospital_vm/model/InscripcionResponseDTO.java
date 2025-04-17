package com.hospital_vm.cl.hospital_vm.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InscripcionResponseDTO {
  private Long idInscripcion;
  private Long eventoId;
  private Long participanteId;
  private LocalDate fechaInscripcion;
}