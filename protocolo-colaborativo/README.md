# Sistema Logístico de Gestión de Encomiendas
## Implementación Profesional con Java Collections Framework (JCF)

**Curso:** Estructuras de Datos  
**Unidad 3:** Java Collections Framework (JCF)  
**Actividad:** Protocolo Colaborativo (Modalidad Individual) — Caso 4: Sistema de gestión de encomiendas  
**Institución:** Universidad de Cartagena — Centro de Tecnologías de la Educación Virtual (CTEV)  
**Estudiante:** Yhon Andy Barrios Gándara  
**Tutor:** John Carlos Arrieta Arrieta  

---

## 1. Descripción del Caso y Alcance

El presente sistema modela la operación logística integral de una empresa de envíos y paquetería nacional. Se descarta cualquier estructura de datos manual o artesanal (sin nodos primitivos ni listas enlazadas ad-hoc) para adoptar con rigor el **Java Collections Framework (JCF)** de la biblioteca estándar de Java SE 17.

El sistema administra encomiendas caracterizadas por código alfanumérico único, remitente, destinatario, ciudad de destino, peso en kilogramos, tipo de servicio logístico, nivel de prioridad operativa y estado de ciclo de vida.

---

## 2. Arquitectura de Colecciones y Mapeo de Requisitos

Para resolver cada necesidad operativa con eficiencia algorítmica óptima, se diseñó un modelo coordinado compuesto por **5 colecciones especializadas**:

```
                                  [ SISTEMA LOGÍSTICO ]
                                            |
         +-----------------+----------------+-----------------+-----------------+
         |                 |                |                 |                 |
         v                 v                v                 v                 v
   ArrayList<E>       HashMap<K,V>     TreeSet<E>      PriorityQueue<E>    HashSet<T>
  (Catálogo Maestro) (Índice Código)  (Catálogo Peso)  (Cola Despacho)    (Cobertura)
      O(1) add          O(1) get        O(log n)          O(log n)          O(1) add/contains
      Secuencial        Directo        Natural Comp.     Prioridad+FIFO     Sin duplicados
```

### 2.1. `List<Encomienda>` (`ArrayList`) — Catálogo Histórico Maestro
- **Rol:** Registro cronológico y secuencial de todas las operaciones de admisión.
- **Razón técnica:** Proporciona inserción amortizada $O(1)$, acceso indexado aleatorio en $O(1)$ y conservación intacta del orden de recepción. Preserva la trazabilidad completa para auditorías e informes generales.

### 2.2. `Map<String, Encomienda>` (`HashMap`) — Índice Directo de Rastreo
- **Rol:** Búsqueda y validación instantánea de encomiendas mediante su código único de guía (ej. `ENC-1001`).
- **Razón técnica:** Acceso, inserción y recuperación en tiempo constante promedio $O(1)$ gracias a la función hash. Evita barridos lineales $O(n)$ en consultas de estado, validación de unicidad en admisión y modificaciones.

### 2.3. `Set<String>` (`HashSet`) — Control de Plazas y Cobertura Única
- **Rol:** Control estricto de destinos geográficos cubiertos por la red logística.
- **Razón técnica:** Garantiza unicidad matemática (cero duplicados de ciudades) en inserción y verificación en $O(1)$. Facilita reportar al instante la cobertura operativa sin requerir filtrados manuales.

### 2.4. `TreeSet<Encomienda>` — Catálogo Ordenado por Peso
- **Rol:** Consulta del catálogo de carga clasificado de menor a mayor peso para cubicaje y tarifas de flete.
- **Razón técnica:** Estructura en árbol rojo-negro autobalanceado que mantiene los elementos persistentemente ordenados en $O(\log n)$.
- **Contrato de consistencia:** `Encomienda` implementa `Comparable<Encomienda>` comparando primero por `peso` ascendente y desempatando por `codigo`. Esto garantiza que `(a.compareTo(b) == 0) == a.equals(b)`, impidiendo que encomiendas distintas con pesos idénticos sean descartadas erróneamente por el árbol.

