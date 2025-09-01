package mavproyecto.controller;

import mavenproject1.model.Producto;
import mavenproject1.model.Component;
import mavenproject1.model.PrecioBase;
import mavenproject1.model.Descuento10;

public class DescuentoController {
    private static DescuentoController instancia;

    //Singleton: constructor privado
    private DescuentoController() {}

    //Singleton: método getInstance()
    public static DescuentoController getInstance() {
        if (instancia == null) {
            instancia = new DescuentoController();
        }
        return instancia;
    }

    //aplica descuento sobre el precio del producto
    public double calcular(Producto produto) {
        Component base = new PrecioBase(produto.getPrecio());
        Component decorado = new Descuento10(base);
        return decorado.calcularPrecio(); // ✅ sin argumentos
    }
}
