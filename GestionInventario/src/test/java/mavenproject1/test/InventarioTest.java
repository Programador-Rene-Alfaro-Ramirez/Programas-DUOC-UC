package mavenproject1.test;

import mavenproject1.modelo.Inventario;
import mavenproject1.modelo.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventarioTest {
    private Inventario inventario;

    @BeforeEach
    public void setUp() {
        inventario = new Inventario();
    }

    @Test
    public void testAgregarYBuscar() {
        Producto p = new Producto("P001", "Polera", "Ropa", 10000, 5);
        inventario.agregarProducto(p);
        Producto resultado = inventario.buscarProducto("Polera");
        assertNotNull(resultado);
        assertEquals("Polera", resultado.getNombre());
    }

    @Test
    public void testModificarStock() {
        Producto p = new Producto("P002", "Zapatos", "Calzado", 20000, 10);
        inventario.agregarProducto(p);
        inventario.modificarCantidad("Zapatos", 15);
        assertEquals(15, inventario.buscarProducto("Zapatos").getStock());
    }
}
