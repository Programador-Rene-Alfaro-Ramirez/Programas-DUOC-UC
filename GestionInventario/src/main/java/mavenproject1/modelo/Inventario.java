package mavenproject1.modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto p) {
        productos.add(p);
    }

    public Producto buscarProducto(String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public void modificarCantidad(String nombre, int nuevoStock) {
        Producto p = buscarProducto(nombre);
        if (p != null) p.disminuirStock(p.getStock() - nuevoStock);
    }

    public List<Producto> listarProductos() {
        return productos;
    }
}

