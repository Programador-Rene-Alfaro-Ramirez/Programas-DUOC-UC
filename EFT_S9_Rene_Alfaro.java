package eft_s9_rene_alfaro;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class EFT_S9_Rene_Alfaro {
    public static void main(String[] args) {
        ArrayList<String> asientosDisponibles = new ArrayList<>();
        List<String> asientosVendidos = new ArrayList<>();
        ArrayList<String> asientosReservadosTemporalmente = new ArrayList<>();
        List<String> tiposAsientos = List.of("vip", "palco", "platea baja", "platea alta", "galeria");
        int [] precios = {50000, 40000, 30000, 20000, 10000};
        List<String> tiposClientes = List.of("infantil", "estudiante", "tercera edad");
        double[] descuentos = {0.10, 0.15, 0.25};
        String continuarCompra;

        try (Scanner scanner = new Scanner(System.in)) { // Breakpoint: Colocar el try-with-resources y que el programa se inicie correctamente.
            do { // Breakpoint: Mostrar lista de asientos disponibles con número único por categoría
                asientosDisponibles.clear();
                for (int i = 1; i <= 20; i++) {
                    for (String tipo : tiposAsientos) {
                        asientosDisponibles.add(tipo + " " + i);
                    }
                }

                asientosReservadosTemporalmente.clear();
                System.out.println("=== Bienvenidos al sistema de venta de entradas del Teatro Moro ===");
                System.out.print("Por favor, Ingrese su nombre: ");
                String nombre = scanner.nextLine().toLowerCase();

                int edad = -1;
                while (edad < 0) {
                    System.out.print("Ingrese su edad: ");
                    try {
                        edad = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) { // Breakpoint: Depurar el manejo de la excepción cuando el usuario ingresa una entrada no válida para la edad. Asegura que el programa no se bloquee y que se muestre el error.
                        System.out.println("Error: Por favor ingrese un numero valido.");
                        scanner.nextLine();
                    }
                }

                String genero = "";
                while (!genero.equalsIgnoreCase("hombre") && !genero.equalsIgnoreCase("mujer")) {
                    System.out.print("Es usted hombre o mujer?: "); //Breakpoint: validar el sexo de la persona con sus respectivos descuentos.
                    genero = scanner.nextLine().toLowerCase();
                    if (!genero.equalsIgnoreCase("hombre") && !genero.equalsIgnoreCase("mujer")) {
                        System.out.println("Error: Por favor ingrese si es 'hombre' o 'mujer'.");
                    }
                }

                double descuentoCalculado = 0.0;
                String tipoCliente = "ninguno"; // Valor por defecto

                if (genero.equalsIgnoreCase("mujer")) {
                    System.out.print("Ingrese su tipo de cliente (infantil, estudiante, tercera edad, o 'ninguno'): "); // Nuevo: Preguntar tipo de cliente para mujer
                    tipoCliente = scanner.nextLine().toLowerCase();

                    if (tiposClientes.contains(tipoCliente)) {
                        int indiceTipoCliente = tiposClientes.indexOf(tipoCliente);
                        descuentoCalculado = descuentos[indiceTipoCliente];
                    } else if (!tipoCliente.equalsIgnoreCase("ninguno") && !tipoCliente.isEmpty()) {
                        System.out.println("Error: Tipo de cliente invalido. No se aplicará descuento.");
                    } else {
                      descuentoCalculado = 0.20;
                    }
                } else {
                    System.out.print("Ingrese su tipo de cliente (infantil, estudiante, tercera edad, o 'ninguno'): "); //Breakpoint: clasificar el tipo de cliente con sus descuentos respectivos incluyendo el cliente que no cumple con los requisitos no reciba el descuento.
                    tipoCliente = scanner.nextLine().toLowerCase();

                    if (tiposClientes.contains(tipoCliente) && tipoCliente.equals("infantil")) { // Breakpoint: verificar que se cumpla el descuento infantil
                        descuentoCalculado = descuentos[tiposClientes.indexOf(tipoCliente)];
                    } else if (tiposClientes.contains(tipoCliente)) { //Breakpoint: Verificar que se cumplan los descuentos a otros tipos de clientes
                        int indiceTipoCliente = tiposClientes.indexOf(tipoCliente);
                        descuentoCalculado = descuentos[indiceTipoCliente];
                    } else if (!tipoCliente.equalsIgnoreCase("ninguno") && !tipoCliente.isEmpty()) { //Breakpoint: Verificar que el tipo de cliente que no cumpla con ninguno de los tipos de cliente no reciba el descuento.
                        System.out.println("Error: Tipo de cliente invalido. No se aplicará descuento."); //Breakpoint: el cliente que no cumpla ningun requisito del tipo de cliente recibe 0% de descuento
                    }
                }

                // Mostrar Asientos Disponibles
                System.out.println("\nAsientos disponibles:"); //Breakpoint: mostrar un sistema de asientos de forma ordenada y facil de leer
                for (String tipo : tiposAsientos) {
                    System.out.print(tipo.toUpperCase() + ": ");
                    for (int i = 1; i <= 20; i++) {
                        String asiento = tipo + " " + i;
                        if (asientosDisponibles.contains(asiento)) { //Breakpoint: Asegurar que solo se muestren los asientos que están realmente disponibles. Esto es importante para evitar errores al seleccionar asientos no disponibles.
                            System.out.print(i + " ");
                        }
                    }
                    System.out.println();
                }

                List<String> asientosSeleccionados = new ArrayList<>();
                String continuar = "si";

                while (continuar.equalsIgnoreCase("si") || continuar.equalsIgnoreCase("s")) { // Breakpoint: Verificar que el bucle de selección de asientos funcione correctamente, permitiendo al usuario seleccionar múltiples asientos.
                    String tipoAsiento;
                    String numeroAsiento;
                    String asientoFinal;

                    while (true) {
                        System.out.println("Ingrese la categoria de asiento (vip, palco, platea baja, platea alta, galeria): ");
                        tipoAsiento = scanner.nextLine().toLowerCase();

                        if (!tiposAsientos.contains(tipoAsiento)) { //Breakpoint: Validar la entrada de la categoria del asiento.
                            System.out.println("Error: Categoria invalida. Intente nuevamente.");
                            continue;
                        }

                        System.out.print("Ingrese el numero de asiento (1 al 20): ");
                        numeroAsiento = scanner.nextLine();

                        try {
                            int numAsiento = Integer.parseInt(numeroAsiento);
                            if (numAsiento < 1 || numAsiento > 20) { //Breakpoint: validar el numero de asiento.
                                System.out.println("Error: Numero de asiento invalido. Debe estar entre 1 y 20.");
                                continue;
                            }
                        } catch (NumberFormatException e) { //Breakpoint: Manejar la excepcion si el usuario ingresa un valor no numerico para el numero de asiento.
                            System.out.println("Error: Numero de asiento invalido. Debe ser un numero.");
                            continue;
                        }

                        asientoFinal = tipoAsiento + " " + numeroAsiento;

                        if (!asientosDisponibles.contains(asientoFinal)) { //Breakpoint: Verificar que el asiento seleccionado este disponible
                            System.out.println("Error: Ese asiento no esta disponible o ha sido vendido. Intente otro.");
                        } else if (asientosReservadosTemporalmente.contains(asientoFinal) || asientosSeleccionados.contains(asientoFinal)) { //Breakpoint: Verificar si el asiento ya está reservado temporalmente o ya ha sido seleccionado por el mismo usuario.
                            System.out.println("Error: Ese asiento ya esta reservado por otro cliente o lo seleccionaste antes. Elige otro.");
                        } else {
                            break;
                        }
                    }

                    asientosSeleccionados.add(asientoFinal);
                    System.out.print("Desea seleccionar otro asiento? (si/no): "); //Breakpoint: validar que la respuesta sea con mayusculas o minusculas incluyendo solo las letras 's' o 'n'
                    continuar = scanner.nextLine().toLowerCase();
                }

                System.out.println("\nSe han reservado los siguientes asientos: " + asientosSeleccionados);
                System.out.print("Desea confirmar su compra? (si/no): "); //Breakpoint: validar que la respuesta sea con mayusculas o minusculas incluyendo solo las letras 's' o 'n'
                String confirmacion = scanner.nextLine().toLowerCase();

                if (confirmacion.equalsIgnoreCase("si") || confirmacion.equalsIgnoreCase("s")) { //Breakpoint: Verificar la logica de la confirmacion de la compra.
                    double precioTotal = 0.0;

                    for (String asiento : asientosSeleccionados) { //Breakpoint: Depurar el calculo del precio total de la compra
                        String categoria = asiento.split(" ")[0];
                        int indiceTipoAsiento = tiposAsientos.indexOf(categoria);
                        double precioFinal = precios[indiceTipoAsiento] * (1 - descuentoCalculado);
                        precioTotal += precioFinal;

                        asientosVendidos.add(asiento);
                        asientosDisponibles.remove(asiento);
                        asientosReservadosTemporalmente.remove(asiento);
                    }
                    
                    System.out.println("------------------------------------");
                    System.out.println("            Teatro Moro             ");
                    System.out.println("------------------------------------");
                    System.out.println("============== BOLETA =============="); //Breakpoint: Verificar la generacion de la boleta
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Edad: " + edad);
                    System.out.println("Genero: " + genero);
                    System.out.println("Tipo de Cliente: " + tipoCliente); // Breakpoint: Agregar el tipo de cliente en la boleta
                    System.out.println("Asientos comprados: " + asientosSeleccionados);
                    System.out.printf("Descuento aplicado: %.2f%%\n", (descuentoCalculado * 100));
                    System.out.printf("Total a pagar: $%.2f\n", precioTotal);
                    System.out.println("====================================");

                    System.out.println("Compra confirmada.");
                    System.out.println("Gracias por su visita al Teatro Moro");
                    System.out.println("Esperamos que disfrutes del evento!");
                    
                    System.out.println("====================================");
                } else {
                    System.out.println("\nReserva cancelada. Los asientos siguen disponibles.");
                    asientosReservadosTemporalmente.removeAll(asientosSeleccionados);
                }
                
                System.out.println("\nAsientos vendidos:"); //Breakpoint: mostrar la lista de los asientos vendidos al final
                System.out.println(asientosVendidos);

                System.out.print("\nDesea realizar otra compra? (si/no): "); //Breakpoint: preguntar al cliente si desea realizar otra compra
                continuarCompra = scanner.nextLine().toLowerCase();
            } while (continuarCompra.equalsIgnoreCase("si") || continuarCompra.equalsIgnoreCase("s")); //Breakpoint: Verificar la condicion de la continuacion del bucle principal
        }
    }
}