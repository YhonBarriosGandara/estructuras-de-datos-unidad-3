package com.udc.restaurante.estructuras;

/**
 * Nodo generico enlazado para construccion de estructuras de datos lineales.
 * Cuenta con referencias bidireccionales (siguiente y anterior) para soportar
 * listas doblemente enlazadas, colas FIFO y pilas LIFO.
 */
public class Nodo {
    private Object dato;
    private Nodo siguiente;
    private Nodo anterior;

    public Nodo(Object dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }

    public Nodo(Object dato, Nodo siguiente, Nodo anterior) {
        this.dato = dato;
        this.siguiente = siguiente;
        this.anterior = anterior;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }

    @Override
    public String toString() {
        return dato != null ? dato.toString() : "null";
    }
}
