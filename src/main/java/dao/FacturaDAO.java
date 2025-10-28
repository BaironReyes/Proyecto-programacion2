package dao;

import Modelo.Factura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    // Constantes para nombres de columnas
    private static final String COL_ID = "id";
    private static final String COL_ID_RESERVA = "id_reserva";
    private static final String COL_TOTAL = "total";
    private static final String COL_FECHA = "fecha";
    private static final String COL_PAGADO = "pagado";
    private Connection connection;
    // Constante para "SELECT"
    private static final String SELECT = "SELECT ";

    public FacturaDAO() {
        try {
            connection = Conexion.obtenerConexion();
            if (connection == null) {
                throw new SQLException("No se pudo obtener la conexión a la base de datos");
            }
        } catch (SQLException e) {
            System.err.println("Error en FacturaDAO al obtener conexión: " + e.getMessage());
        }
    }

    public boolean insertarFactura(Factura factura) {
        if (connection == null) {
            System.err.println("Conexión nula en insertarFactura");
            return false;
        }

        String sql = "INSERT INTO facturas (id_reserva, total, fecha, estado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, factura.getIdReserva());
            stmt.setDouble(2, factura.getTotal());
            stmt.setDate(3, Date.valueOf(factura.getFecha()));
            stmt.setString(4, factura.getEstado());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar factura: " + e.getMessage());
            return false;
        }
    }

    public List<Factura> listarFacturas() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM facturas";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Factura f = new Factura(
                        rs.getInt("id_factura"),
                        rs.getInt("id_reserva"),
                        rs.getDouble("total"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("estado")
                );
                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizarEstado(int idFactura, String nuevoEstado) {
        String sql = "UPDATE facturas SET estado = ? WHERE id_factura = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idFactura);
            stmt.executeUpdate();
            return true;
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
                f.setIdFactura(rs.getInt(COL_ID));
                f.setIdReserva(rs.getInt(COL_ID_RESERVA));
                f.setTotal(rs.getDouble(COL_TOTAL));
                f.setFecha(rs.getDate(COL_FECHA).toLocalDate());
                f.setPagado((rs.getBoolean(COL_PAGADO)));
                facturas.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facturas;
    }
}

