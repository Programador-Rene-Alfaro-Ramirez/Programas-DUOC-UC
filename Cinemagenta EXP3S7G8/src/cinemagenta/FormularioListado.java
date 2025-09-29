package cinemagenta;

import conexionbd.DatabaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class FormularioListado extends JFrame {
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public FormularioListado() {
        setTitle("Listado de Peliculas");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Titulo");
        modelo.addColumn("Director");
        modelo.addColumn("Año");
        modelo.addColumn("Duracion");
        modelo.addColumn("Genero");

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 640, 300);
        add(scroll);

        cargarPeliculas();
    }

    private void cargarPeliculas() {
        try {
            Connection con = DatabaseConnection.conectar();
            String sql = "SELECT * FROM Cartelera";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("titulo");
                fila[2] = rs.getString("director");
                fila[3] = rs.getInt("anio");
                fila[4] = rs.getInt("duracion");
                fila[5] = rs.getString("genero");
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar peliculas: " + ex.getMessage());
        }
    }
}
