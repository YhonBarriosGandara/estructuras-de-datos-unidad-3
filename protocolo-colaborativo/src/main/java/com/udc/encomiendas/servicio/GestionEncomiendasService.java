package com.udc.encomiendas.servicio;

import com.udc.encomiendas.modelo.Cliente;
import com.udc.encomiendas.modelo.Encomienda;
import com.udc.encomiendas.modelo.EstadoEncomienda;
import com.udc.encomiendas.modelo.PrioridadEncomienda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class GestionEncomiendasService {

    // 1. List: Registro maestro/histórico de todas las encomiendas
    private final List<Encomienda> listaGeneral;

    // 2. Set: Control estricto de unicidad de ciudades de cobertura habilitadas
    private final Set<String> ciudadesDestino;

    // 3. Map: Índice directo O(1) para búsqueda rápida por código de rastreo
    private final Map<String, Encomienda> mapaPorCodigo;

    // 4. TreeSet: Mantiene el catálogo automáticamente ordenado por peso
    private final TreeSet<Encomienda> catalogoOrdenadoPorPeso;

    // 5. PriorityQueue: Cola de atención según nivel de urgencia (no FIFO plano)
    private final PriorityQueue<Encomienda> colaPrioridad;

    public GestionEncomiendasService() {
        this.listaGeneral = new ArrayList<>();
        this.ciudadesDestino = new HashSet<>();
        this.mapaPorCodigo = new HashMap<>();
        this.catalogoOrdenadoPorPeso = new TreeSet<>();

        // Comparator: primero evalúa nivel de prioridad (1 es más urgente que 4)
        // y como desempate evalúa fecha de registro (FIFO ante misma prioridad)
        Comparator<Encomienda> comparadorPrioridad = Comparator
                .comparingInt((Encomienda e) -> e.getPrioridad().getNivel())
                .thenComparing(Encomienda::getFechaRegistro);

        this.colaPrioridad = new PriorityQueue<>(comparadorPrioridad);
    }

    public boolean registrarCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            return false;
        }
        return ciudadesDestino.add(ciudad.trim().toUpperCase());
    }

    public boolean registrarEncomienda(Encomienda encomienda) {
        if (encomienda == null || encomienda.getCodigo() == null) {
            return false;
        }

        String codigo = encomienda.getCodigo().trim();

        // Control de unicidad mediante Map: no se permiten códigos duplicados
        if (mapaPorCodigo.containsKey(codigo)) {
            return false;
        }

        // 1. Agregar a la lista general
        listaGeneral.add(encomienda);

        // 2. Agregar ciudad al Set de cobertura
        if (encomienda.getCiudadDestino() != null) {
            ciudadesDestino.add(encomienda.getCiudadDestino().trim().toUpperCase());
        }

        // 3. Asociar en el índice Map
        mapaPorCodigo.put(codigo, encomienda);

        // 4. Agregar a la estructura ordenada (TreeSet)
        catalogoOrdenadoPorPeso.add(encomienda);

        // 5. Encolar en la cola de prioridad
        colaPrioridad.offer(encomienda);

        return true;
    }

    public Encomienda buscarPorCodigo(String codigo) {
        if (codigo == null)
            return null;
        return mapaPorCodigo.get(codigo.trim());
    }

    public boolean existeCodigo(String codigo) {
        if (codigo == null)
            return false;
        return mapaPorCodigo.containsKey(codigo.trim());
    }

    public List<Encomienda> getListaGeneral() {
        return Collections.unmodifiableList(listaGeneral);
    }

    public Set<String> getCiudadesDestino() {
        return Collections.unmodifiableSet(ciudadesDestino);
    }

    public Set<Encomienda> getCatalogoOrdenadoPorPeso() {
        return Collections.unmodifiableSet(catalogoOrdenadoPorPeso);
    }

    public Encomienda consultarProximoDespacho() {
        return colaPrioridad.peek();
    }

    public Encomienda despacharSiguiente() {
        Encomienda proxima = colaPrioridad.poll();
        if (proxima != null) {
            proxima.setEstado(EstadoEncomienda.EN_TRANSITO);
        }
        return proxima;
    }

    public boolean cancelarEncomienda(String codigo) {
        if (codigo == null)
            return false;
        Encomienda encomienda = mapaPorCodigo.get(codigo.trim());
        if (encomienda == null) {
            return false;
        }

        encomienda.setEstado(EstadoEncomienda.CANCELADA);
        colaPrioridad.remove(encomienda);
        catalogoOrdenadoPorPeso.remove(encomienda);
        return true;
    }

    public boolean eliminarDefinitivamente(String codigo) {
        if (codigo == null)
            return false;
        Encomienda encomienda = mapaPorCodigo.remove(codigo.trim());
        if (encomienda == null) {
            return false;
        }

        listaGeneral.remove(encomienda);
        catalogoOrdenadoPorPeso.remove(encomienda);
        colaPrioridad.remove(encomienda);
        return true;
    }

    public boolean modificarDestino(String codigo, String nuevoDestino) {
        if (codigo == null || nuevoDestino == null || nuevoDestino.trim().isEmpty()) {
            return false;
        }

        Encomienda encomienda = mapaPorCodigo.get(codigo.trim());
        if (encomienda == null) {
            return false;
        }

        String ciudadFormateada = nuevoDestino.trim().toUpperCase();
        encomienda.setCiudadDestino(ciudadFormateada);
        ciudadesDestino.add(ciudadFormateada);
        return true;
    }

    public int getCantidadRegistradas() {
        return listaGeneral.size();
    }

    public int getCantidadPendientesDespacho() {
        return colaPrioridad.size();
    }

    public Map<String, Integer> obtenerResumenColecciones() {
        Map<String, Integer> resumen = new HashMap<>();
        resumen.put("listaGeneral", listaGeneral.size());
        resumen.put("mapaPorCodigo", mapaPorCodigo.size());
        resumen.put("catalogoOrdenadoPorPeso", catalogoOrdenadoPorPeso.size());
        resumen.put("colaPrioridad", colaPrioridad.size());
        resumen.put("ciudadesDestino", ciudadesDestino.size());
        return resumen;
    }
}
