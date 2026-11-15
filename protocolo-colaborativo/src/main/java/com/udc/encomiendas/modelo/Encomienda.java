package com.udc.encomiendas.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Encomienda implements Comparable<Encomienda> {

    private String codigo;
    private Cliente remitente;
    private Cliente destinatario;
    private String ciudadDestino;
    private double peso;
    private String tipoServicio;
    private PrioridadEncomienda prioridad;
    private EstadoEncomienda estado;
    private LocalDateTime fechaRegistro;

    public Encomienda(String codigo, Cliente remitente, Cliente destinatario,
            String ciudadDestino, double peso, String tipoServicio,
            PrioridadEncomienda prioridad) {
        this.codigo = codigo;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.ciudadDestino = ciudadDestino;
        this.peso = peso;
        this.tipoServicio = tipoServicio;
        this.prioridad = prioridad;
        this.estado = EstadoEncomienda.REGISTRADA;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Encomienda(String codigo, Cliente remitente, Cliente destinatario,
            String ciudadDestino, double peso, String tipoServicio,
            PrioridadEncomienda prioridad, LocalDateTime fechaRegistro) {
        this.codigo = codigo;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.ciudadDestino = ciudadDestino;
        this.peso = peso;
        this.tipoServicio = tipoServicio;
        this.prioridad = prioridad;
        this.estado = EstadoEncomienda.REGISTRADA;
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getRemitente() {
        return remitente;
    }

    public void setRemitente(Cliente remitente) {
        this.remitente = remitente;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Cliente destinatario) {
        this.destinatario = destinatario;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public PrioridadEncomienda getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadEncomienda prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoEncomienda getEstado() {
        return estado;
    }

    public void setEstado(EstadoEncomienda estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Encomienda that = (Encomienda) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public int compareTo(Encomienda otra) {
        int comparacionPeso = Double.compare(this.peso, otra.peso);
        if (comparacionPeso != 0) {
            return comparacionPeso;
        }
        return this.codigo.compareTo(otra.codigo);
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("[%s] %s -> %s (%s) | Peso: %.2f kg | %s | %s | %s",
                codigo,
                remitente.getNombre(),
                destinatario.getNombre(),
                ciudadDestino,
                peso,
                tipoServicio,
                prioridad,
                estado,
                fechaRegistro.format(formato));
    }
}
