package exp_s4_rene_alfaro;
import java.util.Scanner;
import java.text.NumberFormat;
import java.util.Locale;
public class Exp_s4_Rene_Alfaro {
    public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
       boolean salir = false;
 
       for (;;) {
           if (salir) break;
           System.out.println ("\nCompra Entradas Teatro Moro");
           System.out.println ("1. Comprar Entrada");
           System.out.println ("2. Salir");
           System.out.println ("Ingrese opcion: ");
           int opcion = scanner.nextInt();
           
           if (opcion == 2) {
               System.out.println ("Saliendo del programa...");
               salir = true;
               break;
           } else if (opcion != 1) {
               System.out.println ("Opcion invalida. por favor, intente nuevamente.");
               continue;
           }
           
           boolean realizarOtraCompra = true;
           while (realizarOtraCompra) {
               //mostrar plano del teatro
               System.out.println("\nPlano del Teatro");
               System.out.println("1. Zona A: $100000");
               System.out.println("2. Zona B: $70000");
               System.out.println("3: Zona C: $50000");
               System.out.println("seleccione su ubicacion (A, B, C):");
               char zona = scanner.next().toUpperCase().charAt(0);
               int precioBase = 0;
               
               if (zona == 'A') {
                   precioBase = 100000;
               } else if (zona =='B') {
                   precioBase = 70000;
               } else if (zona =='C') {
                   precioBase =50000;
               } else {
                   System.out.println("Zona invalida. por favor intente nuevamente.");
                   continue;
               }
               
               //Solicitar edad
               System.out.println("ingrese su edad: ");
               int edad = scanner.nextInt();
               if (edad <= 0) {
                   System.out.println("Edad invalida. por favor intente nuevamente.");
                   continue;
               }
               
                //Calcular descuento
                double descuento = 0.0;
                if (edad >= 65) {
                    descuento = 15; // 15% de descuento para adulto mayor
                } else if (edad <= 20) {
                  descuento = 10; //10% de descuento para estudiantes
                }
                 
                //calcular precio final
                double precioFinal = (int) (precioBase - (precioBase * descuento /100.0));
                
                //mostrar resumen
                System.out.println("\nCompra realizada:");
                System.out.println("Ubicacion del asiento: Zona " + (char) ('A' + zona - 1));
                System.out.println("Precio base: $" + precioBase);
                System.out.println("Descuento aplicado: " + descuento + "%");
                System.out.println("Total a pagar: $" + precioFinal);                
                //preguntar si desea realizar otra compra
                System.out.println("\nDesea realizar otra compra? (s/n): ");
                char respuesta = scanner.next().toLowerCase().charAt(0);
                realizarOtraCompra = (respuesta == 's');
                salir = (respuesta == 'n');
        }
           }
    scanner.close();
    System.out.println("programa finalizado.Hasta pronto!");
       }
    }
        
       

    
        
