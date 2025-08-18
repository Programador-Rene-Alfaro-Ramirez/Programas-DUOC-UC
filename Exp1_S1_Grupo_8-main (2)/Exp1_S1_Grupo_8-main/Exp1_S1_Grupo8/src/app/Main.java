package app;

import negocio.DiscountManager;
import util.ConsoleIO;
import util.Menu;

import java.util.Map;


public class Main {
    public static void main(String[] args) {
        // Titulo y opciones de menu
        final String titulo = "Tienda de Ropa - Descuentos ";
        final String[] opciones = {
                "1) Aplicar DESCUENTO % a un precio",
                "2) Aplicar DESCUENTO por CUPÓN a un precio",
                "3) Listar cupones disponibles",
                "4) Salir"
        };


        DiscountManager dm = DiscountManager.getInstance();

        boolean salir = false;
        while (!salir) {
            Menu.show(titulo, opciones); // Menu
            int opt = ConsoleIO.readInt("Elige opción", 1, 4); // se solicita la opcion

            try {
                switch (opt) {
                    case 1 -> { // en esta opcion se solicita un porcentaje de descuento al manipulador del programa
                        double precio = ConsoleIO.readDouble("Precio", 0, Double.MAX_VALUE);
                        int pct = ConsoleIO.readInt("Porcentaje 0..100", 0, 100);
                        double finalPrice = dm.aplicarDescuento(precio, pct);
                        ConsoleIO.ok("Original: " + precio + " | -" + pct + "% | Final: " + finalPrice);
                    }
                    case 2 -> { // Opcion en donde el usuario elije el % a descontar
                        double precio = ConsoleIO.readDouble("Precio", 0, Double.MAX_VALUE);
                        String cupon = ConsoleIO.readNonEmpty("Cupón");
                        double finalPrice = dm.aplicarDescuentoCupon(precio, cupon);
                        ConsoleIO.ok("Cupón " + cupon.toUpperCase() + " → Final: " + finalPrice);
                    }
                    case 3 -> { // cupones cargados previamente para tener opciones rapidas (igual se podran ver en el menu)
                        Map<String, Integer> map = dm.getCupones();
                        if (map.isEmpty()) {
                            ConsoleIO.warn("No hay cupones disponibles.");
                        } else {
                            ConsoleIO.info("Cupones disponibles:");
                            map.forEach((k, v) -> System.out.println(" - " + k + " => " + v + "%"));
                        }
                    }
                    case 4 -> { // cierre de app
                        salir = true;
                        ConsoleIO.info("¡Gracias! Hasta luego.");
                    }
                }
            } catch (Exception ex) {
                // se centraliza el manejo de errores para que el programa no se caiga
                ConsoleIO.error("Error: " + ex.getMessage());
            }


            if (!salir) ConsoleIO.pause();
        }
    }
}
