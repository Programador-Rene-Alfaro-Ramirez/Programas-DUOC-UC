package mavenproject1.command;

import mavenproject1.controller.ProductoController;
import mavenproject1.model.Producto;

public class AgregarProductoCommand implements Command {
    private final ProductoController controller;
    private final Producto producto;

    public AgregarProductoCommand(ProductoController controller, Producto producto) {
        this.controller = controller;
        this.producto = producto;
    }

    @Override
    public void ejecutar() {
        controller.agregarProducto(producto);
        System.out.println("Producto agregado al catalogo: " + producto.getNombre());
    }
}
