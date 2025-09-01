package mavenproject1.command;

import mavenproject1.controller.ProductoController;
import mavenproject1.model.Producto;

public class EliminarProductoCommand implements Command {
    private final ProductoController controller;
    private final Producto producto;

    public EliminarProductoCommand(ProductoController controller, Producto producto) {
        this.controller = controller;
        this.producto = producto;
    }

    @Override
    public void ejecutar() {
        controller.eliminarProducto(producto);
        System.out.println("Producto eliminado del catalogo: " + producto.getNombre());
    }
}
