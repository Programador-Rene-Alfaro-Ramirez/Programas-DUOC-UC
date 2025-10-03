package cinemagenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import conexionbd.DatabaseConnection;

public class PeliculaDAO {
    public boolean agregar(Pelicula p) {
        String sql = "INSERT INTO peliculas (titulo, director, genero, anio, duracion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getTitulo());
            stmt.setString(2, p.getDirector());
            stmt.setString(3, p.getGenero());
            stmt.setInt(4, p.getAnio());
            stmt.setInt(5, p.getDuracion());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al agregar película: " + ex.getMessage());
            return false;
        }
    }

    public boolean modificar(Pelicula p) {
        String sql = "UPDATE peliculas SET director = ?, genero = ?, anio = ?, duracion = ? WHERE titulo = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getDirector());
            stmt.setString(2, p.getGenero());
            stmt.setInt(3, p.getAnio());
            stmt.setInt(4, p.getDuracion());
            stmt.setString(5, p.getTitulo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al modificar película: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminarPorTitulo(String titulo) {
        String sql = "DELETE FROM peliculas WHERE titulo = ?";
        try (Connection conn = DatabaseConnection.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            return stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar película: " + ex.getMessage());
            return false;
        }
    }

    public List<Pelicula> listarTodas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";

        try (Connection conn = DatabaseConnection.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pelicula p = new Pelicula(
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getString("genero"),
                    rs.getInt("anio"),
                    rs.getInt("duracion")
                );
                lista.add(p);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar películas: " + ex.getMessage());
        }

        return lista;
    }
}


