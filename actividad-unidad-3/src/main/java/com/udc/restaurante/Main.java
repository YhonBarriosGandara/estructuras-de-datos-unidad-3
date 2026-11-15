package com.udc.restaurante;

import com.udc.restaurante.modelo.Pedido;
import com.udc.restaurante.servicio.GestionRestauranteService;

import java.util.Scanner;

/**
 * Clase principal y menu de ejecucion interactivo en consola.
 * Implementa de manera estricta las 10 opciones requeridas por la guia oficial:
 * 1. Registrar elemento
 * 2. Ver todos los elementos registrados
 * 3. Ver elementos pendientes
 * 4. Procesar siguiente elemento
 * 5. Ver historial de elementos procesados
 * 6. Buscar elemento por codigo
 * 7. Cancelar elemento pendiente
 * 8. Deshacer ultimo procesamiento
 * 9. Ver cantidad de elementos
 * 10. Salir
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestionRestauranteService service = new GestionRestauranteService();
        service.cargarDatosIniciales();

        int opcion = -1;
        do {
            mostrarEncabezadoMenu();
            System.out.print("  Seleccione una opcion [1-10]: ");
            String entrada = scanner.nextLine().trim();

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("\n  [ERROR] Entrada invalida. Por favor digite un numero del 1 al 10.");
                esperarEnter(scanner);
                continue;
            }

            System.out.println();
            switch (opcion) {
                case 1 -> registrarElemento(scanner, service);
                case 2 -> verTodosLosRegistrados(service);
                case 3 -> verElementosPendientes(service);
                case 4 -> procesarSiguiente(service);
                case 5 -> verHistorialProcesados(service);
                case 6 -> buscarPorCodigo(scanner, service);
                case 7 -> cancelarElementoPendiente(scanner, service);
                case 8 -> deshacerUltimoProcesamiento(service);
                case 9 -> verCantidadElementos(service);
                case 10 -> System.out.println("  Saliendo del Sistema de Gestion de Restaurante. ¡Hasta pronto!");
                default -> System.out.println("  [ADVERTENCIA] Opcion fuera de rango. Debe ser entre 1 y 10.");
            }

            if (opcion != 10) {
                esperarEnter(scanner);
            }
        } while (opcion != 10);
    }

    private static void mostrarEncabezadoMenu() {
        System.out.println("\n================================================================================");
        System.out.println("     SISTEMA DE GESTION DE RESTAURANTE — CASO 4 (LISTA, COLA Y PILA)            ");
        System.out.println("================================================================================");
        System.out.println("  1. Registrar elemento (Nuevo Pedido -> Lista y Cola)");
        System.out.println("  2. Ver todos los elementos registrados (Lista General)");
        System.out.println("  3. Ver elementos pendientes (Cola de Cocina FIFO)");
        System.out.println("  4. Procesar siguiente elemento (Desencolar -> Apilar en Historial)");
        System.out.println("  5. Ver historial de elementos procesados (Pila LIFO)");
        System.out.println("  6. Buscar elemento por codigo (Consulta en Lista)");
        System.out.println("  7. Cancelar elemento pendiente (Algoritmo con Cola Auxiliar)");
        System.out.println("  8. Deshacer ultimo procesamiento (Desapilar -> Re-encolar)");
        System.out.println("  9. Ver cantidad de elementos en cada estructura");
        System.out.println("  10. Salir");
        System.out.println("================================================================================");
    }

    private static void registrarElemento(Scanner scanner, GestionRestauranteService service) {
        System.out.println("--- 1. REGISTRAR NUEVO PEDIDO ---");
        System.out.print("  Numero de Pedido (ej. PED-200): ");
        String numero = scanner.nextLine().trim();
        if (numero.isEmpty()) {
            System.out.println("  [ERROR] El numero de pedido no puede estar vacio.");
            return;
        }

        if (service.buscarPedidoPorCodigo(numero) != null) {
            System.out.println("  [ERROR] Ya existe un pedido registrado con el identificador: " + numero);
            return;
        }

        System.out.print("  Cliente / Mesa (ej. Mesa 5 - Laura Gomez): ");
        String cliente = scanner.nextLine().trim();

        System.out.print("  Descripcion de platos (ej. Salmon al Ajillo + Bebida): ");
        String descripcion = scanner.nextLine().trim();

        System.out.print("  Valor Total ($): ");
        double total = 0.0;
        try {
            total = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Valor total invalido. Se asignara $0.0");
        }

        Pedido nuevo = new Pedido(numero, cliente, descripcion, total);
        boolean exito = service.registrarPedido(nuevo);
        if (exito) {
            System.out.println("\n  [EXITO] Pedido registrado correctamente.");
            System.out.println("  -> Agregado a: Lista General");
            System.out.println("  -> Encolado en: Cola de Pendientes (Turno #" + service.getColaPendientes().tamanio() + ")");
        } else {
            System.out.println("  [ERROR] No se pudo registrar el pedido.");
        }
    }

    private static void verTodosLosRegistrados(GestionRestauranteService service) {
        System.out.println("--- 2. LISTA GENERAL DE PEDIDOS REGISTRADOS (TOTAL: " + service.getListaGeneral().cuentaElementos() + ") ---");
        service.getListaGeneral().mostrarAdelante();
    }

    private static void verElementosPendientes(GestionRestauranteService service) {
        System.out.println("--- 3. ELEMENTOS PENDIENTES POR PROCESAR EN COCINA (COLA FIFO: " + service.getColaPendientes().tamanio() + ") ---");
        service.getColaPendientes().mostrar();
    }

    private static void procesarSiguiente(GestionRestauranteService service) {
        System.out.println("--- 4. PROCESAR SIGUIENTE ELEMENTO PENDIENTE ---");
        Pedido procesado = service.procesarSiguientePedido();
        if (procesado != null) {
            System.out.println("  [PROCESADO CON EXITO]");
            System.out.println("  -> Elemento desencolado de pendientes:");
            System.out.println("     " + procesado);
            System.out.println("  -> Elemento apilado en la cima de la Pila de Historial.");
        } else {
            System.out.println("  [AVISO] No hay pedidos pendientes en la cola para procesar.");
        }
    }

    private static void verHistorialProcesados(GestionRestauranteService service) {
        System.out.println("--- 5. HISTORIAL DE ELEMENTOS PROCESADOS (PILA LIFO: " + service.getHistorialProcesados().tamanio() + ") ---");
        service.getHistorialProcesados().mostrar();
    }

    private static void buscarPorCodigo(Scanner scanner, GestionRestauranteService service) {
        System.out.println("--- 6. BUSCAR ELEMENTO POR CODIGO ---");
        System.out.print("  Ingrese codigo o numero de pedido a buscar: ");
        String codigo = scanner.nextLine().trim();
        Pedido encontrado = service.buscarPedidoPorCodigo(codigo);
        if (encontrado != null) {
            System.out.println("  [ENCONTRADO EN LISTA GENERAL]:");
            System.out.println("  " + encontrado);
        } else {
            System.out.println("  [NO ENCONTRADO] No existe ningun pedido registrado con codigo: " + codigo);
        }
    }

    private static void cancelarElementoPendiente(Scanner scanner, GestionRestauranteService service) {
        System.out.println("--- 7. CANCELAR ELEMENTO PENDIENTE (ALGORITMO COLA AUXILIAR) ---");
        System.out.print("  Ingrese numero de pedido a cancelar: ");
        String codigo = scanner.nextLine().trim();
        Pedido cancelado = service.cancelarPedidoPendiente(codigo);
        if (cancelado != null) {
            System.out.println("  [EXITO] Pedido cancelado mediante recorrido con Cola Auxiliar.");
            System.out.println("  -> " + cancelado);
            System.out.println("  -> Se preservo el orden FIFO de los demas pedidos pendientes.");
        } else {
            System.out.println("  [AVISO] El pedido " + codigo + " no se encuentra en la cola de pendientes.");
        }
    }

    private static void deshacerUltimoProcesamiento(GestionRestauranteService service) {
        System.out.println("--- 8. DESHACER ULTIMO PROCESAMIENTO ---");
        Pedido deshecho = service.deshacerUltimoProcesamiento();
        if (deshecho != null) {
            System.out.println("  [EXITO] Operacion deshecha satisfactoriamente.");
            System.out.println("  -> Elemento desapilado de la Pila de Historial:");
            System.out.println("     " + deshecho);
            System.out.println("  -> Su estado fue revertido a PENDIENTE y re-encolado en Cocina.");
        } else {
            System.out.println("  [AVISO] El historial de procesados esta vacio. No hay operaciones para deshacer.");
        }
    }

    private static void verCantidadElementos(GestionRestauranteService service) {
        System.out.println("--- 9. BALANCE Y CANTIDAD DE ELEMENTOS EN CADA ESTRUCTURA ---");
        System.out.println("  • Lista General (Registro historico total): " + service.getListaGeneral().cuentaElementos() + " pedidos");
        System.out.println("  • Cola de Pendientes (En espera FIFO):       " + service.getColaPendientes().tamanio() + " pedidos");
        System.out.println("  • Pila de Historial (Procesados LIFO):       " + service.getHistorialProcesados().tamanio() + " pedidos");
    }

    private static void esperarEnter(Scanner scanner) {
        System.out.print("\n  Presione ENTER para continuar...");
        scanner.nextLine();
    }
}
