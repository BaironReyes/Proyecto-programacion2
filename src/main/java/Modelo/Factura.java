package Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Factura {
    private int id;
    private int idReserva;
    private BigDecimal total;
    private LocalDate fecha;
    private boolean pagado;

    public Factura() {
        this.fecha = LocalDate.now();
        this.pagado = false;
    }

    public Factura(int idReserva, BigDecimal total) {
        this.idReserva = idReserva;
        this.total = total;
        this.fecha = LocalDate.now();
        this.pagado = false;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", idReserva=" + idReserva +
                ", total=" + total +
                ", fecha=" + fecha +
                ", pagado=" + pagado +
                '}';
    }
}
