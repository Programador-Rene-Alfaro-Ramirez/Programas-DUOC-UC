package mavenproject1.view;

import mavenproject1.controller.PedidoController;
import mavenproject1.controller.ProductoController;
import mavenproject1.command.AgregarProductoCommand;
import mavenproject1.command.EliminarProductoCommand;
import mavenproject1.model.Producto;
import mavenproject1.model.Usuario;
import mavenproject1.model.Component;
import mavenproject1.model.PrecioBase;
import mavenproject1.model.Descuento10;
import mavenproject1.model.DiscountManager;

import java.util.List;
import java.util.Scanner;

public class ConsolaView {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Usuario usuario = new Usuario("Rene", "rene@email.com");
            ProductoController productoController = new ProductoController();
            PedidoController pedidoController = new PedidoController(usuario);
            
            System.out.println("Bienvenido a la tienda de ropa MVC\n");
            
            boolean ejecutando = true;
            while (ejecutando) {
                System.out.println("\n--- MENU PRINCIPAL ---");
                System.out.println("1. Agregar producto");
                System.out.println("2. Ver productos");
                System.out.println("3. Crear pedido");
                System.out.println("4. Eliminar producto");
                System.out.println("5. Salir");
                System.out.println("6. Ver estado del pedido");
                System.out.println("7. Finalizar compra");
                System.out.print("Seleccione una opcion: ");
                
                String opcion = scanner.nextLine().trim();
                
                switch (opcion) {
                    case "1":
                        System.out.print("Nombre del producto: ");
                        String nombre = scanner.nextLine();
                        
                        System.out.print("Descripcion: ");
                        String descripcion = scanner.nextLine();
                        
                        System.out.print("Precio: ");
                        double precio;
                        try {
                            precio = Double.parseDouble(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR:️ Precio inválido. Intente nuevamente.");
                            break;
                        }
                        
                        Producto nuevoProducto = new Producto(nombre, descripcion, precio);
                        new AgregarProductoCommand(productoController, nuevoProducto).ejecutar();
                        System.out.println("Producto agregado correctamente.");
                        break;
                        
                    case "2":
                        List<Producto> productos = productoController.getProductos();
                        if (productos.isEmpty()) {
                            System.out.println("No hay productos registrados.");
                        } else {
                            System.out.println("\nLista de productos:");
                            for (int i = 0; i < productos.size(); i++) {
                                Producto p = productos.get(i);
                                System.out.printf("%d. %s - $%.2f\n", i + 1, p.getNombre(), p.getPrecio());
                            }
                        }
                        break;
                        
                    case "3":
                        List<Producto> disponibles = productoController.getProductos();
                        if (disponibles.isEmpty()) {
                            System.out.println("ERROR: No hay productos disponibles para crear un pedido.");
                            break;
                        }
                        
                        System.out.println("\nSeleccione productos por numero (separados por coma):");
                        for (int i = 0; i < disponibles.size(); i++) {
                            Producto p = disponibles.get(i);
                            System.out.printf("%d. %s - $%.2f\n", i + 1, p.getNombre(), p.getPrecio());
                        }
                        
                        String[] seleccion = scanner.nextLine().split(",");
                        for (String indiceStr : seleccion) {
                            try {
                                int indice = Integer.parseInt(indiceStr.trim()) - 1;
                                if (indice >= 0 && indice < disponibles.size()) {
                                    Producto seleccionado = disponibles.get(indice);
                                    pedidoController.agregarProducto(seleccionado);
                                } else {
                                    System.out.println("ERROR: Indice fuera de rango: " + (indice + 1));
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("ERROR: Entrada invalida: " + indiceStr);
                            }
                        }
                        
                        System.out.println("Pedido creado con los productos seleccionados.");
                        break;
                        
                    case "4":
                        List<Producto> lista = productoController.getProductos();
                        if (lista.isEmpty()) {
                            System.out.println("No hay productos para eliminar.");
                            break;
                        }
                        
                        System.out.println("\nSeleccione el numero del producto a eliminar:");
                        for (int i = 0; i < lista.size(); i++) {
                            Producto p = lista.get(i);
                            System.out.printf("%d. %s - $%.2f\n", i + 1, p.getNombre(), p.getPrecio());
                        }
                        
                        System.out.print("Numero: ");
                        try {
                            int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                            if (indice >= 0 && indice < lista.size()) {
                                Producto productoAEliminar = lista.get(indice);
                                new EliminarProductoCommand(productoController, productoAEliminar).ejecutar();
                                System.out.println("Producto eliminado satisfactoriamente.");
                            } else {
                                System.out.println("ERROR: Índice fuera de rango.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("ERROR: Entrada invalida.");
                        }
                        break;
                        
                    case "5":
                        ejecutando = false;
                        System.out.println("Gracias por usar el sistema. Hasta pronto!");
                        break;
                        
                    case "6":
                        List<Producto> productosPedido = pedidoController.getProductos();
                        if (productosPedido.isEmpty()) {
                            System.out.println("El pedido está vacío.");
                        } else {
                            System.out.println("\nProductos en el pedido:");
                            for (Producto p : productosPedido) {
                                System.out.printf("- %s: $%.2f\n", p.getNombre(), p.getPrecio());
                            }
                        }
                        break;
                        
                    case "7":
                        List<Producto> productosFinales = pedidoController.getProductos();
                        if (productosFinales.isEmpty()) {
                            System.out.println("No hay productos en el pedido.");
                            break;
                        }
                        
                        double total = 0;
                        System.out.println("\nResumen del pedido:");
                        for (Producto p : productosFinales) {
                            System.out.printf("- %s: $%.2f\n", p.getNombre(), p.getPrecio());
                            total += p.getPrecio();
                        }
                        
                        Component precioBase = new PrecioBase(total);
                        Component conDescuento = new Descuento10(precioBase);
                        double totalConDescuento = conDescuento.calcularPrecio();
                        
                        DiscountManager.getInstance().registrarDescuento("Descuento aplicado: 10% sobre total");
                        
                        System.out.printf("\nTotal sin descuento: $%.2f\n", total);
                        System.out.printf("Total con descuento: $%.2f\n", totalConDescuento);
                        
                        System.out.println("\nDescuentos registrados:");
                        for (String d : DiscountManager.getInstance().getDescuentosAplicados()) {
                            System.out.println("- " + d);
                        }
                        
                        pedidoController.finalizarPedido();
                        System.out.println("Compra finalizada. Gracias por su pedido.");
                        break;
                        
                    default:
                        System.out.println("ERROR: Opcion invalida. Intente nuevamente.");
                }
            }
        }
    }
}
