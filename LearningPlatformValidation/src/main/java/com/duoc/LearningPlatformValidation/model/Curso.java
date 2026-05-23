package com.duoc.LearningPlatformValidation.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "profesor_id") // Eliminamos el 'Long profesorId' para evitar el choque
    private Usuario profesor;

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private List<Inscripcion> inscripciones;

    @JsonIgnore
    @OneToMany(mappedBy = "curso")
    private List<Evaluacion> evaluaciones;

    public Curso() {}

    public Curso(String nombre, String descripcion, Usuario profesor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesor = profesor;
    }

    // Getters y Setters corregidos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Usuario getProfesor() { return profesor; }
    public void setProfesor(Usuario profesor) { this.profesor = profesor; }
} 