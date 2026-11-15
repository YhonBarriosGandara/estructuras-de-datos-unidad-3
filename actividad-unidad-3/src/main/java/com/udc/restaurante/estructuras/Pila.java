package com.udc.restaurante.estructuras;

/**
 * Estructura de datos Pila enlazada bajo la disciplina LIFO (Last In, First Out).
 * Usada para registrar el historial cronologico inverso de pedidos procesados y
 * posibilitar la operacion de deshacer el ultimo procesamiento.
 */
public class Pila {
    private Nodo cima;
    private int tamanio;

    public Pila() {
        this.cima = null;
        this.tamanio = 0;
    }

    public void apilar(Object dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.setSiguiente(cima);
        if (cima != null) {
            cima.setAnterior(nuevo);
        }
        cima = nuevo;
        tamanio++;
    }

    public Object desapilar() {
        if (esVacia()) {
            return null;
        }
        Object dato = cima.getDato();
        cima = cima.getSiguiente();
        if (cima != null) {
            cima.setAnterior(null);
        }
        tamanio--;
        return dato;
    }

    public Object peek() {
        if (esVacia()) {
            return null;
        }
        return cima.getDato();
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean esVacia() {
        return tamanio == 0;
    }

    public boolean contiene(Object dato) {
        if (dato == null || esVacia()) return false;
        Nodo actual = cima;
        while (actual != null) {
            if (dato.equals(actual.getDato())) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void limpiar() {
        cima = null;
        tamanio = 0;
    }

    public void mostrar() {
        if (esVacia()) {
            System.out.println("  (El historial de procesados esta vacio)");
            return;
        }
        Nodo actual = cima;
        int pos = 1;
        while (actual != null) {
            System.out.println("  [LIFO #" + pos + " - Ultimo procesado]: " + actual.getDato());
            actual = actual.getSiguiente();
            pos++;
        }
    }

    public int buscar(Object dato) {
        if (dato == null || esVacia()) return -1;
        Nodo actual = cima;
        int distancia = 1;
        while (actual != null) {
            if (dato.equals(actual.getDato())) {
                return distancia; // Distancia 1-based desde la cima
            }
            actual = actual.getSiguiente();
            distancia++;
        }
        return -1;
    }

    public Nodo getCima() {
        return cima;
    }
}
