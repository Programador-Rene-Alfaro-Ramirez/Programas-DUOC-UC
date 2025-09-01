package mavenproject1.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final Usuario usuario;
    private final List<Producto> productos;

    public Pedido(Usuario usuario) {
        this.usuario = usuario;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    //Método que soluciona el error
    public void vaciar() {
        productos.clear();
    }
}

