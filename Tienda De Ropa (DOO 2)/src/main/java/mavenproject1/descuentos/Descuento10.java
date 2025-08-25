package mavenproject1.descuentos;

import mavenproject1.modelo.Component;
import mavenproject1.modelo.Decorator;

public class Descuento10 extends Decorator {
    public Descuento10(Component componente) {
        super(componente);
    }

    @Override
    public double getPrecio() {
        return super.getPrecio() * 0.9;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Descuento 10%";
    }
}
