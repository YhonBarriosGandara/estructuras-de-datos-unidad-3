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

    public void cargarDatosIniciales() {
        java.time.LocalDateTime base = java.time.LocalDateTime.now();

        Cliente c1 = new Cliente("1047111001", "Carlos Mendoza", "3001234567", "Cartagena");
        Cliente c2 = new Cliente("1047111002", "María Gómez", "3019876543", "Barranquilla");
        Cliente c3 = new Cliente("1047111003", "Andrés Ruiz", "3104567890", "Medellín");
        Cliente c4 = new Cliente("1047111004", "Laura Torres", "3156789012", "Bogotá");
        Cliente c5 = new Cliente("1047111005", "David Meza", "3203456789", "Cali");
        Cliente c6 = new Cliente("1047111006", "Valentina Ríos", "3008765432", "Santa Marta");
        Cliente c7 = new Cliente("1047111007", "Jorge Castro", "3127890123", "Bucaramanga");
        Cliente c8 = new Cliente("1047111008", "Diana Herrera", "3182345678", "Cartagena");

        registrarEncomienda(new Encomienda("ENC-1001", c1, c2, "Barranquilla", 2.50, "Express", PrioridadEncomienda.URGENTE, base.minusMinutes(90)));
        registrarEncomienda(new Encomienda("ENC-1002", c3, c4, "Bogotá", 8.20, "Estándar", PrioridadEncomienda.NORMAL, base.minusMinutes(85)));
        registrarEncomienda(new Encomienda("ENC-1003", c5, c1, "Cartagena", 1.20, "Express", PrioridadEncomienda.ALTA, base.minusMinutes(80)));
        registrarEncomienda(new Encomienda("ENC-1004", c2, c6, "Santa Marta", 15.00, "Carga", PrioridadEncomienda.ECONOMICA, base.minusMinutes(75)));
        registrarEncomienda(new Encomienda("ENC-1005", c4, c3, "Medellín", 3.80, "Express", PrioridadEncomienda.URGENTE, base.minusMinutes(70)));
        registrarEncomienda(new Encomienda("ENC-1006", c6, c5, "Cali", 5.50, "Estándar", PrioridadEncomienda.NORMAL, base.minusMinutes(65)));
        registrarEncomienda(new Encomienda("ENC-1007", c7, c8, "Cartagena", 12.30, "Carga", PrioridadEncomienda.ALTA, base.minusMinutes(60)));
        registrarEncomienda(new Encomienda("ENC-1008", c8, c7, "Bucaramanga", 0.80, "Documentos", PrioridadEncomienda.URGENTE, base.minusMinutes(55)));
        registrarEncomienda(new Encomienda("ENC-1009", c1, c4, "Bogotá", 22.00, "Carga", PrioridadEncomienda.ECONOMICA, base.minusMinutes(50)));
        registrarEncomienda(new Encomienda("ENC-1010", c3, c2, "Barranquilla", 4.10, "Estándar", PrioridadEncomienda.NORMAL, base.minusMinutes(45)));
        registrarEncomienda(new Encomienda("ENC-1011", c5, c6, "Santa Marta", 6.70, "Express", PrioridadEncomienda.ALTA, base.minusMinutes(40)));
        registrarEncomienda(new Encomienda("ENC-1012", c2, c7, "Bucaramanga", 1.90, "Documentos", PrioridadEncomienda.NORMAL, base.minusMinutes(35)));
        registrarEncomienda(new Encomienda("ENC-1013", c4, c5, "Cali", 18.50, "Carga", PrioridadEncomienda.ECONOMICA, base.minusMinutes(30)));
        registrarEncomienda(new Encomienda("ENC-1014", c6, c1, "Cartagena", 7.40, "Estándar", PrioridadEncomienda.ALTA, base.minusMinutes(25)));
        registrarEncomienda(new Encomienda("ENC-1015", c7, c3, "Medellín", 3.20, "Express", PrioridadEncomienda.NORMAL, base.minusMinutes(20)));
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
