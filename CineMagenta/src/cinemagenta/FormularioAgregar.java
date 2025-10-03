package cinemagenta;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import conexionbd.DatabaseConnection;

public class FormularioAgregar extends JFrame {
    private final JTextField txtTitulo;
    private final JTextField txtDirector;
    private final JTextField txtGenero;
    private final JSpinner spinnerAnio;
    private final JSpinner spinnerDuracion;
    private final JButton btnAgregar;

    public FormularioAgregar() {
        setTitle("Agregar Película");
        setSize(400, 320);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Etiquetas
        JLabel lblTitulo = new JLabel("Título:");
        JLabel lblDirector = new JLabel("Director:");
        JLabel lblGenero = new JLabel("Género:");
        JLabel lblAnio = new JLabel("Año:");
        JLabel lblDuracion = new JLabel("Duración:");

        // Campos de texto
        txtTitulo = new JTextField();
        txtDirector = new JTextField();
        txtGenero = new JTextField();

        // Spinners para año y duración
        spinnerAnio = new JSpinner(new SpinnerNumberModel(2023, 1900, 2100, 1));
        spinnerDuracion = new JSpinner(new SpinnerNumberModel(90, 1, 500, 1));

        // Botón
        btnAgregar = new JButton("Agregar");

        // Posicionamiento
        lblTitulo.setBounds(30, 20, 80, 25);
        txtTitulo.setBounds(120, 20, 220, 25);
        lblDirector.setBounds(30, 60, 80, 25);
        txtDirector.setBounds(120, 60, 220, 25);
        lblGenero.setBounds(30, 100, 80, 25);
        txtGenero.setBounds(120, 100, 220, 25);
        lblAnio.setBounds(30, 140, 80, 25);
        spinnerAnio.setBounds(120, 140, 100, 25);
        lblDuracion.setBounds(30, 180, 80, 25);
        spinnerDuracion.setBounds(120, 180, 100, 25);
        btnAgregar.setBounds(140, 230, 120, 30);

        // Agregar componentes
        add(lblTitulo); add(txtTitulo);
        add(lblDirector); add(txtDirector);
        add(lblGenero); add(txtGenero);
        add(lblAnio); add(spinnerAnio);
        add(lblDuracion); add(spinnerDuracion);
        add(btnAgregar);

        // Acción del botón
        btnAgregar.addActionListener((ActionEvent e) -> {
            agregarPelicula();
        });

        setVisible(true);
    }

    private void agregarPelicula() {
        String titulo = txtTitulo.getText().trim();
        String director = txtDirector.getText().trim();
        String genero = txtGenero.getText().trim();
        int anio = (int) spinnerAnio.getValue();
        int duracion = (int) spinnerDuracion.getValue();

        // Validación de campos obligatorios
        if (titulo.isEmpty() || director.isEmpty() || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor completa todos los campos obligatorios.",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.conectar()) {
            String sql = "INSERT INTO peliculas (titulo, director, genero, anio, duracion) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, director);
            stmt.setString(3, genero);
            stmt.setInt(4, anio);
            stmt.setInt(5, duracion);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Película agregada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se pudo agregar la película.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar la película:\n" + ex.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtDirector.setText("");
        txtGenero.setText("");
        spinnerAnio.setValue(2023);
        spinnerDuracion.setValue(90);
    }
}
