package computec.ui;

import computec.dao.VentaDAO;
import computec.model.Venta;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ReporteForm extends JFrame {

    private final JTable tabla;
    private final JLabel lblTotalVentas;
    private final JLabel lblMontoTotal;
    private JComboBox<String> cmbFiltro;
    private final VentaDAO ventaDAO = new VentaDAO();

    public ReporteForm() {
        setTitle("Reporte de Ventas");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cmbFiltro = new JComboBox<>(new String[]{"Todos", "Desktop", "Laptop"});
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnActualizar = new JButton("Actualizar");

        JPanel pnlTop = new JPanel();
        pnlTop.add(new JLabel("Tipo de equipo:"));
        pnlTop.add(cmbFiltro);
        pnlTop.add(btnFiltrar);
        pnlTop.add(btnActualizar);

        tabla = new JTable();
        JScrollPane scroll = new JScrollPane(tabla);

        lblTotalVentas = new JLabel("Total ventas: 0");
        lblMontoTotal = new JLabel("Monto total: $0");
        JPanel pnlBottom = new JPanel();
        pnlBottom.add(lblTotalVentas);
        pnlBottom.add(lblMontoTotal);

        add(pnlTop, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(pnlBottom, BorderLayout.SOUTH);

        btnFiltrar.addActionListener(e -> cargarVentas((String) cmbFiltro.getSelectedItem()));
        btnActualizar.addActionListener(e -> cargarVentas("Todos"));

        cargarVentas("Todos");
    }

    private void cargarVentas(String tipo) {
        List<Venta> lista = ventaDAO.listarVentas(tipo);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Venta");
        model.addColumn("ID Cliente");
        model.addColumn("ID Equipo");
        model.addColumn("Precio Final");
        model.addColumn("Fecha Venta");

        double total = 0;
        for (Venta v : lista) {
            model.addRow(new Object[]{
                v.getIdVenta(),
                v.getIdCliente(),
                v.getIdEquipo(),
                v.getPrecioFinal(),
                v.getFechaVenta()
            });
            total += v.getPrecioFinal();
        }

        tabla.setModel(model);
        lblTotalVentas.setText("Total ventas: " + lista.size());
        lblMontoTotal.setText("Monto total: $" + total);
    }
}