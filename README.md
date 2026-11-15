# Universidad de Cartagena — Estructuras de Datos
## Unidad 3: Java Collections Framework (JCF)

Repositorio oficial para las actividades prácticas y colaborativas de la asignatura de **Estructuras de Datos** en el programa de Ingeniería de Software (CTEV) de la Universidad de Cartagena.

---

### Módulos y Entregas de la Unidad

1. **[Protocolo Colaborativo — Sistema Logístico de Gestión de Encomiendas](./protocolo-colaborativo/)**
   - **Caso 4:** Diseño y construcción de un sistema de gestión y despacho de encomiendas utilizando exclusivamente el **Java Collections Framework (JCF)**.
   - **Colecciones implementadas:**
     - `List<Encomienda>` (`ArrayList`): Catálogo histórico maestro de admisión y trazabilidad.
     - `Map<String, Encomienda>` (`HashMap`): Índice directo de búsqueda y validación de unicidad en tiempo constante $O(1)$.
     - `Set<String>` (`HashSet`): Control de cobertura territorial y plazas de destino sin duplicados en $O(1)$.
     - `TreeSet<Encomienda>`: Catálogo ordenado naturalmente por peso ascendente mediante el contrato `Comparable<Encomienda>` ($O(\log n)$).
     - `PriorityQueue<Encomienda>`: Cola de despacho dinámico por niveles de prioridad (`URGENTE` > `ALTA` > `NORMAL` > `ECONOMICA`) con desempate FIFO por fecha ($O(\log n)$).
   - **Características clave:** Sincronización transaccional entre las 5 colecciones, banco inicial de 15 encomiendas de prueba, menú interactivo por consola (10 operaciones guiadas) y panel de auditoría de consistencia de datos en tiempo real.

---

### Información Institucional

- **Estudiante:** Yhon Andy Barrios Gándara
- **Programa:** Ingeniería de Software
- **Asignatura:** Estructuras de Datos (Unidad 3)
- **Tutor:** John Carlos Arrieta Arrieta
- **Institución:** Universidad de Cartagena — CTEV
- **Año:** 2026
