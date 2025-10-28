import dao.Conexion;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = Conexion.obtenerConexion()) {
            System.out.println("Conexión exitosa!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
