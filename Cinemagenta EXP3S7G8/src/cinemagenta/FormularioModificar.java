package cinemagenta;

import conexionbd.DatabaseConnection;
import java.awt.HeadlessException;
import javax.swing.*;
import java.sql.*;

public class FormularioModificar extends JFrame {

    private final JTextField txtId;
    private final JTextField txtTitulo;
    private final JTextField txtDirector;
    private final JTextField txtAnio;
    private final JTextField txtDuracion;
    private final JComboBox<String> cmbGenero;
    private final JButton btnBuscar;
    private final JButton btnModificar;

    public FormularioModificar() {
        setTitle("Modificar Película");
        setSize(450, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblId = new JLabel("ID Película:");
        lblId.setBounds(30, 20, 100, 25);
        txtId = new JTextField();
        txtId.setBounds(140, 20, 200, 25);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(350, 20, 80, 25);
        btnBuscar.addActionListener(e -> buscar());

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(30, 60, 100, 25);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(140, 60, 200, 25);

        JLabel lblDirector = new JLabel("Director:");
        lblDirector.setBounds(30, 100, 100, 25);
        txtDirector = new JTextField();
        txtDirector.setBounds(140, 100, 200, 25);

        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setBounds(30, 140, 100, 25);
        txtAnio = new JTextField();
        txtAnio.setBounds(140, 140, 200, 25);

        JLabel lblDuracion = new JLabel("Duración:");
        lblDuracion.setBounds(30, 180, 100, 25);
        txtDuracion = new JTextField();
        txtDuracion.setBounds(140, 180, 200, 25);

        JLabel lblGenero = new JLabel("Género:");
        lblGenero.setBounds(30, 220, 100, 25);
        cmbGenero = new JComboBox<>(new String[] {
            "Comedia", "Drama", "Acción", "Terror", "Romance", "Ciencia Ficción"
        });
        cmbGenero.setBounds(140, 220, 200, 25);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(140, 270, 200, 30);
        btnModificar.addActionListener(e -> modificar());
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(30, 270, 100, 30);
        btnLimpiar.addActionListener(e -> limpiarCampos());
        
        add(btnLimpiar);
        add(lblId); add(txtId); add(btnBuscar);
        add(lblTitulo); add(txtTitulo);
        add(lblDirector); add(txtDirector);
        add(lblAnio); add(txtAnio);
        add(lblDuracion); add(txtDuracion);
        add(lblGenero); add(cmbGenero);
        add(btnModificar);
    }

    private void buscar() {
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la película.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Connection con = DatabaseConnection.conectar();
            String sql = "SELECT * FROM Cartelera WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                txtTitulo.setText(rs.getString("titulo"));
                txtDirector.setText(rs.getString("director"));
                txtAnio.setText(String.valueOf(rs.getInt("anio")));
                txtDuracion.setText(String.valueOf(rs.getInt("duracion")));
                cmbGenero.setSelectedItem(rs.getString("genero"));
            } else {
                JOptionPane.showMessageDialog(this, "Película no encontrada.");
            }
        } catch (HeadlessException | NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
        }
    }

    private void modificar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String titulo = txtTitulo.getText().trim();
            String director = txtDirector.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            String genero = (String) cmbGenero.getSelectedItem();

            Connection con = DatabaseConnection.conectar();
            String sql = "UPDATE Cartelera SET titulo=?, director=?, anio=?, duracion=?, genero=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, titulo);
            ps.setString(2, director);
            ps.setInt(3, anio);
            ps.setInt(4, duracion);
            ps.setString(5, genero);
            ps.setInt(6, id);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Película modificada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo modificar la película.");
            }
        } catch (HeadlessException | NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }
    
    private void limpiarCampos() {
    txtId.setText("");
    txtTitulo.setText("");
    txtDirector.setText("");
    txtAnio.setText("");
    txtDuracion.setText("");
    cmbGenero.setSelectedIndex(0);
    }
}
