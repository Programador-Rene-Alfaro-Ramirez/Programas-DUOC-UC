package com.duoc.seguridadcalidad.model;

public class Paciente {
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private String dueño;

    // Constructor, Getters y Setters
    public Paciente(String nombre, String especie, String raza, int edad, String dueño) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.dueño = dueño;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getDueño() { return dueño; }
    public void setDueño(String dueño) { this.dueño = dueño; }
}