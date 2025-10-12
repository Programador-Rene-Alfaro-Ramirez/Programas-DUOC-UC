package computec.ui;

import computec.dao.ClienteDAO;
import computec.dao.EquipoDAO;
import computec.dao.VentaDAO;
import computec.model.Cliente;
import computec.model.Equipo;
import computec.model.Venta;
import computec.patterns.Descuento;
import computec.patterns.DescuentoBase;
import computec.patterns.DescuentoDesktop15;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class VentaForm extends JFrame {

    private final JComboBox<Cliente> cmbCliente;
    private final JComboBox<Equipo> cmbEquipo;
    private final JTextField txtPrecio;
    private final JButton btnRegistrar;
    private final JButton btnEliminar;
    private final JButton btnCerrar;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final EquipoDAO equipoDAO = new EquipoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    public VentaForm() {
        setTitle("Registrar Venta");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 5, 5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ---- Componentes ----
        add(new JLabel("Cliente:"));
        cmbCliente = new JComboBox<>(clienteDAO.listarClientes().toArray(new Cliente[0]));
        add(cmbCliente);

        add(new JLabel("Equipo:"));
        cmbEquipo = new JComboBox<>(equipoDAO.listarEquipos().toArray(new Equipo[0]));
        cmbEquipo.addActionListener(e -> actualizarPrecio());
        add(cmbEquipo);

        add(new JLabel("Precio Final:"));
        txtPrecio = new JTextField();
        txtPrecio.setEditable(false);
        add(txtPrecio);

        // ---- Botones ----
        btnRegistrar = new JButton("Registrar Venta");
        btnEliminar = new JButton("Eliminar Venta");
        btnCerrar = new JButton("Cerrar");

        btnRegistrar.addActionListener(e -> registrarVenta());
        btnEliminar.addActionListener(e -> eliminarVenta());
        btnCerrar.addActionListener(e -> dispose());

        add(btnRegistrar);
        add(btnEliminar);
        add(btnCerrar);

        actualizarPrecio();
    }

    private void actualizarPrecio() {
        Equipo equipo = (Equipo) cmbEquipo.getSelectedItem();
        if (equipo != null) {
            Descuento descuento = new DescuentoDesktop15(new DescuentoBase());
            double precioFinal = descuento.aplicarDescuento(equipo.getPrecio(), equipo.getTipo());
            txtPrecio.setText(String.valueOf(precioFinal));
        }
    }

    private void registrarVenta() {
        Equipo eq = (Equipo) cmbEquipo.getSelectedItem();
        Cliente cl = (Cliente) cmbCliente.getSelectedItem();

        if (eq == null || cl == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente y un equipo");
            return;
        }

        double precioFinal = Double.parseDouble(txtPrecio.getText());
        Venta venta = new Venta(cl.getIdCliente(), eq.getIdEquipo(), precioFinal, new Date());

        if (ventaDAO.insertarVenta(venta)) {
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente");
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar venta");
        }
    }

    private void eliminarVenta() {
        String idVentaStr = JOptionPane.showInputDialog(this, "Ingrese el ID de la venta a eliminar:");
        if (idVentaStr == null || idVentaStr.trim().isEmpty()) return;

        try {
            int idVenta = Integer.parseInt(idVentaStr);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar la venta #" + idVenta + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (ventaDAO.eliminarVenta(idVenta)) {
                    JOptionPane.showMessageDialog(this, "Venta eliminada correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la venta o hubo un error");
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número de ID válido️");
        }
    }
}