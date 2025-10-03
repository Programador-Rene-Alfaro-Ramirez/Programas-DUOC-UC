package cinemagenta;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import conexionbd.DatabaseConnection;

public class FormularioEliminar extends JFrame {
    private final JTextField txtTitulo;
    private final JButton btnEliminar;

    public FormularioEliminar() {
        setTitle("Eliminar Película");
        setSize(350, 180);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel lblTitulo = new JLabel("Título a eliminar:");
        txtTitulo = new JTextField();
        btnEliminar = new JButton("Eliminar");

        lblTitulo.setBounds(30, 30, 120, 25);
        txtTitulo.setBounds(150, 30, 150, 25);
        btnEliminar.setBounds(100, 80, 120, 30);

        add(lblTitulo); add(txtTitulo); add(btnEliminar);

        btnEliminar.addActionListener((ActionEvent e) -> {
            eliminarPelicula();
        });

        setVisible(true);
    }

    private void eliminarPelicula() {
        String titulo = txtTitulo.getText().trim();

        // Validación
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debes ingresar el título de la película a eliminar.",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmación
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas eliminar la película \"" + titulo + "\"?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.conectar()) {
            String sql = "DELETE FROM peliculas WHERE titulo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this,
                    "Película eliminada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                txtTitulo.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "No se encontró ninguna película con ese título.",
                    "Sin coincidencias",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al eliminar la película:\n" + ex.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
