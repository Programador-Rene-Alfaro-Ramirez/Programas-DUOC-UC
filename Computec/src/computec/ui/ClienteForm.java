package computec.ui;

import computec.dao.ClienteDAO;
import computec.model.Cliente;
import java.awt.*;
import javax.swing.*;
import java.util.List;

public class ClienteForm extends JFrame {

    private final JTextField txtRut, txtNombre, txtDireccion, txtCorreo, txtTelefono;
    private final JButton btnGuardar, btnEditar, btnEliminar, btnCerrar;
    private final JComboBox<Cliente> cmbClientes;
    private final ClienteDAO clienteDAO = new ClienteDAO();

    public ClienteForm() {
        setTitle("Gestión de Clientes");
        setSize(500, 400);
        setLayout(new GridLayout(8, 2, 5, 5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Seleccionar Cliente:"));
        cmbClientes = new JComboBox<>();
        add(cmbClientes);
        cmbClientes.addActionListener(e -> cargarClienteSeleccionado());

        add(new JLabel("RUT:"));
        txtRut = new JTextField(); add(txtRut);

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField(); add(txtNombre);

        add(new JLabel("Dirección:"));
        txtDireccion = new JTextField(); add(txtDireccion);

        add(new JLabel("Correo:"));
        txtCorreo = new JTextField(); add(txtCorreo);

        add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField(); add(txtTelefono);

        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnCerrar = new JButton("Cerrar");

        btnGuardar.addActionListener(e -> guardarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnCerrar.addActionListener(e -> dispose());

        add(btnGuardar); add(btnEditar);
        add(btnEliminar); add(btnCerrar);

        cargarClientes();
    }

    private void cargarClientes() {
        cmbClientes.removeAllItems();
        List<Cliente> lista = clienteDAO.listarClientes();
        for (Cliente c : lista) cmbClientes.addItem(c);
    }

    private void cargarClienteSeleccionado() {
        Cliente c = (Cliente) cmbClientes.getSelectedItem();
        if (c != null) {
            txtRut.setText(c.getRut());
            txtNombre.setText(c.getNombre());
            txtDireccion.setText(c.getDireccion());
            txtCorreo.setText(c.getCorreo());
            txtTelefono.setText(c.getTelefono());
        }
    }

    private void guardarCliente() {
        Cliente c = new Cliente(0, txtRut.getText(), txtNombre.getText(), txtDireccion.getText(),
                txtCorreo.getText(), txtTelefono.getText());
        if (clienteDAO.insertarCliente(c)) {
            JOptionPane.showMessageDialog(this, "Cliente agregado correctamente");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente");
        }
    }

    private void editarCliente() {
        Cliente sel = (Cliente) cmbClientes.getSelectedItem();
        if (sel == null) return;

        Cliente c = new Cliente(sel.getIdCliente(), txtRut.getText(), txtNombre.getText(),
                txtDireccion.getText(), txtCorreo.getText(), txtTelefono.getText());
        if (clienteDAO.actualizarCliente(c)) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar");
        }
    }

    private void eliminarCliente() {
        Cliente sel = (Cliente) cmbClientes.getSelectedItem();
        if (sel == null) return;
        if (clienteDAO.eliminarCliente(sel.getIdCliente())) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar");
        }
    }
}