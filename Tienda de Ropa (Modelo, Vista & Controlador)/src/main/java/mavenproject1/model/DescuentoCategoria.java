package mavenproject1.model;

public class DescuentoCategoria implements Component {
    private final Component componente;
    private final String categoriaObjetivo;

    public DescuentoCategoria(Component componente, String categoriaObjetivo) {
        this.componente = componente;
        this.categoriaObjetivo = categoriaObjetivo;
    }

    @Override
    public double calcularPrecio() {
        if (componente instanceof PrecioDecorator decorator) {
            if (decorator.getProducto().getCategoria().equalsIgnoreCase(categoriaObjetivo)) {
                return decorator.calcularPrecio() * 0.80; // 20% de descuento
            }
        }
        return componente.calcularPrecio(); // sin descuento
    }
}

