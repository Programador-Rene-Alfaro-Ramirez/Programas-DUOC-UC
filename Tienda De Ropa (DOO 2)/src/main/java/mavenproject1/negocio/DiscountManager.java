package mavenproject1.negocio;

import mavenproject1.modelo.Component;
import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private static DiscountManager instancia;
    private final List<Component> carrito;

    private DiscountManager() {
        carrito = new ArrayList<>();
    }

    public static DiscountManager getInstancia() {
        if (instancia == null) {
            instancia = new DiscountManager();
        }
        return instancia;
    }

    public void agregarAlCarrito(Component producto) {
        carrito.add(producto);
    }

    public void eliminarDelCarrito(Component producto) {
        carrito.remove(producto);
    }

    public void mostrarCarrito() {
        System.out.println("Carrito de Compras:");
        for (Component p : carrito) {
            System.out.println("- " + p.getDescripcion() + " → $" + p.getPrecio());
        }
    }
}
