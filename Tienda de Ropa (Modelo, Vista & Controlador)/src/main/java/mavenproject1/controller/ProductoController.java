package mavenproject1.controller;

import mavenproject1.model.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoController {
    private final List<Producto> productos;

    public ProductoController() {
        productos = new ArrayList<>();
        productos.add(new Producto("Polera", "Ropa", 10000));
        productos.add(new Producto("Pantalon", "Ropa", 20000));
        productos.add(new Producto("Zapatos", "Calzado", 30000));
    }

    //Método para agregar productos al catálogo
    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    //Método para eliminar productos del catálogo
    public void eliminarProducto(Producto producto) {
        productos.remove(producto);
    }

    //Método para obtener la lista de productos
    public List<Producto> getProductos() {
        return new ArrayList<>(productos); // protección contra modificación externa
    }
}
