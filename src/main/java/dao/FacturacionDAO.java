package dao;

import Modelo.Factura;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturacionDAO {

    // Nombres de columnas (por claridad y evitar errores)
    private static final String COL_ID = "id";
    private static final String COL_ID_RESERVA = "id_reserva";
    private static final String COL_TOTAL = "total";
    private static final String COL_FECHA = "fecha";
    private static final String COL_PAGADO = "pagado";

    // ==========================================================
    // === CREAR FACTURA
    // ==========================================================
    public boolean crearFactura(int idReserva, BigDecimal total) {
        String sql = "INSERT INTO facturas (id_reserva, total, fecha, pagado) VALUES (?, ?, CURDATE(), false)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            stmt.setBigDecimal(2, total);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear factura: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // === MARCAR COMO PAGADA
    // ==========================================================
    public boolean marcarComoPagada(int idFactura) {
        String sql = "UPDATE facturas SET pagado = true WHERE id = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFactura);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar factura como pagada: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // === OBTENER TODAS LAS FACTURAS
    // ==========================================================
    public List<Factura> obtenerTodasFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas ORDER BY fecha DESC";

        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Factura f = new Factura();
                f.setId(rs.getInt(COL_ID));
                f.setIdReserva(rs.getInt(COL_ID_RESERVA));
                f.setTotal(rs.getBigDecimal(COL_TOTAL));
                f.setFecha(rs.getDate(COL_FECHA).toLocalDate());
                f.setPagado(rs.getBoolean(COL_PAGADO));
                facturas.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener facturas: " + e.getMessage());
        }
        return facturas;
    }

    // ==========================================================
    // === OBTENER FACTURA POR ID
    // ==========================================================
    public Factura obtenerPorId(int id) {
        String sql = "SELECT * FROM facturas WHERE id = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Factura f = new Factura();
                    f.setId(rs.getInt(COL_ID));
                    f.setIdReserva(rs.getInt(COL_ID_RESERVA));
                    f.setTotal(rs.getBigDecimal(COL_TOTAL));
                    f.setFecha(rs.getDate(COL_FECHA).toLocalDate());
                    f.setPagado(rs.getBoolean(COL_PAGADO));
                    return f;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener factura por ID: " + e.getMessage());
        }
        return null;
    }

    // ==========================================================
    // === OBTENER FACTURA POR ID DE RESERVA
    // ==========================================================
    public Factura obtenerPorReserva(int idReserva) {
        String sql = "SELECT * FROM facturas WHERE id_reserva = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Factura f = new Factura();
                    f.setId(rs.getInt(COL_ID));
                    f.setIdReserva(rs.getInt(COL_ID_RESERVA));
                    f.setTotal(rs.getBigDecimal(COL_TOTAL));
                    f.setFecha(rs.getDate(COL_FECHA).toLocalDate());
                    f.setPagado(rs.getBoolean(COL_PAGADO));
                    return f;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener factura por reserva: " + e.getMessage());
        }
        return null;
    }
}
