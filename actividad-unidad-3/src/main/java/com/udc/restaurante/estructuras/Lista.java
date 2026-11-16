package com.udc.restaurante.estructuras;

/**
 * Estructura de datos Lista doblemente enlazada personalizada con punteros a cabeza y cola.
 * Implementa todas las operaciones requeridas por la guia docente para gestion integral.
 */
public class Lista {
    private Nodo cabeza;
    private Nodo cola;
    private int tamanio;

    public Lista() {
        this.cabeza = null;
        this.cola = null;
        this.tamanio = 0;
    }

    public void agregar(Object dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo;
        }
        tamanio++;
    }

    public void agregarAlInicio(Object dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            nuevo.setSiguiente(cabeza);
            cabeza.setAnterior(nuevo);
            cabeza = nuevo;
        }
        tamanio++;
    }

    public void agregarEnPosicion(int indice, Object dato) {
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        if (indice == 0) {
            agregarAlInicio(dato);
            return;
        }
        if (indice == tamanio) {
            agregar(dato);
            return;
        }
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        Nodo previo = actual.getAnterior();
        Nodo nuevo = new Nodo(dato, actual, previo);
        previo.setSiguiente(nuevo);
        actual.setAnterior(nuevo);
        tamanio++;
    }

    public Object eliminarPrimero() {
        if (cabeza == null) {
            return null;
        }
        Object dato = cabeza.getDato();
        if (cabeza == cola) {
            cabeza = null;
            cola = null;
        } else {
            cabeza = cabeza.getSiguiente();
            cabeza.setAnterior(null);
        }
        tamanio--;
        return dato;
    }

    public Object eliminarUltimo() {
        if (cola == null) {
            return null;
        }
        Object dato = cola.getDato();
        if (cabeza == cola) {
            cabeza = null;
            cola = null;
        } else {
            cola = cola.getAnterior();
            cola.setSiguiente(null);
        }
        tamanio--;
        return dato;
    }

    public Object eliminarEnPosicion(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }
        if (indice == 0) return eliminarPrimero();
        if (indice == tamanio - 1) return eliminarUltimo();

        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        Object dato = actual.getDato();
        actual.getAnterior().setSiguiente(actual.getSiguiente());
        actual.getSiguiente().setAnterior(actual.getAnterior());
        tamanio--;
        return dato;
    }

    public Object buscarDato(int indice) {
        if (indice < 0 || indice >= tamanio) {
            return null;
        }
        Nodo actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public int buscarDato(Object dato) {
        if (dato == null) return -1;
        Nodo actual = cabeza;
        int index = 0;
        while (actual != null) {
            if (dato.equals(actual.getDato())) {
                return index;
            }
            actual = actual.getSiguiente();
            index++;
        }
        return -1;
    }

    public boolean contiene(Object dato) {
        return buscarDato(dato) != -1;
    }

    public int cuentaElementos() {
        return tamanio;
    }

    public void limpiar() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    public void mostrarAdelante() {
        if (cabeza == null) {
            System.out.println("  (La lista esta vacia)");
            return;
        }
        Nodo actual = cabeza;
        int i = 0;
        while (actual != null) {
            System.out.println("  [" + i + "] " + actual.getDato());
            actual = actual.getSiguiente();
            i++;
        }
    }

    public void mostrarAtras() {
        if (cola == null) {
            System.out.println("  (La lista esta vacia)");
            return;
        }
        Nodo actual = cola;
        int i = tamanio - 1;
        while (actual != null) {
            System.out.println("  [" + i + "] " + actual.getDato());
            actual = actual.getAnterior();
            i--;
        }
    }

    public Nodo getCabeza() {
        return cabeza;
    }
}
