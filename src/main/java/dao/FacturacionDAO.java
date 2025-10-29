package dao;

import Modelo.Factura;
import Modelo.Reserva;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturacionDAO {

    private Connection con;

    public FacturacionDAO() throws SQLException {
        con = Conexion.obtenerConexion();
    }

    // 🔹 Obtener reservas pendientes de facturar
    public List<Reserva> obtenerReservasPendientes() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.id, r.id_cliente, r.id_habitacion, r.check_in, r.check_out, r.estado " +
                "FROM reservas r " +
                "WHERE r.id NOT IN (SELECT id_reserva FROM facturas)";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId(rs.getInt("id"));
                r.setIdCliente(rs.getInt("id_cliente"));
                r.setIdHabitacion(rs.getInt("id_habitacion"));
                r.setCheckIn(rs.getDate("check_in").toLocalDate());
                r.setCheckOut(rs.getDate("check_out").toLocalDate());
                r.setEstado(rs.getString("estado"));
                reservas.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener reservas pendientes: " + e.getMessage());
        }
        return reservas;
    }

    // 🔹 Obtener todas las facturas existentes
    public List<Factura> obtenerFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Factura f = new Factura();
                f.setId(rs.getInt("id"));
                f.setIdReserva(rs.getInt("id_reserva"));
                f.setTotal(rs.getDouble("total"));
                f.setFecha(rs.getString("fecha"));
                f.setPagado(rs.getBoolean("pagado"));
                facturas.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener facturas: " + e.getMessage());
        }
        return facturas;
    }

    // 🔹 Generar una nueva factura
    public boolean generarFactura(int idReserva, double total, String fecha) {
        String sql = "INSERT INTO facturas (id_reserva, total, fecha, pagado) VALUES (?, ?, ?, 0)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ps.setDouble(2, total);
            ps.setString(3, fecha);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al generar factura: " + e.getMessage());
            return false;
        }
    }

    // Marcar factura como pagada
    public boolean marcarFacturaPagada(int idFactura) {
        String sql = "UPDATE facturas SET pagado = 1 WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idFactura);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al marcar factura como pagada: " + e.getMessage());
            return false;
        }
    }
}
