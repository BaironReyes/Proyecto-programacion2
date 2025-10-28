package dao;

import Modelo.Habitacion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {

    public boolean insertarHabitacion(String numero, String tipo, double precio, String estado) {
        String sql = "INSERT INTO habitaciones (numero_habitacion, tipo_habitacion, precio, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero);
            stmt.setString(2, tipo);
            stmt.setDouble(3, precio);
            stmt.setString(4, estado);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Habitacion> listarHabitaciones() {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT id, numero_habitacion, tipo_habitacion, precio, estado FROM habitaciones";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Habitacion h = new Habitacion();
                h.setId(rs.getInt("id"));
                h.setNumeroHabitacion(rs.getString("numero_habitacion"));
                h.setTipoHabitacion(rs.getString("tipo_habitacion"));
                h.setPrecio(rs.getBigDecimal("precio"));
                h.setEstado(rs.getString("estado"));
                habitaciones.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habitaciones;
    }
}
