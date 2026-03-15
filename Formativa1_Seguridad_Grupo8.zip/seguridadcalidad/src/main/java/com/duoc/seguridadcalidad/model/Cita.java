package com.duoc.seguridadcalidad.model;

public class Cita {
    private String fecha;
    private String hora;
    private String motivo;
    private String veterinarioAsignado;

    public Cita(String fecha, String hora, String motivo, String veterinarioAsignado) {
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.veterinarioAsignado = veterinarioAsignado;
    }

    // Getters y Setters
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getVeterinarioAsignado() { return veterinarioAsignado; }
    public void setVeterinarioAsignado(String veterinarioAsignado) { this.veterinarioAsignado = veterinarioAsignado; }
}