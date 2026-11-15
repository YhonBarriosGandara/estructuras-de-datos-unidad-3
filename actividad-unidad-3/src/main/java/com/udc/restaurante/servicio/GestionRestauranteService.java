package com.udc.restaurante.servicio;

import com.udc.restaurante.estructuras.Cola;
import com.udc.restaurante.estructuras.Lista;
import com.udc.restaurante.estructuras.Nodo;
import com.udc.restaurante.estructuras.Pila;
import com.udc.restaurante.modelo.EstadoPedido;
import com.udc.restaurante.modelo.Pedido;

/**
 * Servicio orquestador del restaurante.
 * Integra de forma sincronizada y coordinada las tres estructuras manuales:
 * - Lista listaGeneral: Catalogo maestro y registro total de pedidos.
 * - Cola colaPendientes: Cola de atencion FIFO para pedidos en espera de cocina.
 * - Pila historialProcesados: Pila LIFO de pedidos entregados que permite deshacer operaciones.
 */
public class GestionRestauranteService {
    private final Lista listaGeneral;
    private final Cola colaPendientes;
    private final Pila historialProcesados;

    public GestionRestauranteService() {
        this.listaGeneral = new Lista();
        this.colaPendientes = new Cola();
        this.historialProcesados = new Pila();
    }

    public boolean registrarPedido(Pedido pedido) {
        if (pedido == null || pedido.getNumeroPedido() == null || pedido.getNumeroPedido().isEmpty()) {
            return false;
        }

        // Validar unicidad de numero de pedido en la lista general
        if (buscarPedidoPorCodigo(pedido.getNumeroPedido()) != null) {
            return false; // Codigo duplicado
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        // Regla obligatoria: agregar a lista general y encolar a pendientes
        listaGeneral.agregar(pedido);
        colaPendientes.encolar(pedido);
        return true;
    }

    public Pedido procesarSiguientePedido() {
        if (colaPendientes.esVacia()) {
            return null;
        }
        // Regla obligatoria: desencolar de pendientes y apilar en historial
        Pedido procesado = (Pedido) colaPendientes.desencolar();
        procesado.setEstado(EstadoPedido.PROCESADO);
        historialProcesados.apilar(procesado);
        return procesado;
    }

    /**
     * Cancelar elemento pendiente.
     * REGLA OBLIGATORIA DE LA GUIA:
     * "Debe resolverse usando una cola auxiliar. No se permite eliminar directamente desde el centro de la cola."
     *
     * @param numeroPedido Identificador del pedido a cancelar.
     * @return El pedido cancelado o null si no se encontro en la cola de pendientes.
     */
    public Pedido cancelarPedidoPendiente(String numeroPedido) {
        if (numeroPedido == null || colaPendientes.esVacia()) {
            return null;
        }

        String codigoObjetivo = numeroPedido.trim().toUpperCase();
        Cola colaAuxiliar = new Cola();
        Pedido cancelado = null;

        // Fase 1: Desencolar todo elemento por elemento hacia la cola auxiliar
        while (!colaPendientes.esVacia()) {
            Pedido actual = (Pedido) colaPendientes.desencolar();
            if (cancelado == null && actual.getNumeroPedido().equalsIgnoreCase(codigoObjetivo)) {
                // Elemento localizado: se extrae, se cambia su estado y NO se agrega a la auxiliar
                actual.setEstado(EstadoPedido.CANCELADO);
                cancelado = actual;
            } else {
                colaAuxiliar.encolar(actual);
            }
        }

        // Fase 2: Restaurar los elementos de la cola auxiliar de vuelta a la cola de pendientes
        while (!colaAuxiliar.esVacia()) {
            colaPendientes.encolar(colaAuxiliar.desencolar());
        }

        return cancelado;
    }

    /**
     * Deshacer ultimo procesamiento.
     * REGLA OBLIGATORIA DE LA GUIA:
     * "Al deshacer: Elemento ultimo = (Elemento) historialProcesados.desapilar(); colaPendientes.encolar(ultimo);"
     *
     * @return El pedido reincorporado a la cola o null si la pila esta vacia.
     */
    public Pedido deshacerUltimoProcesamiento() {
        if (historialProcesados.esVacia()) {
            return null;
        }

        Pedido ultimo = (Pedido) historialProcesados.desapilar();
        ultimo.setEstado(EstadoPedido.PENDIENTE);
        colaPendientes.encolar(ultimo);
        return ultimo;
    }

    public Pedido buscarPedidoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return null;
        String codigoLimpio = codigo.trim().toUpperCase();

        Nodo actual = listaGeneral.getCabeza();
        while (actual != null) {
            Pedido p = (Pedido) actual.getDato();
            if (p != null && p.getNumeroPedido().equalsIgnoreCase(codigoLimpio)) {
                return p;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    public Lista getListaGeneral() {
        return listaGeneral;
    }

    public Cola getColaPendientes() {
        return colaPendientes;
    }

    public Pila getHistorialProcesados() {
        return historialProcesados;
    }
}
