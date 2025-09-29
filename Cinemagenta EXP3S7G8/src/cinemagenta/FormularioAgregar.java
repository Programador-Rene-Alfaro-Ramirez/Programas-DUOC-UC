package cinemagenta;

import conexionbd.DatabaseConnection;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class FormularioAgregar extends JFrame {
    private final JTextField txtTitulo;
    private final JTextField txtDirector;
    private final JTextField txtAnio;
    private final JTextField txtDuracion;
    private final JComboBox<String> cmbGenero;

    public FormularioAgregar() {
        setTitle("Agregar Película");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(30, 20, 100, 25);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(140, 20, 200, 25);

        JLabel lblDirector = new JLabel("Director:");
        lblDirector.setBounds(30, 60, 100, 25);
        txtDirector = new JTextField();
        txtDirector.setBounds(140, 60, 200, 25);

        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setBounds(30, 100, 100, 25);
        txtAnio = new JTextField();
        txtAnio.setBounds(140, 100, 200, 25);

        JLabel lblDuracion = new JLabel("Duración (min):");
        lblDuracion.setBounds(30, 140, 100, 25);
        txtDuracion = new JTextField();
        txtDuracion.setBounds(140, 140, 200, 25);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(30, 180, 100, 25);
        cmbGenero = new JComboBox<>(new String[] {
            "Comedia", "Drama", "Acción", "Terror", "Romance", "Ciencia Ficción"
        });
        cmbGenero.setBounds(140, 180, 200, 25);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(80, 220, 100, 30);
        btnAgregar.addActionListener(e -> guardar());

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(200, 220, 100, 30);
        btnLimpiar.addActionListener(e -> limpiar());

        add(lblTitulo); add(txtTitulo);
        add(lblDirector); add(txtDirector);
        add(lblAnio); add(txtAnio);
        add(lblDuracion); add(txtDuracion);
        add(lblGenero); add(cmbGenero);
        add(btnAgregar); add(btnLimpiar);
    }

    private void guardar() {
        String titulo = txtTitulo.getText().trim();
        String director = txtDirector.getText().trim();
        String anioStr = txtAnio.getText().trim();
        String duracionStr = txtDuracion.getText().trim();
        String genero = (String) cmbGenero.getSelectedItem();

        if (titulo.isEmpty() || director.isEmpty() || anioStr.isEmpty() || duracionStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos.");
            return;
        }

        try {
            int anio = Integer.parseInt(anioStr);
            int duracion = Integer.parseInt(duracionStr);

            Connection con = DatabaseConnection.conectar();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.");
                return;
            }

            String sql = "INSERT INTO Cartelera (titulo, director, anio, duracion, genero) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, titulo);
            ps.setString(2, director);
            ps.setInt(3, anio);
            ps.setInt(4, duracion);
            ps.setString(5, genero);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
            int nuevoId = rs.getInt(1);
            JOptionPane.showMessageDialog(this, "Película agregada con ID: " + nuevoId);
}

            JOptionPane.showMessageDialog(this, "Película agregada correctamente.");
            limpiar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y duración deben ser números válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtTitulo.setText("");
        txtDirector.setText("");
        txtAnio.setText("");
        txtDuracion.setText("");
        cmbGenero.setSelectedIndex(0);
    }
}
