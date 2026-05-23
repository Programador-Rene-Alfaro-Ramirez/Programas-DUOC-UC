package com.duoc.LearningPlatformValidation.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "curso_id")
    private Long cursoId;
    
    @Column(nullable = false, length = 150)
    private String nombre;
    
    @Column(name = "puntaje_maximo")
    private int puntajeMaximo;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_aplicacion")
    private Date fechaAplicacion;
    
    @ManyToOne
    @JoinColumn(name = "curso_id", insertable = false, updatable = false)
    private Curso curso;
    
    public Evaluacion() {}
    
    public Evaluacion(Long cursoId, String nombre, int puntajeMaximo, Date fechaAplicacion) {
        this.cursoId = cursoId;
        this.nombre = nombre;
        this.puntajeMaximo = puntajeMaximo;
        this.fechaAplicacion = fechaAplicacion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCursoId() {
        return cursoId;
    }
    
    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getPuntajeMaximo() {
        return puntajeMaximo;
    }
    
    public void setPuntajeMaximo(int puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }
    
    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }
    
    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
    
    public Curso getCurso() {
        return curso;
    }
    
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
