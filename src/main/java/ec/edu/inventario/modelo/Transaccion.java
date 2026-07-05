package ec.edu.inventario.modelo;

import java.time.LocalDate;

public abstract class Transaccion {

    protected int idTransaccion;
    protected LocalDate fecha;
    protected double total;
    protected String estado;

    public Transaccion() {
        this.fecha = LocalDate.now();
        this.estado = "PENDIENTE";
    }

    public void cambiarEstado(String estado) {
        this.estado = estado;
    }

    public String mostrarResumen() {
        return "Fecha: " + fecha + " | Total: " + total + " | Estado: " + estado;
    }

    public abstract double calcularTotal();

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }
}
