package com.udc.restaurante.modelo;

public enum EstadoPedido {
    PENDIENTE("Pendiente en cocina"),
    EN_PREPARACION("En preparacion"),
    PROCESADO("Entregado / Facturado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
