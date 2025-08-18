package util;

import java.util.Scanner;

/**
 * Yo uso esta clase para leer datos por consola de forma segura.
 * La idea es no romper el programa si el usuario escribe algo inválido.
 */
public class ConsoleIO {
    // Se utiliza solo un Scaner para todo el codigo
    private static final Scanner SC = new Scanner(System.in);

    /**
     * Se pide un entero en un rango. Si el usuario se equivoca, se vuelvo a preguntar.
     */
    public static int readInt(String label, int min, int max) {
        while (true) { // Se repite hasta una respuesta correcta
            try {
                System.out.print(label + " [" + min + "-" + max + "]: ");
                int v = Integer.parseInt(SC.nextLine().trim());
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango.");
                    continue;
                }
                return v;
            } catch (Exception e) {
                System.out.println("Ingresa un entero válido."); // Aviso de que se ingreso un digito invalido
            }
        }
    }

    /**
     * Yo pido un double. Si el tope es Double.MAX_VALUE significa "sin límite superior",
     * por eso muestro ">= min" para que no se vea un numero gigante en el programa y asi el usuario no se confunda.
     */
    public static double readDouble(String label, double min, double max) {
        boolean sinTope = (max == Double.MAX_VALUE);
        while (true) {
            try {

                if (sinTope) {
                    System.out.print(label + " [>= " + min + "]: ");
                } else {
                    System.out.print(label + " [" + min + " - " + max + "]: ");
                }

                double v = Double.parseDouble(SC.nextLine().trim());


                if (v < min || (!sinTope && v > max)) {
                    System.out.println(sinTope ? "Debe ser >= " + min : "Valor fuera de rango.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido (usa punto decimal).");
            }
        }
    }

    /** Se pide cuando no tiene contenido */
    public static String readNonEmpty(String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = SC.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("No puede estar vacío.");
        }
    }


    public static void info(String msg)  { System.out.println("[INFO] " + msg); }
    public static void ok(String msg)    { System.out.println("[OK] " + msg); }
    public static void warn(String msg)  { System.out.println("[WARN] " + msg); }
    public static void error(String msg) { System.out.println("[ERROR] " + msg); }


    public static void pause() {
        System.out.print("Presiona Enter para continuar...");
        SC.nextLine();
    }
}
