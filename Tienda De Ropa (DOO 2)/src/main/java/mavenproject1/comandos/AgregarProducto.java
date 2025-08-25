package mavenproject1.comandos;

import mavenproject1.modelo.Component;
import mavenproject1.negocio.DiscountManager;

public class AgregarProducto implements Command {
    private final DiscountManager manager;
    private final Component producto;

    public AgregarProducto(DiscountManager manager, Component producto) {
        this.manager = manager;
        this.producto = producto;
    }

    @Override
    public void ejecutar() {
        manager.agregarAlCarrito(producto);
    }
}
