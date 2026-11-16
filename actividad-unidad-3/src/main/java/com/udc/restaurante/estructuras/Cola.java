package com.udc.restaurante.estructuras;

/**
 * Estructura de datos Cola enlazada bajo la disciplina FIFO (First In, First Out).
 * Usada para gestionar los pedidos en espera de preparacion en cocina.
 */
public class Cola {
    private Nodo frente;
    private Nodo fin;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public void encolar(Object dato) {
        Nodo nuevo = new Nodo(dato);
        if (esVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.setSiguiente(nuevo);
            nuevo.setAnterior(fin);
            fin = nuevo;
        }
        tamanio++;
    }

    public Object desencolar() {
        if (esVacia()) {
            return null;
        }
        Object dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) {
            fin = null;
        } else {
            frente.setAnterior(null);
        }
        tamanio--;
        return dato;
    }

    public Object peek() {
        if (esVacia()) {
            return null;
        }
        return frente.getDato();
    }

    public int tamanio() {
        return tamanio;
    }

    public boolean esVacia() {
        return tamanio == 0;
    }

    public boolean contiene(Object dato) {
        if (dato == null || esVacia()) return false;
        Nodo actual = frente;
        while (actual != null) {
            if (dato.equals(actual.getDato())) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void limpiar() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    public void mostrar() {
        if (esVacia()) {
            System.out.println("  (La cola de pendientes esta vacia)");
            return;
        }
        Nodo actual = frente;
        int posicion = 1;
        while (actual != null) {
            System.out.println("  Turno #" + posicion + " en espera: " + actual.getDato());
            actual = actual.getSiguiente();
            posicion++;
        }
    }

    public Nodo getFrente() {
        return frente;
    }
}
