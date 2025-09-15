package mavenproject1.modelo;

import java.util.HashMap;
import java.util.Map;

public class GestorDescuentos {
    private static final double TOPE_DESCUENTO_MAXIMO = 0.80;
    private static final double DESCUENTO_ROPA = 0.15;
    private static final double DESCUENTO_ELECTRONICA = 0.10;
    private static final double DESCUENTO_ALIMENTOS = 0.05;
    private static GestorDescuentos instancia;
    private final Map<String, Double> descuentosPorCategoria = new HashMap<>();
    private double topeDescuentoTotal = TOPE_DESCUENTO_MAXIMO;

    private GestorDescuentos() {
        // Parametrización inicial
        descuentosPorCategoria.put("Ropa", DESCUENTO_ROPA);
        descuentosPorCategoria.put("Electronica", DESCUENTO_ELECTRONICA);
        descuentosPorCategoria.put("Alimentos", DESCUENTO_ALIMENTOS);
    }

    public static GestorDescuentos getInstancia() {
        if (instancia == null) instancia = new GestorDescuentos();
        return instancia;
    }

    public void setDescuentoCategoria(String categoria, double porcentaje) {
        descuentosPorCategoria.put(categoria, porcentaje);
    }

    public void setTopeDescuentoTotal(double tope) {
        this.topeDescuentoTotal = Math.min(tope, 1.0); // nunca más del 100%
    }

    public double calcularDescuento(String categoria, String cupon) {
        double descuento = descuentosPorCategoria.getOrDefault(categoria, 0.0);
        return Math.min(descuento, topeDescuentoTotal);
    }

    public String resumenDescuento(String categoria, String cupon) {
        double porcentaje = calcularDescuento(categoria, cupon);
        return String.format("Descuento aplicado: %.0f%%", porcentaje * 100);
    }
}
