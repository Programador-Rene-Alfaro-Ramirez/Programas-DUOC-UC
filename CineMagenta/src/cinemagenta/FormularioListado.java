package cinemagenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import conexionbd.DatabaseConnection;

public class FormularioListado extends JFrame {
    private final JTable tablaPeliculas;
    private final JComboBox<String> comboGenero;
    private final JSpinner spinnerDesde;
    private final JSpinner spinnerHasta;
    private final JButton btnFiltrar;

    public FormularioListado() {
        setTitle("Listado de Películas");
        setSize(700, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Componentes de filtro
        JLabel lblGenero = new JLabel("Género:");
        comboGenero = new JComboBox<>(new String[]{"Todos", "Acción", "Comedia", "Drama", "Terror", "Ciencia Ficción"});
        JLabel lblDesde = new JLabel("Desde:");
        spinnerDesde = new JSpinner(new SpinnerNumberModel(1980, 1900, 2100, 1));
        JLabel lblHasta = new JLabel("Hasta:");
        spinnerHasta = new JSpinner(new SpinnerNumberModel(2023, 1900, 2100, 1));
        btnFiltrar = new JButton("Filtrar");

        // Tabla
        tablaPeliculas = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tablaPeliculas);

        // Posicionamiento
        lblGenero.setBounds(20, 20, 60, 25);
        comboGenero.setBounds(80, 20, 120, 25);
        lblDesde.setBounds(220, 20, 50, 25);
        spinnerDesde.setBounds(270, 20, 70, 25);
        lblHasta.setBounds(350, 20, 50, 25);
        spinnerHasta.setBounds(400, 20, 70, 25);
        btnFiltrar.setBounds(490, 20, 100, 25);
        scrollTabla.setBounds(20, 60, 640, 280);

        // Agregar componentes
        add(lblGenero); add(comboGenero);
        add(lblDesde); add(spinnerDesde);
        add(lblHasta); add(spinnerHasta);
        add(btnFiltrar); add(scrollTabla);

        // Acción del botón con lambda
        btnFiltrar.addActionListener(e -> listarPeliculas());

        // Carga inicial
        listarPeliculas();

        setVisible(true);
    }

    private void listarPeliculas() {
        String genero = comboGenero.getSelectedItem().toString();
        int desde = (int) spinnerDesde.getValue();
        int hasta = (int) spinnerHasta.getValue();

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Título");
        modelo.addColumn("Director");
        modelo.addColumn("Género");
        modelo.addColumn("Año");
        modelo.addColumn("Duración");

        try (Connection conn = DatabaseConnection.conectar()) {
            String sql = "SELECT * FROM peliculas WHERE anio BETWEEN ? AND ?";
            PreparedStatement stmt;

            if (!genero.equals("Todos")) {
                sql += " AND genero = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, desde);
                stmt.setInt(2, hasta);
                stmt.setString(3, genero);
            } else {
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, desde);
                stmt.setInt(2, hasta);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("titulo"),
                    rs.getString("director"),
                    rs.getString("genero"),
                    rs.getInt("anio"),
                    rs.getInt("duracion")
                });
            }

            tablaPeliculas.setModel(modelo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al listar películas:\n" + ex.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
