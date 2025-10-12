package computec.patterns;

public class DescuentoBase implements Descuento {
    @Override
    public double aplicarDescuento(double precioBase, String tipoEquipo) {
        return precioBase; // sin descuento
    }
}
