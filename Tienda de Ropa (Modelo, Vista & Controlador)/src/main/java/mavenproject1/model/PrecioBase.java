package mavenproject1.model;

public class PrecioBase implements Component {
    private final double precio;

    public PrecioBase(double precio) {
        this.precio = precio;
    }

    @Override
    public double calcularPrecio() {
        return precio;
    }
}

