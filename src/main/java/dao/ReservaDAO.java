package dao;

import Modelo.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public boolean crearReserva(int idCliente, int idHabitacion, LocalDate checkIn, LocalDate checkOut) {
        String sql = "INSERT INTO reservas (id_cliente, id_habitacion, check_in, check_out, estado) VALUES (?, ?, ?, ?, 'Confirmada')";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.setInt(2, idHabitacion);
            stmt.setDate(3, Date.valueOf(checkIn));
            stmt.setDate(4, Date.valueOf(checkOut));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reserva> obtenerReservasActivas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, id_cliente, id_habitacion, check_in, check_out, estado, reserva_creada FROM reservas WHERE estado = 'Confirmada'";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setIdCliente(rs.getInt("id_cliente"));
                r.setIdHabitacion(rs.getInt("id_habitacion"));
                r.setCheckIn(rs.getDate("check_in").toLocalDate());
                r.setCheckOut(rs.getDate("check_out").toLocalDate());
                r.setEstado(rs.getString("estado"));
                r.setReservaCreada(rs.getTimestamp("reserva_creada").toLocalDateTime());
                reservas.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }
}