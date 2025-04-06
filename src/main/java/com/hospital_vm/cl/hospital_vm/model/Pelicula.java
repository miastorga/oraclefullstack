package com.hospital_vm.cl.hospital_vm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity  // Marca esta clase como una entidad JPA.
@Table(name = "PELICULA")  // Especifica el nombre de la tabla en la base de datos.
@Data  // Genera automáticamente getters, setters, equals, hashCode y toString.
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor  // Genera un constructor con un argumento por cada campo en la clase.
public class Pelicula {

    @Id  // Especifica el identificador primario.
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // El valor del ID se generará automáticamente.
    private Integer id;

    @Column(nullable = false, length = 255)  // Esta columna no puede ser nula y tiene un tamaño máximo.
    private String titulo;

    @Column(nullable = false)  // Esta columna no puede ser nula.
    private String director;

    @Column(nullable = false)  // Esta columna no puede ser nula.
    private String genero;

    @Column(nullable = true)  // Esta columna puede ser nula.
    private Date fechaEstreno;

    @Column(nullable = true, length = 500)  // Esta columna puede ser nula y tiene un tamaño máximo.
    private String sinopsis;

    @Column(nullable = true)  // Esta columna puede ser nula.
    private Double duracion;  // En minutos

    @Column(nullable = true)  // Esta columna puede ser nula.
    private Double calificacion;  // Calificación de la película (por ejemplo, IMDb)
}
