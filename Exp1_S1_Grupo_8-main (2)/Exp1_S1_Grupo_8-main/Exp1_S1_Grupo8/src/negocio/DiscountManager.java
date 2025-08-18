package negocio;

import java.util.Map;

/**
 * Yo creo esta clase con el NOMBRE que la pauta menciona (DiscountManager)
 * para no tener problemas de forma. También la hago Singleton.
 * Para no duplicar lógica, yo DELEGO todo en mi Singleton real.
 */
public final class DiscountManager {
    // Se define instancia unica
    private static final DiscountManager instance = new DiscountManager();


    private DiscountManager() {}

    // Acceso a la instancia
    public static DiscountManager getInstance() {
        return instance;
    }

    // ---- Yo delego en Singleton para reutilizar toda la lógica de descuentos ----
    public double aplicarDescuento(double precio, int porcentaje) {
        return Singleton.getInstance().aplicarDescuento(precio, porcentaje);
    }

    public double aplicarDescuentoCupon(double precio, String cupon) {
        return Singleton.getInstance().aplicarDescuentoCupon(precio, cupon);
    }

    public Map<String, Integer> getCupones() {
        return Singleton.getInstance().getCupones();
    }
}
