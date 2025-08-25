package mavenproject1.descuentos;

import mavenproject1.modelo.Component;
import mavenproject1.modelo.Decorator;

public class DescuentoCategoria extends Decorator {
    public DescuentoCategoria(Component componente) {
        super(componente);
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() * 0.8;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Descuento por categoria";
    }
}
