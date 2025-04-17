package com.hospital_vm.cl.hospital_vm.model;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class EventoDTO {
  @NotNull
  @Size(max = 20)
  private String nombre;
  @NotNull
  private LocalDate fecha;
  @NotNull
  private String ubicacion;
  @NotNull
  @Size(max = 200)
  private String descripcion;
}