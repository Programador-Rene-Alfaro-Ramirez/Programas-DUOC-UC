package mavenproject1.app;

import mavenproject1.modelo.Producto;
import mavenproject1.modelo.Inventario;
import mavenproject1.modelo.GestorDescuentos;

import java.text.DecimalFormat;
import java.util.Scanner;

public class MenuPrincipal {

    private static final DecimalFormat formato = new DecimalFormat("#,##0.00");
    private static final Scanner scanner = new Scanner(System.in);
    private static final Inventario inventario = new Inventario();
    private static final GestorDescuentos gestor = GestorDescuentos.getInstancia();

    public static void main(String[] args) {
        configurarDescuentos();
        System.out.println("Bienvenido al sistema de gestion de inventario\n");

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1 -> registrarProducto();
                case 2 -> listarProductos();
                case 3 -> buscarProducto();
                case 4 -> eliminarProducto();
                case 5 -> verPrecioConDescuento();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private static void configurarDescuentos() {
        gestor.setDescuentoCategoria("Ropa", 0.15);
        gestor.setDescuentoCategoria("Alimentos", 0.10);
        gestor.setDescuentoCategoria("Electronica", 0.05);
        gestor.setTopeDescuentoTotal(0.80);
    }

    private static void mostrarMenu() {
        System.out.println("\nMenu Principal:");
        System.out.println("1. Registrar producto");
        System.out.println("2. Lista de productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Ver precios con descuento");
        System.out.println("0. Salir");
    }

    private static void registrarProducto() {
        System.out.println("\nRegistrar nuevo producto:");
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        String categoria = leerTexto("Categoria: ");
        double precio = leerDecimal("Precio base: ");
        int stock = leerEntero("Stock inicial: ");

        Producto nuevo = new Producto(codigo, nombre, categoria, precio, stock);
        inventario.agregarProducto(nuevo);
        System.out.println("Producto registrado correctamente.");
    }

    private static void listarProductos() {
        System.out.println("\nLista de productos:");
        if (inventario.listarProductos().isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            for (Producto p : inventario.listarProductos()) {
                mostrarProducto(p);
            }
        }
    }

    private static void buscarProducto() {
        System.out.println("\nBuscar producto:");
        String nombre = leerTexto("Nombre del producto: ");
        Producto p = inventario.buscarProducto(nombre);

        if (p != null) {
            mostrarProducto(p);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void eliminarProducto() {
        System.out.println("\nEliminar producto:");
        String nombre = leerTexto("Nombre del producto: ");
        Producto p = inventario.buscarProducto(nombre);

        if (p != null) {
            inventario.listarProductos().remove(p);
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void verPrecioConDescuento() {
        System.out.println("\nVer precio con descuento:");
        String nombre = leerTexto("Nombre del producto: ");
        Producto p = inventario.buscarProducto(nombre);

        if (p != null) {
            double descuento = gestor.calcularDescuento(p.getDescripcion(), null);
            double precioFinal = p.getPrecio() * (1 - descuento);
            String resumen = String.format("Descuento %.0f%%", descuento * 100);

            System.out.println("Precio base: $" + formato.format(p.getPrecio()));
            System.out.println("Precio final: $" + formato.format(precioFinal) + " | " + resumen);
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void mostrarProducto(Producto p) {
        double descuento = gestor.calcularDescuento(p.getDescripcion(), null);
        double precioFinal = p.getPrecio() * (1 - descuento);
        String resumen = String.format("Descuento %.0f%%", descuento * 100);

        System.out.println("Nombre: " + p.getNombre()
                + " | Categoria: " + p.getDescripcion()
                + " | Precio base: $" + formato.format(p.getPrecio())
                + " | " + resumen
                + " | Precio final: $" + formato.format(precioFinal)
                + " | Stock: " + p.getStock());
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer
        return valor;
    }

    private static double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.print("Ingrese un valor numerico valido: ");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine(); // limpiar buffer
        return valor;
    }
}