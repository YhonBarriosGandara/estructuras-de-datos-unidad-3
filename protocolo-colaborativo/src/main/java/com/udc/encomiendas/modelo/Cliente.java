package com.udc.encomiendas.modelo;

import java.util.Objects;

public class Cliente {

    private String documento;
    private String nombre;
    private String telefono;
    private String ciudad;

    public Cliente(String documento, String nombre, String telefono, String ciudad) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
        this.ciudad = ciudad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(documento, cliente.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return nombre + " (Doc: " + documento + ", Ciudad: " + ciudad + ")";
    }
}

