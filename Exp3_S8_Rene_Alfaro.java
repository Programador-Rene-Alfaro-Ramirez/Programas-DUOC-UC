package exp3_s8_rene_alfaro;

import java.util.ArrayList;
import java.util.Scanner;

// === Clases auxiliares ===

class Asiento {
    int idAsiento;
    boolean ocupado;

    public Asiento(int idAsiento) {
        this.idAsiento = idAsiento;
        this.ocupado = false;
    }
}

class Cliente {
    static int contadorId = 1;
    int idCliente;
    String nombre;
    boolean esEstudiante;
    boolean esAdultoMayor;

    public Cliente(String nombre, boolean esEstudiante, boolean esAdultoMayor) {
        this.idCliente = contadorId++;
        this.nombre = nombre;
        this.esEstudiante = esEstudiante;
        this.esAdultoMayor = esAdultoMayor;
    }
}

class Venta {
    static int contadorId = 1;
    int idVenta;
    Cliente cliente;
    Asiento asiento;
    double precio;

    public Venta(Cliente cliente, Asiento asiento, double precio) {
        this.idVenta = contadorId++;
        this.cliente = cliente;
        this.asiento = asiento;
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ID Venta: " + idVenta +
               ", Cliente: " + cliente.nombre +
               " (ID: " + cliente.idCliente + ")" +
               ", Asiento: " + asiento.idAsiento +
               ", Precio: $" + precio;
    }
}

class Descuento {
    String tipo;
    double valor;

    public Descuento(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
}

// === Clase principal ===

public class Exp3_S8_Rene_Alfaro {

    static ArrayList<Asiento> asientos = new ArrayList<>();
    static ArrayList<Venta> ventas = new ArrayList<>();
    static ArrayList<Descuento> descuentos = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarAsientos();
        cargarDescuentosIniciales();

        boolean salir = false;
        while (!salir) {
            try {
                System.out.println("\n=== TEATRO MORO ===");
                System.out.println("1. Venta de Entrada");
                System.out.println("2. Ver Ventas");
                System.out.println("3. Ver Asientos Disponibles");
                System.out.println("4. Salir");
                System.out.print("Elige una opcion: ");

                int opcion = scan.nextInt();
                scan.nextLine();

                switch (opcion) { // Breakpoint: Validar opción ingresada en menú principal
                    case 1 -> venderEntrada();
                    case 2 -> mostrarVentas();
                    case 3 -> mostrarAsientosDisponibles();
                    case 4 -> salir = true;
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (Exception e) {
                System.out.println("Error: Ingrese un numero valido.");
                scan.nextLine();
            }
        }
    }

    static void inicializarAsientos() {
        int cantidadAsientos = 50; // Breakpoint: optimizar el codigo para definir la cantidad de asientos antes del bucle
        asientos = new ArrayList<>(cantidadAsientos);
        
        for (int i = 1; i <= cantidadAsientos; i++) {
            asientos.add(new Asiento(i));
        }
    }

    static void cargarDescuentosIniciales() {
        descuentos.add(new Descuento("Estudiante", 0.10));
        descuentos.add(new Descuento("Adulto Mayor", 0.15));
    }

    static void venderEntrada() {
        System.out.println("\n--- Venta de Entrada ---");
        System.out.print("Nombre del cliente: ");
        String nombre = scan.nextLine();

        int numeroAsiento;
        while (true) {
            try {
                System.out.print("Ingrese numero de asiento (1-50): ");
                numeroAsiento = scan.nextInt();
                scan.nextLine();

                if (numeroAsiento < 1 || numeroAsiento > 50) { // :Breakpoint: Validar rango del número de asiento
                    System.out.println("Error: Numero de asiento fuera del rango.");
                    continue;
                }

                Asiento asientoSeleccionado = asientos.get(numeroAsiento - 1);
                
                if (asientoSeleccionado.ocupado) { // Breakpoint: Verificar si el asiento ya está ocupado
                    System.out.println("Error: Asiento ocupado. Por favor elija otro.");
                    continue;
                }

                break;
            } catch (Exception e) {
                System.out.println("Error: Debe ingresar un numero valido.");
                scan.nextLine();
            }
        }
   
        System.out.print("Es estudiante? (s/n): "); // Breakpoint: Validar tipo de cliente
        boolean esEstudiante = scan.nextLine().equalsIgnoreCase("s");

        System.out.print("Es adulto mayor? (s/n): ");
        boolean esAdultoMayor = scan.nextLine().equalsIgnoreCase("s");

        double precioBase = 10000;
        double descuento = obtenerDescuento(esEstudiante, esAdultoMayor); // Breakpoint: Aplicar descuentos
        double precioFinal = precioBase * (1 - descuento);

        Cliente cliente = new Cliente(nombre, esEstudiante, esAdultoMayor);
        Asiento asiento = asientos.get(numeroAsiento - 1);
        asiento.ocupado = true;

        ventas.add(new Venta(cliente, asiento, precioFinal)); // Breakpoint: Registrar nueva venta

        System.out.println("Entrada vendida con exito para el asiento " + asiento.idAsiento + ". Total a pagar: $" + precioFinal);
    }

    static double obtenerDescuento(boolean esEstudiante, boolean esAdultoMayor) { // Breakpoint: Verificar descuento aplicado por tipo
        double descuentoTotal = 0.0;

        if (esEstudiante) descuentoTotal += buscarDescuento("Estudiante");
        if (esAdultoMayor) descuentoTotal += buscarDescuento("Adulto Mayor");

        return descuentoTotal;
    }

    static double buscarDescuento(String tipo) {
        for (Descuento d : descuentos) {
            if (d.tipo.equalsIgnoreCase(tipo)) {
                return d.valor;
            }
        }
        return 0.0;
    }

    static void mostrarVentas() {
        System.out.println("\n--- Ventas Registradas ---");

        if (ventas.isEmpty()) { // Breakpoint: Mostrar ventas registradas
            System.out.println("No hay ventas registradas.");
        } else {
            ventas.forEach(System.out::println);
        }
    }

    static void mostrarAsientosDisponibles() {
        System.out.println("\n--- Asientos Disponibles ---");
        for (Asiento a : asientos) {
            if (!a.ocupado) System.out.print(a.idAsiento + " ");
        }
        System.out.println();
    }
}