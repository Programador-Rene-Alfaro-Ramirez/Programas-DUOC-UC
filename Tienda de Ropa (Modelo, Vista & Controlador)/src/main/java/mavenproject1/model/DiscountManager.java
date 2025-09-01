package mavenproject1.model;

import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private static DiscountManager instancia;
    private final List<String> descuentosAplicados;

    // Constructor privado
    private DiscountManager() {
        descuentosAplicados = new ArrayList<>();
    }

    //Método público para obtener la instancia única
    public static DiscountManager getInstance() {
        if (instancia == null) {
            instancia = new DiscountManager();
        }
        return instancia;
    }

    public void registrarDescuento(String descripcion) {
        descuentosAplicados.add(descripcion);
    }

    public List<String> getDescuentosAplicados() {
        return new ArrayList<>(descuentosAplicados);
    }

    public void limpiarDescuentos() {
        descuentosAplicados.clear();
    }
}
