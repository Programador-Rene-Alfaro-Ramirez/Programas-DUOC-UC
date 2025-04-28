package exp5_s7_rene_alfaro;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class EXP5_S7_Rene_Alfaro {
    // Variables estaticas (estaticas globales)
    static double totalIngresos = 0;
    static int totalEntradasVendidas = 0;
    static int totalDescuentosAplicados = 0;
    
    // Variables de instancia
    static ArrayList<String> ubicaciones = new ArrayList<>();
    static ArrayList<Double> preciosFinales = new ArrayList<>();
    static ArrayList<String> descuentosAplicados = new ArrayList<>();
    static ArrayList<String> fechasDeVenta = new ArrayList<>();
    
    // Variables locales
    int capacidadSala = 100;
    static double precioBaseVIP = 10000;
    static double precioBasePlatea = 7000;
    static double precioBaseBalcon = 5000;
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;
            
            while (!salir) {
                System.out.println("\n Bienvenido al Teatro Moro");
                System.out.println("1. Venta de entradas");
                System.out.println("2. Visualizar resumen de ventas");
                System.out.println("3. Generar boleta");
                System.out.println("4. Calcular ingresos totales");
                System.out.println("5. Salir");
                System.out.println("Elige una opcion: ");
                
                int opcion = scanner.nextInt();
                switch (opcion) {
                    case 1:
                        venderEntrada(scanner); 
                        break;
                    case 2:
                        mostrarResumenVentas(); 
                        break;
                    case 3:
                        generarBoleta(); 
                        break;
                    case 4:
                        calcularIngresosTotales(); 
                        break;
                    case 5:
                        salir = true;
                        System.out.println("Gracias por su compra.");
                        break;
                    default:
                        System.out.println("opcion no valida");
                }
            }
        }
    }
    
    public static void venderEntrada (Scanner scanner) {
        System.out.println("Elige la ubicacion (VIP, Platea, Balcon): ");
        String ubicacion = scanner.next();
        double precioBase = 0;
        
        switch (ubicacion.toLowerCase()) {
            case "vip":
                precioBase = precioBaseVIP;
                break;
            case "platea":
                precioBase = precioBasePlatea;
                break;
            case "balcon":
                precioBase = precioBaseBalcon;
                break;
            default:
                System.out.println("Ubicacion no valida.");
                return;
        }
        
        System.out.println("Es estudiante? (si/no): ");
        String esEstudiante = scanner.next();
        System.out.println("es adulto mayor (si/no): ");
        String esAdultoMayor = scanner.next();
        
        double descuento = 0;
        String tipoDescuento = "Ninguno";
        
        if (esEstudiante.equalsIgnoreCase("si")) {
            descuento = 0.10;
            tipoDescuento = "10% Estudiante";
            totalDescuentosAplicados++; //Incrementar Descuentos
        } else if (esAdultoMayor.equalsIgnoreCase("si")) {
            descuento = 0.15;
            tipoDescuento = "15% Adulto Mayor";
            totalDescuentosAplicados++; //Incrementar Descuentos
        }
        
        double precioFinal = precioBase - (precioBase * descuento);
        if (precioFinal < 0) {
            System.out.println ("Error: el precio final no puede ser negativo.");
            return;
        }
        totalIngresos += precioFinal;
        totalEntradasVendidas++;
        
        ubicaciones.add(ubicacion);
        preciosFinales.add(precioFinal);
        descuentosAplicados.add(tipoDescuento);
        fechasDeVenta.add(LocalDate.now().toString()); //Registrar fecha de venta
        
        System.out.println("Entrada vendida exitosamente. Precio final: $" + precioFinal);
    }
    
    public static void mostrarResumenVentas(){
        System.out.println("\nResumen de Ventas:");
        for (int i = 0; i < ubicaciones.size(); i++) {
            System.out.println("Ubicacion: "+ ubicaciones.get(i) +
                              ", Precio Final: $" + preciosFinales.get(i) +
                              ", Descuento: " + descuentosAplicados.get(i) +
                              ", Fecha: " + fechasDeVenta.get(i));
        }     
    }
    
    public static void generarBoleta() {
        System.out.println("\nGenerando boletas detalladas:");
        for (int i = 0; i < ubicaciones.size(); i++) {
            System.out.println("--------------------------------------");
            System.out.println("              Teatro Moro             ");
            System.out.println("--------------------------------------");
            System.out.println("Ubicacion: " + ubicaciones.get(i));
            System.out.println("Fecha: " + fechasDeVenta.get(i));
            System.out.println("Descuento: " + descuentosAplicados.get(i));
            System.out.println("Precio Final: $" + preciosFinales.get(i));
            System.out.println("--------------------------------------");
            System.out.println(" Gracias por su visita al Teatro Moro ");
            System.out.println("--------------------------------------");
        }
    }
    
    public static void calcularIngresosTotales() {
        System.out.println("\nTotal ingresos acumulados: $" + totalIngresos);
        System.out.println("Total de entradas vendidas: " + totalEntradasVendidas);
        System.out.println("Total descuentos aplicados: " + totalDescuentosAplicados);
    }
}
