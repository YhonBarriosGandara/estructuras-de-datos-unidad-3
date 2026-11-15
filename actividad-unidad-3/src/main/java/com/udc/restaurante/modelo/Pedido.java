package com.udc.restaurante.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entidad principal para el Caso 4: Restaurante.
 * Cumple con los requerimientos de la guia:
 * - Minimo 4 atributos de negocio.
 * - Constructor, getters, setters.
 * - toString() descriptivo.
 * - equals() comparando por identificador principal (numeroPedido).
 */
public class Pedido {
    private String numeroPedido; // Identificador principal (ej. PED-101)
    private String cliente;      // Mesa o nombre del comensal
    private String descripcion;  // Platos y bebidas solicitadas
    private double total;        // Valor total del pedido
    private EstadoPedido estado; // Estado en el flujo de atencion
    private LocalDateTime fechaHora;

    public Pedido(String numeroPedido, String cliente, String descripcion, double total) {
        this.numeroPedido = numeroPedido != null ? numeroPedido.trim().toUpperCase() : "";
        this.cliente = cliente != null ? cliente.trim() : "";
        this.descripcion = descripcion != null ? descripcion.trim() : "";
        this.total = total;
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaHora = LocalDateTime.now();
    }

    public Pedido(String numeroPedido, String cliente, String descripcion, double total, EstadoPedido estado, LocalDateTime fechaHora) {
        this.numeroPedido = numeroPedido != null ? numeroPedido.trim().toUpperCase() : "";
        this.cliente = cliente != null ? cliente.trim() : "";
        this.descripcion = descripcion != null ? descripcion.trim() : "";
        this.total = total;
        this.estado = estado != null ? estado : EstadoPedido.PENDIENTE;
        this.fechaHora = fechaHora != null ? fechaHora : LocalDateTime.now();
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido != null ? numeroPedido.trim().toUpperCase() : "";
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(numeroPedido, pedido.numeroPedido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroPedido);
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaStr = fechaHora != null ? fechaHora.format(fmt) : "--:--:--";
        return String.format("[%s] Cliente: %-18s | Platos: %-35s | Total: $%8.2f | Estado: %-22s (%s)",
                numeroPedido, cliente, descripcion, total, estado.getDescripcion(), horaStr);
    }
}
