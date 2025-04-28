package exp_s5_rene_alfaro;
import java.util.ArrayList;
import java.util.Scanner;

public class Exp_s5_Rene_Alfaro {
    // declaracion de la lista para almacenar entradas
    private final ArrayList<String> entradas = new ArrayList<>();
    
    // Variables Estaticas 
    private static final double DESCUENTO_ESTUDIANTE = 10.0;
    private static final double DESCUENTO_ADULTO_MAYOR = 15.0;
    private static final double PRECIO_BASE_VIP = 100000.0;
    private static final double PRECIO_BASE_PLATEA = 70000.0;
    private static final double PRECIO_BASE_GENERAL = 50000.0;
    
    // Variables de instancia
    private int numeroEntrada = 0;
    private double precioFinalEntrada;
    
    public static void main(String[] args) {
        Exp_s5_Rene_Alfaro teatro = new Exp_s5_Rene_Alfaro();
        teatro.mostrarMenu();
    }
    
    // Metodo para mostrar el menú
    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        int opcion;
        
        while (!salir) {
            System.out.println("Asistente de Venta de Entradas del Teatro Moro");
            System.out.println("1. Vender entrada");
            System.out.println("2. Mostrar promociones");
            System.out.println("3. Buscar entrada");
            System.out.println("4. Eliminar entrada");
            System.out.println("5. Salir");
            System.out.println("Seleccione una opcion: ");
            
            opcion = scanner.nextInt();
            
            switch (opcion) {
                    case 1:
                        venderEntrada(scanner);
                        break;
                    case 2:
                        mostrarPromociones();
                        break;
                    case 3:
                        buscarEntrada(scanner);
                        break;
                    case 4:
                        eliminarEntrada(scanner);
                        break;
                    case 5:
                        System.out.println ("Gracias por usar el sistema del Teatro Moro!");
                        salir = true;
                        break;
                    default:
                        System.out.println ("Opcion no valida.porfavor intente nuevamente");
            }
        }
    }
    
    //Metodo para vender una entrada
    private void venderEntrada(Scanner scanner) {
        System.out.println ("\nPlano del Teatro");
        System.out.println ("a) Zona Vip: $100,000");
        System.out.println ("b) Zona Platea: $70,000");
        System.out.println ("c) Zona General: $50,000");
        System.out.println ("seleccione su ubicacion (A, B, C):");
        char zona = scanner.next().toUpperCase().charAt(0);
        
        System.out.print("Eres estudiante? (s/n): ");
        String respuestaEstudiante = scanner.next();
        boolean estudiante = respuestaEstudiante.equalsIgnoreCase("s");
    
        System.out.println ("Eres adulto mayor? (s/n): ");
        String respuestaAdultoMayor = scanner.next();
        boolean adultoMayor = respuestaAdultoMayor.equalsIgnoreCase("s");
        
        double precioBase = determinarPrecioBase(zona);
        if (precioBase == 0.0) {
            System.out.println("Ubicacion no valida. Intente nuevamente.");
            return;
        }
        
        double descuento = calcularDescuento(estudiante, adultoMayor);
        precioFinalEntrada = precioBase - (precioBase * descuento /100);
        
        numeroEntrada++;
        String entrada = "Numero: " + numeroEntrada + "Zona: " + zona + " Precio: $" + precioFinalEntrada; 
        entradas.add(entrada);
        
        System.out.println("Entrada vendida:");
        System.out.println (entrada);  
    }
    
    // Metodo para determinar el precio base segun la ubicacion
    private double determinarPrecioBase(char ubicacion) {
        switch (ubicacion) {
            case 'A':
                return PRECIO_BASE_VIP;
            case 'B':
                return PRECIO_BASE_PLATEA;
            case 'C':
                return PRECIO_BASE_GENERAL;
            default:
                System.out.println("Ubicacion no valida.");
                return 0.0;
        }
    }
    
    // Metodo para calcular el descuento
    private double calcularDescuento(boolean estudiante, boolean adultoMayor) {
        if (adultoMayor) {
            return DESCUENTO_ADULTO_MAYOR;
        } else if (estudiante) {
            return DESCUENTO_ESTUDIANTE;
        } else {
            return 0.0; // Sin descuento
        }
    }
    
    // Metodo para mostrar las promociones disponibles
    private void mostrarPromociones() {
        System.out.println("Promociones:");
        System.out.println("1. Descuento del 10% para estudiantes ");
        System.out.println("2. Descuento del 15% para adultos mayores.");
    }
    
    // Metodo para buscar una entrada
    private void buscarEntrada(Scanner scanner) {
        System.out.println("Ingrese el numero de entrada a buscar:");
        int numero = scanner.nextInt();
        boolean encontrado = false;
        
        for (String entrada : entradas) {
            if (entrada.contains("Numero: " + numero)) {
                System.out.println("Entrada encontrada: " + entrada);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            System.out.println("No se encontro ninguna entrada con ese numero.");   
        }
    }
    
    // Metodo para eliminar una entrada
    private void eliminarEntrada(Scanner scanner) {
        System.out.println("Ingrese el numero de entrada a eliminar:");
        int numero = scanner.nextInt();
        boolean eliminado = false;
        
        for (int i = 0; i < entradas.size(); i++) {
            if (entradas.get(i).contains("Numero: " + numero)) {
                System.out.println("Entrada eliminada: "+ entradas.get(i));
                entradas.remove(i);
                eliminado = true;
                break;
            }
        }
        
        if (!eliminado) {
            System.out.println("No se encontro ninguna entrada con ese numero.");
        }
    }
}
