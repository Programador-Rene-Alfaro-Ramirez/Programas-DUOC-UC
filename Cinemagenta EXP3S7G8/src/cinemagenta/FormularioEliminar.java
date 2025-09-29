package cinemagenta;

import conexionbd.DatabaseConnection;
import java.awt.HeadlessException;
import javax.swing.*;
import java.sql.*;

public class FormularioEliminar extends JFrame {
    private final JTextField txtId;
    private final JButton btnEliminar;

    public FormularioEliminar() {
        setTitle("Eliminar Película");
        setSize(400, 200);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblId = new JLabel("ID Película:");
        lblId.setBounds(30, 30, 100, 25);
        txtId = new JTextField();
        txtId.setBounds(140, 30, 200, 25);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(140, 80, 200, 30);
        btnEliminar.addActionListener(e -> eliminar());
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(30, 80, 100, 30);
        btnLimpiar.addActionListener(e -> txtId.setText(""));
        
        add(btnLimpiar);
        add(lblId);
        add(txtId);
        add(btnEliminar);
    }

    private void eliminar() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la película.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);

            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar esta película?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Connection con = DatabaseConnection.conectar();
                String sql = "DELETE FROM Cartelera WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Película eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna película con ese ID.");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido. Debe ser un número.");
        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}
