package com.duoc.LearningPlatformValidation.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inscripcion")
    private Date fechaInscripcion;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Usuario estudiante;

    public Inscripcion() {}

    public Inscripcion(Curso curso, Usuario estudiante, Date fechaInscripcion) {
        this.curso = curso;
        this.estudiante = estudiante;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(Date fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }
} 