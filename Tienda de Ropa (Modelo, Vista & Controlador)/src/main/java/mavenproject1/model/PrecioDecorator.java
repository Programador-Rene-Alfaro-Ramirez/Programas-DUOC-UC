package mavenproject1.model;

public class PrecioDecorator implements Component {
    private final Producto producto;

    public PrecioDecorator(Producto producto) {
        this.producto = producto;
    }

    public Producto getProducto() {
        return producto;
    }

    @Override
    public double calcularPrecio() {
        return producto.getPrecio();
    }
}

