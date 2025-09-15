package mavenproject1.modelo;

import java.text.DecimalFormat;

public class Producto {
    private final String codigo;
    private final String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    private static final DecimalFormat formatoPrecio = new DecimalFormat("#,##0.00");

    public Producto(String codigo, String nombre, String descripcion, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setPrecio(double precio) { this.precio = precio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void incrementarStock(int cantidad) { this.stock += cantidad; }
    public void disminuirStock(int cantidad) { this.stock -= cantidad; }

    public String getPrecioFormateado() {
        return formatoPrecio.format(precio);
    }

    public String getPrecioFinalFormateado(String categoria, String cupon) {
        double descuento = GestorDescuentos.getInstancia().calcularDescuento(categoria, cupon);
        double precioFinal = precio * (1 - descuento);
        return formatoPrecio.format(precioFinal) + " (" + GestorDescuentos.getInstancia().resumenDescuento(categoria, cupon) + ")";
    }
}
