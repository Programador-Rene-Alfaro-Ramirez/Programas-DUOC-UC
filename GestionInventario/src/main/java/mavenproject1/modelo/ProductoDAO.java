package mavenproject1.modelo;

import java.util.ArrayList;

public class ProductoDAO {
    private final ArrayList<Producto> productos;

    public ProductoDAO() {
        productos = new ArrayList<>();
    }

    public void agregar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        productos.add(producto);
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }
        return null;
    }

    public boolean eliminarPorNombre(String nombre) {
        Producto producto = buscarPorNombre(nombre);
        if (producto != null) {
            productos.remove(producto);
            return true;
        }
        return false;
    }

    public ArrayList<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}
