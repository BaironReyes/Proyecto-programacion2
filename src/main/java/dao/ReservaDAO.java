package dao;

import Modelo.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public boolean crearReserva(Reserva reserva) {
        String sql = "INSERT INTO reservas (id_cliente, id_habitacion, check_in, check_out, estado) VALUES (?, ?, ?, ?, 'Confirmada')";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdHabitacion());
            stmt.setDate(3, java.sql.Date.valueOf(reserva.getCheckIn()));
            stmt.setDate(4, java.sql.Date.valueOf(reserva.getCheckOut()));
            //stmt.setDouble(5,reserva.getTotal());

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