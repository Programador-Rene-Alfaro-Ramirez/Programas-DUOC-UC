package exp2_s6_rene_alfaro;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;

public class EXP2_S6_Rene_Alfaro {
// declaracion de la lista para almacenar entradas
    static final String NOMBRE_TEATRO = "Teatro Moro";
    static final int CAPACIDAD_SALA = 100;
    static final double PRECIO_ENTRADA = 10000.0;
    static final long TIEMPO_RESERVA_MS = 60000; // tiempo limite 1 minuto
    
    // Estado de asientos
    static boolean[] asientosReservados = new boolean [CAPACIDAD_SALA];
    static boolean[] asientosComprados = new boolean [CAPACIDAD_SALA];
    static Timer [] temporizadores = new Timer[CAPACIDAD_SALA];
    
    // Registro de ventas
    static Map<Integer, Venta> ventas = new HashMap<>();
    
    public static void main (String[] args) {
        try (Scanner scanner = new Scanner(System.in)) { //Breakpoint: verificar el valor inicial
            int opcion; 
            
            do { 
                mostrarMenu();
                while (!scanner.hasNextInt()) {
                    System.out.print("Ingrese un numero valido: ");
                    scanner.next();
                }
                opcion = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcion) { 
                    case 1 -> reservarAsiento(scanner); // Breakpoint: Confirmar que el asiento reservado esta ocupado antes de la compra.
                    case 2 -> comprarAsiento(scanner); 
                    case 3 -> modificarVenta(scanner); 
                    case 4 -> imprimirBoleta(scanner); 
                    case 5 -> cerrarPrograma();        
                }
            } while (opcion !=5); // BReakpoint: correccion de variable N°0 por N°5 y cambio de =/= por !=
        }
    }
    
    static void mostrarMenu() {
        System.out.println("\nMENU DE VENTAS DEL TEATRO MORO"); 
        System.out.println("1. Reservar asiento");
        System.out.println("2. Comprar asiento");
        System.out.println("3. Modificar venta");
        System.out.println("4. Imprimir boleta");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opcion: ");
    }
    
    static void reservarAsiento(Scanner scanner) {
        System.out.print("Ingrese el numero de asiento a reservar (1-" + CAPACIDAD_SALA + "): ");
        int asiento = leerNumeroAsiento(scanner); // Breakpoint: Confirmar que el asiento reservado esta ocupado antes de la compra.
        
        if (esAsientoValido(asiento)) { //Breakpoint: confirmar que el asiento esta disponible para reservar.
            if (!asientosReservados[asiento] && !asientosComprados[asiento]) { //Breakpoint: Verificar que el numero de asiento ingresado esta dentro del rango válido. 
                asientosReservados[asiento] = true;
                iniciarTemporizador(asiento);
                System.out.println("Asiento " + (asiento + 1) + "reservado por 1 minuto-"); 
            } else {
                System.out.println("El asiento ya esta reservado o comprado."); //Breakpoint: verificar el estado de reserva antes de continuar.
            } 
        } else {
            System.out.println("Numero de asiento invalido.");
        }
    }
        
    static void iniciarTemporizador(int asiento) {
        temporizadores[asiento] = new Timer();
        temporizadores [asiento].schedule(new TimerTask() { 
            @Override
            public void run() {
                asientosReservados[asiento] = false;
                System.out.println("\nLa reserva del asiento "+ (asiento +1) + "ha expirado.");
            }
        }, TIEMPO_RESERVA_MS);
    }
    
    static void comprarAsiento(Scanner scanner) {
        System.out.print("Ingrese el numero de asiento a comprar (1-" + CAPACIDAD_SALA + "): ");
        int asiento = leerNumeroAsiento(scanner);
        
        if (esAsientoValido(asiento)) {
            if (!asientosComprados[asiento]) {
                asientosComprados[asiento] = true;
                asientosReservados[asiento] = false;
                
                if (temporizadores[asiento] != null) temporizadores[asiento].cancel(); 
                
                Venta venta = new Venta(asiento + 1, PRECIO_ENTRADA); 
                ventas.put(asiento + 1, venta); //Breakpoint: verificar que la compra se registro exitosamente.
                System.out.println("Compra exitosa para el asiento " + (asiento +1));
            } else {
                System.out.println("El asiento ya fue comprado.");
            }
        } else {
            System.out.println("Numero de asiento invalido.");
        }
    }
    
    static void modificarVenta(Scanner scanner){
        System.out.print("Ingrese el numero de asiento comprado a modificar (1-" + CAPACIDAD_SALA + "): ");
        int asientoAntiguo = leerNumeroAsiento(scanner);
        
        if (esAsientoValido(asientoAntiguo) && asientosComprados[asientoAntiguo]) {
            System.out.print("Ingrese el nuevo numero de asiento (1-" + CAPACIDAD_SALA + "): "); 
            int asientoNuevo = leerNumeroAsiento(scanner);
            
            if (esAsientoValido(asientoNuevo) && !asientosComprados[asientoAntiguo]) {
                asientosComprados[asientoAntiguo] = false;
                asientosComprados[asientoNuevo] = true;
            
                Venta venta = ventas.remove(asientoAntiguo +1);
                venta.setNumeroAsiento(asientoNuevo + 1);
                ventas.put(asientoNuevo + 1, venta);
            
                System.out.println("Venta modificada exitosamente.");
            } else {
                System.out.println("El nuevo asiento no esta disponible");
            }
        } else {
            System.out.println("Asiento no valido o no comprado.");
        }
    }
      
    static void imprimirBoleta(Scanner scanner) {
        System.out.println("Ingrese el numero de asiento para imprimir boleta (1-" + CAPACIDAD_SALA + "): "); //Breakpoint: verificar que se recibe el asiento correectamente. 
        int asiento = leerNumeroAsiento(scanner) +1;
        
        Venta venta = ventas.get(asiento); //Breakpoint: Confirmar que se recuperan los datos correctos de la venta.
        if (venta != null) {
            System.out.println("\n--- BOLETA DE COMPRA ---"); //Breakpoint: Verificar que se genera la boleta con todos los datos esperados y corregir errores ortograficos. 
            System.out.println("Teatro: "+ NOMBRE_TEATRO);
            System.out.println("Asiento: "+ venta.getNumeroAsiento());
            System.out.println("Precio: $" + venta.getPrecio());
            System.out.println("------------------------\n");
        } else {
            System.out.println("No se encontro una boleta para ese asiento.");
        }
    }
    
    static void cerrarPrograma() {
        System.out.println("Gracias por usar el sistema del Teatro Moro. Hasta Pronto!"); 
        double ingresosTotales = ventas.size() * PRECIO_ENTRADA;
        System.out.println("Total de ventas: "+ ventas.size());
        System.out.println("Ingresos totales: $" + ingresosTotales);
    }
    
    static boolean esAsientoValido(int asiento) {
        return asiento >= 0 && asiento < CAPACIDAD_SALA;
    }
    
    static int leerNumeroAsiento(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            scanner.next();
        }
        return scanner.nextInt() -1;
    }
}

class Venta {
    private int numeroAsiento;
    private final double precio;
    
    public Venta(int numeroAsiento, double precio) {
        this.numeroAsiento = numeroAsiento;
        this.precio = precio;

    }
    
    public int getNumeroAsiento() {
        return numeroAsiento;
    }
    
    public void setNumeroAsiento(int numeroAsiento){
        this.numeroAsiento = numeroAsiento;
    }
    
    public double getPrecio() {
        return precio;
    }
}