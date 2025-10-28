package Modelo;

import java.time.LocalDate;

public class Factura {
    private int idFactura;
    private int idReserva;
    private double total;
    private LocalDate fecha;
    private String estado;
    private boolean pagado;

    public Factura() {}

    public Factura(int idFactura, int idReserva, double total, LocalDate fecha, String estado) {
        this.idFactura = idFactura;
        this.idReserva = idReserva;
        this.total = total;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public boolean isPagado() {
        return pagado;
    }
    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
}