### 2.5. `PriorityQueue<Encomienda>` — Cola de Despacho Logístico Prioritario
- **Rol:** Despacho dinámico de vehículos donde los envíos más urgentes se atienden primero.
- **Razón técnica:** Montículo binario (*min-heap*) que permite inspeccionar la encomienda más prioritaria en $O(1)$ (`peek()`) y extraerla en $O(\log n)$ (`poll()`).
- **Criterio de ordenamiento:** `Comparator` basado en el nivel ordinal de `PrioridadEncomienda` (`URGENTE` [1] > `ALTA` [2] > `NORMAL` [3] > `ECONOMICA` [4]), con desempate temporal **FIFO** estricto por `fechaRegistro`.

---

## 3. Matriz de Complejidad Asintótica (Big-O)

| Operación Logística | Colección Subyacente | Complejidad Temporal | Complejidad Espacial | Justificación Algorítmica |
| :--- | :--- | :---: | :---: | :--- |
| **Registrar nueva encomienda** | `ArrayList` + `HashMap` + `TreeSet` + `PriorityQueue` + `HashSet` | $O(\log n)$ | $O(1)$ | La inserción en `TreeSet` y `PriorityQueue` domina asintóticamente ($O(\log n)$) sobre las inserciones $O(1)$ de lista, mapa y conjunto. |
| **Búsqueda por código de rastreo** | `HashMap<String, Encomienda>` | $O(1)$ | $O(1)$ | Búsqueda directa por valor hash de la clave alfanumérica. |
| **Consultar próximo despacho** | `PriorityQueue<Encomienda>` | $O(1)$ | $O(1)$ | Inspección directa de la raíz del montículo (`peek()`) sin extracción. |
| **Despachar siguiente paquete** | `PriorityQueue<Encomienda>` | $O(\log n)$ | $O(1)$ | Extracción de raíz (`poll()`) y rebalanceo (*heapify-down*) en árbol binario. |
| **Consultar catálogo ordenado** | `TreeSet<Encomienda>` | $O(n)$ | $O(n)$ | Recorrido en orden (*in-order traversal*) del árbol rojo-negro. |
| **Consultar cobertura única** | `HashSet<String>` | $O(k)$ | $O(k)$ | Iteración sobre los $k$ baldes de ciudades distintas registradas. |
| **Modificar destino** | `HashMap` + `HashSet` | $O(1)$ | $O(1)$ | Acceso $O(1)$ por mapa, mutación in-situ y adición $O(1)$ a la tabla hash del set. |
| **Cancelar encomienda** | `HashMap` + `TreeSet` + `PriorityQueue` | $O(n)$ | $O(1)$ | Localización $O(1)$ en mapa y retiro $O(n)$ en heap/árbol por búsqueda de nodo. |
| **Eliminar definitivamente** | 5 colecciones sincronizadas | $O(n)$ | $O(1)$ | Remoción coordinada garantizando integridad referencial absoluta. |

---

## 4. Contratos de Identidad e Integridad en el Modelo

Para que las colecciones basadas en hashing (`HashMap`, `HashSet`) y en comparación (`TreeSet`, `PriorityQueue`) operen sin fugas de memoria ni colisiones indeseadas, la entidad `Encomienda` satisface rigurosamente los siguientes contratos:

1. **`equals(Object obj)` y `hashCode()`:** Basados de forma estricta e inmutable en el atributo `codigo`. Dos encomiendas son idénticas si y sólo si poseen la misma guía de rastreo.
2. **`Comparable<Encomienda>`:**
   ```java
   @Override
   public int compareTo(Encomienda otra) {
       if (otra == null) return 1;
       int cmp = Double.compare(this.peso, otra.peso);
       if (cmp != 0) return cmp;
       return this.codigo.compareTo(otra.codigo); // Desempate determinista
   }
   ```
3. **Encapsulamiento y Vistas Inmutables:** Los métodos de consulta en `GestionEncomiendasService` devuelven colecciones protegidas mediante `Collections.unmodifiableList()` y `Collections.unmodifiableSet()`, evitando modificaciones externas que corrompan el estado interno del servicio.

---

## 5. Datos Iniciales Precargados (Banco de Prueba)

