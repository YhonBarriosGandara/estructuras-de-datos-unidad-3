# Sistema de Gestión de Restaurante — Caso 4
## Actividad Unidad 3: Estructuras de Datos Lineales Dinámicas (Lista, Cola y Pila con Nodos)

- **Asignatura:** Estructuras de Datos — Unidad 3
- **Programa:** Ingeniería de Software (CTEV) — Semestre IV
- **Institución:** Universidad de Cartagena
- **Docente:** John Carlos Arrieta Arrieta
- **Estudiante:** Yhon Andy Barrios Gándara
- **Caso Asignado:** Caso 4 — Restaurante
- **Entidad Principal:** `Pedido` (Identificador: `numeroPedido`)

---

## 1. Descripción Funcional
El sistema resuelve la gestión integral de pedidos en un restaurante comercial, modelando con rigor computacional las tres fases de atención:
1. **Admisión y Registro General:** Todo pedido ingresado se almacena de forma inmutable en una **Lista doblemente enlazada**, garantizando consulta histórica y auditoría.
2. **Cola de Espera en Cocina:** Cada pedido nuevo pasa de inmediato a una **Cola FIFO**, asegurando que los comensales sean atendidos en estricto orden cronológico de llegada.
3. **Historial de Despacho y Deshacer:** Cuando un pedido es preparado y facturado, se desencola y se apila en una **Pila LIFO**. Esta estructura permite registrar el historial inverso y ejecutar la reversión o cancelación inmediata del último pedido despachado (*Undo*).

---

## 2. Estructuras de Datos Implementadas (Sin librerías externas)
Se prescinde del uso de colecciones nativas de Java (`java.util.*`), implementando desde cero los algoritmos con manipulación directa de punteros y nodos:

* **`Nodo`:** Celda elemental de almacenamiento que contiene el dato (`Object`) y dos referencias (`siguiente` y `anterior`).
* **`Lista`:** Lista doblemente enlazada con punteros a `cabeza` y `cola`. Soporta inserciones y eliminaciones en extremos y posiciones intermedias, búsquedas por índice y por valor, y recorridos bidireccionales (`mostrarAdelante` y `mostrarAtras`).
* **`Cola`:** Estructura FIFO con punteros `frente` y `fin`. Implementa `encolar()`, `desencolar()` y `peek()` en $O(1)$.
* **`Pila`:** Estructura LIFO con puntero a `cima`. Implementa `apilar()`, `desapilar()`, `peek()` y `buscar()` en $O(1)$.

---

## 3. Algoritmos Destacados

### A. Cancelación mediante Cola Auxiliar
Para cumplir estrictamente con el principio de encapsulamiento de la Cola (no acceder al medio de la estructura):
1. Se desencola cada elemento de `colaPendientes` hacia una `colaAuxiliar`.
2. Cuando se localiza el pedido objetivo, se extrae, se actualiza su estado a `CANCELADO` y **no se ingresa a la auxiliar**.
3. Se vacía la `colaAuxiliar` re-encolando todos los pedidos restantes a la `colaPendientes` original, preservando el orden FIFO sin alteraciones.

### B. Deshacer Procesamiento con Pila
1. Se ejecuta `desapilar()` sobre `historialProcesados`, obteniendo el pedido más reciente.
2. Se revierte su estado a `PENDIENTE`.
3. Se re-encola en `colaPendientes` para su reingreso al flujo de cocina.

---

## 4. Instrucciones de Compilación y Ejecución

### Requisitos
- Java JDK 17 o superior.
- Apache Maven 3.8+.

### Compilación
```bash
mvn clean compile
```

### Ejecutar Pruebas Automatizadas
```bash
mvn test
```

### Ejecutar la Aplicación en Consola
```bash
mvn exec:java
```
