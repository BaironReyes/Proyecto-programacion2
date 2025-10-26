package dao;

import Modelo.Cliente;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insertarCliente(String nombre, String apellido, String email,
                                   String telefono, String identificacion, String tipoIdentificacion) {
        String sql = "INSERT INTO clientes (nombre, apellido, email, telefono, identificacion, tipo_identificacion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, email);
            stmt.setString(4, telefono);
            stmt.setString(5, identificacion);
            stmt.setString(6, tipoIdentificacion);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cliente> obtenerTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, email, telefono, identificacion, tipo_identificacion, fecha_creacion FROM clientes";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setApellido(rs.getString("apellido"));
                c.setEmail(rs.getString("email"));
                c.setTelefono(rs.getString("telefono"));
                c.setIdentificacion(rs.getString("identificacion"));
                c.setTipoIdentificacion(rs.getString("tipo_identificacion"));
                c.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                clientes.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}