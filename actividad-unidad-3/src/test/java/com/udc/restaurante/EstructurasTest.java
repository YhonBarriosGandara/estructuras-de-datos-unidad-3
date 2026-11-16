package com.udc.restaurante;

import com.udc.restaurante.estructuras.Cola;
import com.udc.restaurante.estructuras.Lista;
import com.udc.restaurante.estructuras.Pila;
import com.udc.restaurante.modelo.EstadoPedido;
import com.udc.restaurante.modelo.Pedido;
import com.udc.restaurante.servicio.GestionRestauranteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EstructurasTest {
    private GestionRestauranteService service;

    @BeforeEach
    void setUp() {
        service = new GestionRestauranteService();
    }

    @Test
    void testRegistroYUnicidad() {
        Pedido p1 = new Pedido("PED-001", "Cliente 1", "Plato 1", 25000.0);
        Pedido p1Dup = new Pedido("PED-001", "Cliente 1 Duplicado", "Plato 1", 25000.0);

        assertTrue(service.registrarPedido(p1));
        assertFalse(service.registrarPedido(p1Dup)); // Rechaza duplicado
        assertEquals(1, service.getListaGeneral().cuentaElementos());
        assertEquals(1, service.getColaPendientes().tamanio());
    }

    @Test
    void testProcesamientoFifoYApiladoLifo() {
        Pedido p1 = new Pedido("PED-001", "Cliente 1", "Plato 1", 25000.0);
        Pedido p2 = new Pedido("PED-002", "Cliente 2", "Plato 2", 30000.0);
        service.registrarPedido(p1);
        service.registrarPedido(p2);

        Pedido procesado1 = service.procesarSiguientePedido();
        assertEquals("PED-001", procesado1.getNumeroPedido()); // FIFO: p1 llego primero
        assertEquals(EstadoPedido.PROCESADO, procesado1.getEstado());
        assertEquals(1, service.getColaPendientes().tamanio());
        assertEquals(1, service.getHistorialProcesados().tamanio());

        // La cima de la pila debe ser p1
        assertEquals("PED-001", ((Pedido) service.getHistorialProcesados().peek()).getNumeroPedido());
    }

    @Test
    void testDeshacerUltimoProcesamiento() {
        Pedido p1 = new Pedido("PED-001", "Cliente 1", "Plato 1", 25000.0);
        service.registrarPedido(p1);
        service.procesarSiguientePedido();

        assertEquals(0, service.getColaPendientes().tamanio());
        assertEquals(1, service.getHistorialProcesados().tamanio());

        Pedido deshecho = service.deshacerUltimoProcesamiento();
        assertNotNull(deshecho);
        assertEquals("PED-001", deshecho.getNumeroPedido());
        assertEquals(EstadoPedido.PENDIENTE, deshecho.getEstado());
        assertEquals(1, service.getColaPendientes().tamanio());
        assertEquals(0, service.getHistorialProcesados().tamanio());
    }

    @Test
    void testCancelacionConColaAuxiliar() {
        Pedido p1 = new Pedido("PED-001", "Cliente 1", "Plato 1", 10.0);
        Pedido p2 = new Pedido("PED-002", "Cliente 2", "Plato 2", 20.0);
        Pedido p3 = new Pedido("PED-003", "Cliente 3", "Plato 3", 30.0);

        service.registrarPedido(p1);
        service.registrarPedido(p2);
        service.registrarPedido(p3);

        // Cancelar el del medio (p2)
        Pedido cancelado = service.cancelarPedidoPendiente("PED-002");
        assertNotNull(cancelado);
        assertEquals("PED-002", cancelado.getNumeroPedido());
        assertEquals(EstadoPedido.CANCELADO, cancelado.getEstado());

        // Cola de pendientes debe conservar p1 y luego p3 en estricto orden
        assertEquals(2, service.getColaPendientes().tamanio());
        Pedido primerRestante = (Pedido) service.getColaPendientes().desencolar();
        Pedido segundoRestante = (Pedido) service.getColaPendientes().desencolar();
        assertEquals("PED-001", primerRestante.getNumeroPedido());
        assertEquals("PED-003", segundoRestante.getNumeroPedido());
    }
}
