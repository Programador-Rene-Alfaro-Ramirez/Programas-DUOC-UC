package computec.model;

public class Equipo {
    private int idEquipo;
    private String marca;
    private String modelo;
    private double precio;
    private String tipo; // Desktop o Laptop

    public Equipo() {}

    public Equipo(int idEquipo, String marca, String modelo, double precio, String tipo) {
        this.idEquipo = idEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.tipo = tipo;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return marca + " " + modelo;
    }
}