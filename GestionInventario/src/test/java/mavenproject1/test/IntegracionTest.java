package mavenproject1.test;

import mavenproject1.modelo.Inventario;
import mavenproject1.modelo.Producto;
import mavenproject1.modelo.GestorDescuentos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegracionTest {
    private Inventario inventario;

    @BeforeEach
    public void setUp() {
        inventario = new Inventario();
        GestorDescuentos gestor = GestorDescuentos.getInstancia();
        gestor.setDescuentoCategoria("Ropa", 0.15);
        gestor.setTopeDescuentoTotal(0.80);
    }

    @Test
    public void testDescuentoAplicado() {
        Producto p = new Producto("P001", "Polera", "Ropa", 10000, 5);
        inventario.agregarProducto(p);
        double descuento = GestorDescuentos.getInstancia().calcularDescuento("Ropa", null);
        double precioFinal = p.getPrecio() * (1 - descuento);
        assertEquals(8500.0, precioFinal, 0.01);
    }
}
