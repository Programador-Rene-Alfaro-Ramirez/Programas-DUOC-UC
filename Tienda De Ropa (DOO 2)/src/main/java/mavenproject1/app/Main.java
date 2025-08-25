package mavenproject1.app;

import java.util.Scanner;
import mavenproject1.modelo.Component;
import mavenproject1.modelo.Producto;
import mavenproject1.negocio.DiscountManager;
import mavenproject1.comandos.*;
import mavenproject1.descuentos.*;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            DiscountManager manager = DiscountManager.getInstancia();
            Invoker invoker = new Invoker();
            
            boolean salir = false;
            
            while (!salir) {
                System.out.println("\n Menu Tienda de Ropa");
                System.out.println("1. Ver productos disponibles");
                System.out.println("2. Seleccionar producto y aplicar descuentos");
                System.out.println("3. Mostrar carrito");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opcion: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer
                
                switch (opcion) {
                    case 1:
                        System.out.println("\nProductos disponibles:");
                        System.out.println("a) Camisa - $20.000");
                        System.out.println("b) Pantalon - $25.000");
                        System.out.println("c) Chaqueta - $40.000");
                        break;
                        
                    case 2:
                        System.out.print("Seleccione producto (a/b/c): ");
                        String seleccion = scanner.nextLine();
                        Component producto = null;
                        
                        switch (seleccion.toLowerCase()) {
                            case "a":
                                producto = new Producto("Camisa", 20000);
                                break;
                            case "b":
                                producto = new Producto("Pantalon", 25000);
                                break;
                            case "c":
                                producto = new Producto("Chaqueta", 40000);
                                break;
                            default:
                                System.out.println("Opcion invalida.");
                                continue;
                        }
                        
                        System.out.println("Desea aplicar descuentos?");
                        System.out.println("1. Descuento 10%");
                        System.out.println("2. Descuento por categoria");
                        System.out.println("3. Ambos");
                        System.out.println("4. Ninguno");
                        System.out.print("Seleccione opcion de descuento: ");
                        int descuento = scanner.nextInt();
                        scanner.nextLine();
                        
                        switch (descuento) {
                            case 1:
                                producto = new Descuento10(producto);
                                break;
                            case 2:
                                producto = new DescuentoCategoria(producto);
                                break;
                            case 3:
                                producto = new DescuentoCategoria(new Descuento10(producto));
                                break;
                            case 4:
                                // sin descuento
                                break;
                            default:
                                System.out.println("Opcion invalida.");
                                continue;
                        }
                        
                        Command agregar = new AgregarProducto(manager, producto);
                        invoker.agregarComando(agregar);
                        invoker.ejecutarComandos();
                        System.out.println("Producto agregado al carrito.");
                        break;
                        
                    case 3:
                        manager.mostrarCarrito();
                        break;
                        
                    case 4:
                        salir = true;
                        System.out.println("Gracias por visitar la tienda.");
                        break;
                        
                    default:
                        System.out.println("Opcion invalida.");
                }
            }
        }
    }
}
