package Modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private int idCliente;
    private int idHabitacion;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String estado;
    private LocalDateTime reservaCreada;


    public Reserva(int idCliente, int idHabitacion, LocalDate checkIn, LocalDate checkOut, String estado) {
        this.idCliente = idCliente;
        this.idHabitacion = idHabitacion;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getReservaCreada() {
        return reservaCreada;
    }

    public void setReservaCreada(LocalDateTime reservaCreada) {
        this.reservaCreada = reservaCreada;
    }
}
