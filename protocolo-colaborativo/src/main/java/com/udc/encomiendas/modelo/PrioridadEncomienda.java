package com.udc.encomiendas.modelo;

public enum PrioridadEncomienda {
    URGENTE(1, "Urgente"),
    ALTA(2, "Alta"),
    NORMAL(3, "Normal"),
    ECONOMICA(4, "Económica");

    private final int nivel;
    private final String descripcion;

    PrioridadEncomienda(int nivel, String descripcion) {
        this.nivel = nivel;
        this.descripcion = descripcion;
    }

    public int getNivel() {
        return nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion + " (Nivel " + nivel + ")";
    }
}

