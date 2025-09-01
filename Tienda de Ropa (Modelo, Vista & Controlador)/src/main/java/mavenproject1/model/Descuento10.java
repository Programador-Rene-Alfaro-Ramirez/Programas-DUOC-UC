package mavenproject1.model;

public class Descuento10 implements Component {
    private final Component componente;

    public Descuento10(Component componente) {
        this.componente = componente;
    }

    @Override
    public double calcularPrecio() {
        return componente.calcularPrecio() * 0.90; // aplica 10% de descuento
    }
}
