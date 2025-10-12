package computec.dao;

import computec.conexionbd.DatabaseConnection;
import computec.model.Equipo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {
    public boolean insertarEquipo(Equipo equipo) {
        String sql = "CALL insertar_equipo(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, equipo.getMarca());
            ps.setString(2, equipo.getModelo());
            ps.setDouble(3, equipo.getPrecio());
            ps.setString(4, equipo.getTipo());
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar equipo: " + e.getMessage());
            return false;
        }
    }

    public List<Equipo> listarEquipos() {
        List<Equipo> lista = new ArrayList<>();
        String sql = "CALL listar_equipos()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Equipo e = new Equipo(
                    rs.getInt("idEquipo"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getDouble("precio"),
                    rs.getString("tipo")
                );
                lista.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar equipos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEquipo(Equipo equipo) {
        String sql = "CALL actualizar_equipo(?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, equipo.getIdEquipo());
            ps.setString(2, equipo.getMarca());
            ps.setString(3, equipo.getModelo());
            ps.setDouble(4, equipo.getPrecio());
            ps.setString(5, equipo.getTipo());
            ps.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar equipo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarEquipo(int idEquipo) {
        String sql = "CALL eliminar_equipo(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);
            ps.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar equipo: " + e.getMessage());
            return false;
        }
    }
}