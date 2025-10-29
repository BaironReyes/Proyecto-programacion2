package Modelo;

public class Factura {
    private int id;
    private int idReserva;
    private double total;
    private String fecha;
    private boolean pagado;

    public Factura() {}

    public Factura(int id, int idReserva, double total, String fecha, boolean pagado) {
        this.id = id;
        this.idReserva = idReserva;
        this.total = total;
        this.fecha = fecha;
        this.pagado = pagado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
}
