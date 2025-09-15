package mavenproject1.test;

import mavenproject1.modelo.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoTest {

    private Producto producto;

    @BeforeEach
    public void setup() {
        producto = new Producto("F001", "Polera", "Rojas", 10.5, 10);
    }

    @Test
    public void testGetPrecio() {
        assertEquals(10.5, producto.getPrecio());
    }

    @Test
    public void testGetStock() {
        assertEquals(10, producto.getStock());
    }

    @Test
    public void testGetDescripcion() {
        assertEquals("Rojas", producto.getDescripcion()); // ← corregido
    }
}

