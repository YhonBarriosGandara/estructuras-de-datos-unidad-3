package com.udc.encomiendas.modelo;

public enum EstadoEncomienda {
    REGISTRADA("Registrada"),
    EN_TRANSITO("En Tránsito"),
    ENTREGADA("Entregada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoEncomienda(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}

