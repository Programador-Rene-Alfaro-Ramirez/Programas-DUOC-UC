package computec.patterns;

public class DescuentoDesktop15 implements Descuento {
    private final Descuento descuentoBase;

    public DescuentoDesktop15(Descuento descuentoBase) {
        this.descuentoBase = descuentoBase;
    }

    @Override
    public double aplicarDescuento(double precioBase, String tipoEquipo) {
        double precio = descuentoBase.aplicarDescuento(precioBase, tipoEquipo);
        if (tipoEquipo.equalsIgnoreCase("Desktop")) {
            precio *= 0.85; // 15% de descuento
        }
        return precio;
    }
}