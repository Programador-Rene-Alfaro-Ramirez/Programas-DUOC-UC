package computec.model;

import java.util.Date;

public class Venta {
    private int idVenta;
    private int idCliente;
    private int idEquipo;
    private double precioFinal;
    private Date fechaVenta;

    // Constructor vacío
    public Venta() {}

    // Constructor con todos los parámetros
    public Venta(int idVenta, int idCliente, int idEquipo, double precioFinal, Date fechaVenta) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.idEquipo = idEquipo;
        this.precioFinal = precioFinal;
        this.fechaVenta = fechaVenta;
    }

    // Constructor sin idVenta (para insertar)
    public Venta(int idCliente, int idEquipo, double precioFinal, Date fechaVenta) {
        this.idCliente = idCliente;
        this.idEquipo = idEquipo;
        this.precioFinal = precioFinal;
        this.fechaVenta = fechaVenta;
    }

    // Getters y setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }

    public double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }

    public Date getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Date fechaVenta) { this.fechaVenta = fechaVenta; }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", idCliente=" + idCliente +
                ", idEquipo=" + idEquipo +
                ", precioFinal=" + precioFinal +
                ", fechaVenta=" + fechaVenta +
                '}';
    }
}