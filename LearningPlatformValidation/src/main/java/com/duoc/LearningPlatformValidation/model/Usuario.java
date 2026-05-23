package com.duoc.LearningPlatformValidation.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(unique = true, nullable = false, length = 100)
    private String correo;
    
    @Column(nullable = false)
    private String contrasena;
    
    @Column(nullable = false)
    private String rol;
    
    @JsonIgnore
    @OneToMany(mappedBy = "profesor")
    private List<Curso> cursosDictados;

    @JsonIgnore
    @OneToMany(mappedBy = "estudiante")
    private List<Inscripcion> inscripciones;
   
    // Constructor vacío
    public Usuario() {}
    
    // Constructor con parámetros
    public Usuario(String nombre, String correo, String contrasena, String rol) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public List<Curso> getCursosDictados() {
        return cursosDictados;
    }
    
    public void setCursosDictados(List<Curso> cursosDictados) {
        this.cursosDictados = cursosDictados;
    }
    
    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }
    
    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
}