package negocio;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Yo implemento el patrón Singleton aquí.
 * ¿Qué significa? Que esta clase solo tendrá UNA instancia en toda la app.
 * - Hago el constructor privado (nadie puede hacer "new" desde afuera).
 * - Creo una instancia estática y final (se crea una sola vez).
 * - Expongo getInstance() para que todos usen la misma instancia.
 *
 * Además, yo concentro la lógica de DESCUENTOS:
 * - aplicar % directo
 * - aplicar por CUPÓN
 * - administrar un pequeño mapa de cupones
 */
public class Singleton {

    // declarada la unica instancia. "final" porque no cambiará.
    private static final Singleton instance = new Singleton();

    // se guardan los cupones como codigo de dcto
    private final Map<String, Integer> couponTable = new HashMap<>();

    // Evito que se cree otra instancia
    private Singleton() {
        // Cupones pre cargados para agilizar el sistema
        couponTable.put("WELCOME10", 10);
        couponTable.put("INVIERNO15", 15);
        couponTable.put("LIQ20", 20);
    }

    // acceso a la unica instancia
    public static Singleton getInstance() {
        return instance;
    }

    /** aplico un descuento porcentual directo a un precio. */
    public double aplicarDescuento(double precioOriginal, int porcentaje) {
        validarPrecio(precioOriginal);  // No se aceptan numeros negativos
        validarPorcentaje(porcentaje);  // Limite a 100
        double descuento = precioOriginal * (porcentaje / 100.0);
        return redondear(precioOriginal - descuento); // Se redondea a 2 digitos
    }

    /** YSe aplica descuento y si este no existe el valor queda igual al inicial. */
    public double aplicarDescuentoCupon(double precioOriginal, String cupon) {
        validarPrecio(precioOriginal);
        if (cupon == null || cupon.isBlank()) return precioOriginal;
        Integer pct = couponTable.get(cupon.trim().toUpperCase());
        if (pct == null) return precioOriginal;
        return aplicarDescuento(precioOriginal, pct);
    }

    /** Se permite registrar o modificar cupon para futuras promociones */
    public void registrarOCambiarCupon(String cupon, int porcentaje) {
        if (cupon == null || cupon.isBlank()) throw new IllegalArgumentException("Cupón vacío.");
        validarPorcentaje(porcentaje);
        couponTable.put(cupon.trim().toUpperCase(), porcentaje);
    }

    /** Se crea un numero para mostrar cupones existentes previamente cargados */
    public Map<String, Integer> getCupones() {
        return Collections.unmodifiableMap(couponTable);
    }

    // Acusa precio no valido, cuando se ingresa un numero en negativo
    private void validarPrecio(double precio) {
        if (precio < 0) throw new IllegalArgumentException("Precio negativo no permitido.");
    }

    private void validarPorcentaje(int porcentaje) {
        if (porcentaje < 0 || porcentaje > 100)
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
    }

    private double redondear(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
