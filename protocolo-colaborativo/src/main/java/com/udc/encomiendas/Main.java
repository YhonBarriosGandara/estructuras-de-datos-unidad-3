package com.udc.encomiendas;

import com.udc.encomiendas.modelo.Cliente;
import com.udc.encomiendas.modelo.Encomienda;
import com.udc.encomiendas.modelo.PrioridadEncomienda;
import com.udc.encomiendas.servicio.GestionEncomiendasService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GestionEncomiendasService service = new GestionEncomiendasService();
        service.cargarDatosIniciales();

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarNuevaEncomienda(scanner, service);
                case 2 -> consultarTodasLasEncomiendas(service);
                case 0 -> System.out.println("\nSaliendo del sistema de gestión de encomiendas...");
                default -> System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=======================================================");
        System.out.println("      SISTEMA LOGÍSTICO DE GESTIÓN DE ENCOMIENDAS      ");
        System.out.println("=======================================================");
        System.out.println("1. Registrar nueva encomienda");
        System.out.println("2. Consultar todas las encomiendas (Catálogo general)");
        System.out.println("0. Salir");
    }

    private static void consultarTodasLasEncomiendas(GestionEncomiendasService service) {
        System.out.println("\n--- CATÁLOGO GENERAL DE ENCOMIENDAS (ArrayList) ---");
        var lista = service.getListaGeneral();

        if (lista.isEmpty()) {
            System.out.println("No hay encomiendas registradas en el sistema.");
            return;
        }

        System.out.println("Total registradas: " + lista.size());
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-18s %-18s %-15s %-10s %-12s %-14s %-12s%n",
                "Código", "Remitente", "Destinatario", "Destino", "Peso (kg)", "Servicio", "Prioridad", "Estado");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");

        for (Encomienda e : lista) {
            System.out.printf("%-10s %-18s %-18s %-15s %-10.2f %-12s %-14s %-12s%n",
                    e.getCodigo(),
                    truncar(e.getRemitente().getNombre(), 17),
                    truncar(e.getDestinatario().getNombre(), 17),
                    e.getCiudadDestino(),
                    e.getPeso(),
                    e.getTipoServicio(),
                    e.getPrioridad().getDescripcion(),
                    e.getEstado().getDescripcion());
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
    }

    private static String truncar(String texto, int longitudMaxima) {
        if (texto == null) return "";
        if (texto.length() <= longitudMaxima) return texto;
        return texto.substring(0, longitudMaxima - 1) + ".";
    }

    private static void registrarNuevaEncomienda(Scanner scanner, GestionEncomiendasService service) {
        System.out.println("\n--- REGISTRO DE NUEVA ENCOMIENDA ---");
        System.out.print("Ingrese código de encomienda (ej. ENC-1016): ");
        String codigo = scanner.nextLine().trim();

        if (codigo.isEmpty()) {
            System.out.println("Error: El código no puede estar vacío.");
            return;
        }

        if (service.existeCodigo(codigo)) {
            System.out.println("Error: Ya existe una encomienda con el código " + codigo + " (Control de unicidad mediante Map/Set).");
            return;
        }

        System.out.println("\n[Datos del Remitente]");
        System.out.print("Documento: ");
        String docRemitente = scanner.nextLine().trim();
        System.out.print("Nombre completo: ");
        String nomRemitente = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String telRemitente = scanner.nextLine().trim();
        System.out.print("Ciudad origen: ");
        String ciuRemitente = scanner.nextLine().trim();

        System.out.println("\n[Datos del Destinatario]");
        System.out.print("Documento: ");
        String docDestinatario = scanner.nextLine().trim();
        System.out.print("Nombre completo: ");
        String nomDestinatario = scanner.nextLine().trim();
        System.out.print("Teléfono: ");
        String telDestinatario = scanner.nextLine().trim();
        System.out.print("Ciudad destino: ");
        String ciuDestinatario = scanner.nextLine().trim();

        System.out.println("\n[Detalles del Envío]");
        double peso = leerDouble(scanner, "Peso del paquete en kg: ");
        System.out.print("Tipo de servicio (Express, Estándar, Carga, Documentos): ");
        String tipoServicio = scanner.nextLine().trim();

        System.out.println("Nivel de prioridad:");
        System.out.println("  1. Urgente (Nivel 1)");
        System.out.println("  2. Alta (Nivel 2)");
        System.out.println("  3. Normal (Nivel 3)");
        System.out.println("  4. Económica (Nivel 4)");
        int opcionPrioridad = leerEntero(scanner, "Seleccione prioridad (1-4): ");

        PrioridadEncomienda prioridad = switch (opcionPrioridad) {
            case 1 -> PrioridadEncomienda.URGENTE;
            case 2 -> PrioridadEncomienda.ALTA;
            case 4 -> PrioridadEncomienda.ECONOMICA;
            default -> PrioridadEncomienda.NORMAL;
        };

        Cliente remitente = new Cliente(docRemitente, nomRemitente, telRemitente, ciuRemitente);
        Cliente destinatario = new Cliente(docDestinatario, nomDestinatario, telDestinatario, ciuDestinatario);

        Encomienda nueva = new Encomienda(codigo, remitente, destinatario, ciuDestinatario, peso, tipoServicio, prioridad);
        boolean registrado = service.registrarEncomienda(nueva);

        if (registrado) {
            System.out.println("\n>> ¡Encomienda " + codigo + " registrada exitosamente!");
            System.out.println(">> Sincronizada en: List general, Map por código, TreeSet por peso y PriorityQueue.");
        } else {
            System.out.println("\nError: No se pudo registrar la encomienda.");
        }
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número entero.");
            }
        }
    }

    private static double leerDouble(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                return Double.parseDouble(linea.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un valor numérico válido.");
            }
        }
    }
}
