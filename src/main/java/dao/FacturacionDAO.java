package dao;

import Modelo.Factura;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturacionDAO {

    // Constantes para nombres de columnas
    private static final String COL_ID = "id";
    private static final String COL_ID_RESERVA = "id_reserva";
    private static final String COL_TOTAL = "total";
    private static final String COL_FECHA = "fecha";
    private static final String COL_PAGADO = "pagado";

    // Constante para "SELECT"
    private static final String SELECT = "SELECT ";

    public boolean crearFactura(int idReserva, BigDecimal total) {
        String sql = "INSERT INTO facturas (id_reserva, total) VALUES (?, ?)";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idReserva);
            stmt.setBigDecimal(2, total);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean marcarComoPagada(int idFactura) {
        String sql = "UPDATE facturas SET pagado = true WHERE id = ?";
        try (Connection conn = Conexion.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFactura);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Factura> obtenerTodasFacturas() {
        List<Factura> facturas = new ArrayList<>();
        String sql = SELECT + COL_ID + ", " + COL_ID_RESERVA + ", " + COL_TOTAL + ", " + COL_FECHA + ", " + COL_PAGADO +
                " FROM facturas ORDER BY " + COL_FECHA + " DESC";
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
            e.printStackTrace();
        }
        return facturas;
    }

    public Factura obtenerPorId(int id) {
        String sql = SELECT + COL_ID + ", " + COL_ID_RESERVA + ", " + COL_TOTAL + ", " + COL_FECHA + ", " + COL_PAGADO +
                " FROM facturas WHERE " + COL_ID + " = ?";
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
            e.printStackTrace();
        }
        return null;
    }

    public Factura obtenerPorReserva(int idReserva) {
        String sql = SELECT + COL_ID + ", " + COL_ID_RESERVA + ", " + COL_TOTAL + ", " + COL_FECHA + ", " + COL_PAGADO +
                " FROM facturas WHERE " + COL_ID_RESERVA + " = ?";
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
            e.printStackTrace();
        }
        return null;
    }
}