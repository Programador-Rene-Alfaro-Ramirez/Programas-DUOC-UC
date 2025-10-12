package computec.dao;

import computec.conexionbd.DatabaseConnection;
import computec.model.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    public boolean insertarVenta(Venta venta) {
        String sql = "CALL insertar_venta(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, venta.getIdCliente());
            ps.setInt(2, venta.getIdEquipo());
            ps.setDouble(3, venta.getPrecioFinal());
            ps.setDate(4, new java.sql.Date(venta.getFechaVenta().getTime()));
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar venta: " + e.getMessage());
            return false;
        }
    }

    public List<Venta> listarVentas(String tipoFiltro) {
        List<Venta> lista = new ArrayList<>();
        String sql = "CALL listar_ventas()";

        if (tipoFiltro != null && !tipoFiltro.equals("Todos")) {
            sql = "CALL listar_ventas_por_tipo(?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (tipoFiltro != null && !tipoFiltro.equals("Todos")) {
                ps.setString(1, tipoFiltro);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Venta v = new Venta(
                    rs.getInt("idVenta"),
                    rs.getInt("idCliente"),
                    rs.getInt("idEquipo"),
                    rs.getDouble("precioFinal"),
                    rs.getDate("fechaVenta")
                );
                lista.add(v);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar ventas: " + e.getMessage());
        }

        return lista;
    }

    public boolean eliminarVenta(int idVenta) {
        String sql = "CALL eliminar_venta(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idVenta);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar venta: " + e.getMessage());
            return false;
        }
    }
}