El sistema se inicializa con un banco de **15 encomiendas realistas** distribuidas en diversas ciudades colombianas:

| Código | Remitente | Destinatario | Ciudad Destino | Peso (kg) | Servicio | Prioridad |
| :---: | :--- | :--- | :--- | :---: | :--- | :--- |
| `ENC-1001` | Carlos Mendoza | María Gómez | Barranquilla | 2.50 | Express | Urgente (1) |
| `ENC-1002` | Andrés Ruiz | Laura Torres | Bogotá | 8.20 | Estándar | Normal (3) |
| `ENC-1003` | David Meza | Carlos Mendoza | Cartagena | 1.20 | Express | Alta (2) |
| `ENC-1004` | María Gómez | Valentina Ríos | Santa Marta | 15.00 | Carga | Económica (4) |
| `ENC-1005` | Laura Torres | Andrés Ruiz | Medellín | 3.80 | Express | Urgente (1) |
| `ENC-1006` | Valentina Ríos | David Meza | Cali | 5.50 | Estándar | Normal (3) |
| `ENC-1007` | Jorge Castro | Diana Herrera | Cartagena | 12.30 | Carga | Alta (2) |
| `ENC-1008` | Diana Herrera | Jorge Castro | Bucaramanga | 0.80 | Documentos | Urgente (1) |
| `ENC-1009` | Carlos Mendoza | Laura Torres | Bogotá | 22.00 | Carga | Económica (4) |
| `ENC-1010` | Andrés Ruiz | María Gómez | Barranquilla | 4.10 | Estándar | Normal (3) |
| `ENC-1011` | David Meza | Valentina Ríos | Santa Marta | 6.70 | Express | Alta (2) |
| `ENC-1012` | María Gómez | Jorge Castro | Bucaramanga | 1.90 | Documentos | Normal (3) |
| `ENC-1013` | Laura Torres | David Meza | Cali | 18.50 | Carga | Económica (4) |
| `ENC-1014` | Valentina Ríos | Carlos Mendoza | Cartagena | 7.40 | Estándar | Alta (2) |
| `ENC-1015` | Jorge Castro | Andrés Ruiz | Medellín | 3.20 | Express | Normal (3) |

---

## 6. Opciones del Menú Interactivo de Consola

El punto de entrada `com.udc.encomiendas.Main` implementa 10 operaciones guiadas:

1. **Registrar nueva encomienda:** Admisión con validación de código único y sincronización en las 5 colecciones.
2. **Consultar todas las encomiendas:** Tabulado maestro cronológico respaldado en `ArrayList`.
3. **Buscar encomienda por código:** Localización instantánea $O(1)$ respaldada en `HashMap`.
4. **Consultar ciudades de destino únicas:** Reporte de cobertura respaldado en `HashSet`.
5. **Mostrar catálogo ordenado por peso:** Vista clasificada ascendente de 0.80 kg a 22.00 kg mediante `TreeSet`.
6. **Consultar próxima encomienda a despachar:** Inspección $O(1)$ de cabecera prioritaria con `PriorityQueue.peek()`.
7. **Despachar siguiente encomienda prioritaria:** Extracción $O(\log n)$ con `PriorityQueue.poll()` y transición a `EN_TRANSITO`.
8. **Modificar ciudad de destino:** Cambio in-situ y actualización de cobertura única en el `HashSet`.
9. **Cancelar o eliminar encomienda:** Submenú que demuestra cancelación lógica vs eliminación física completa.
10. **Auditoría de consistencia de colecciones:** Tablero de métricas en tiempo real que reporta los tamaños de las 5 colecciones para verificar sincronía.
0. **Salir del sistema.**

---

## 7. Compilación y Ejecución

### Requisitos
- **Java SE Development Kit (JDK):** Versión 17 o superior.
- **Apache Maven:** Versión 3.8 o superior.

### Comandos de Terminal

Compilar y limpiar el proyecto:
```bash
mvn clean compile
```

Ejecutar la aplicación interactiva:
```bash
mvn exec:java -Dexec.mainClass="com.udc.encomiendas.Main"
```

O directamente mediante el binario Java:
```bash
java -cp target/classes com.udc.encomiendas.Main
```
