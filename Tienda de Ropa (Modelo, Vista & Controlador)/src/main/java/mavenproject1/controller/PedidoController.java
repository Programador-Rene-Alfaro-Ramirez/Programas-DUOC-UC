package mavenproject1.controller;

import mavenproject1.model.Pedido;
import mavenproject1.model.Producto;
import mavenproject1.model.Usuario;
import mavenproject1.model.DiscountManager;

import java.util.List;

public class PedidoController {
    private final Pedido pedido;

    public PedidoController(Usuario usuario) {
        pedido = new Pedido(usuario);
    }

    public void agregarProducto(Producto producto) {
        pedido.agregarProducto(producto);
    }

    public void eliminarProducto(Producto producto) {
        pedido.eliminarProducto(producto);
    }

    public List<Producto> getProductos() {
        return pedido.getProductos();
    }

    //Método para finalizar el pedido
    public void finalizarPedido() {
        pedido.vaciar(); // método que limpia la lista de productos
        DiscountManager.getInstance().limpiarDescuentos(); // limpia descuentos registrados
    }
}
