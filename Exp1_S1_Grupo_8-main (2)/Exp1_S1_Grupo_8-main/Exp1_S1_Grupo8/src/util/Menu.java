package util;


public class Menu {
    /** Se muestra titulo y lista de opciones */
    public static void show(String title, String[] options) {
        System.out.println("\n==============================");
        System.out.println(" " + title);
        System.out.println("==============================");
        for (String op : options) System.out.println(op);
        System.out.println("==============================");
    }
}
