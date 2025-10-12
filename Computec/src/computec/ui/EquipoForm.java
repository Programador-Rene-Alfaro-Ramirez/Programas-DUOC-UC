package computec.ui;

import computec.dao.EquipoDAO;
import computec.model.Equipo;
import java.awt.*;
import javax.swing.*;
import java.util.List;

public class EquipoForm extends JFrame {

    private final JTextField txtMarca, txtModelo, txtPrecio;
    private final JComboBox<String> cmbTipo;
    private final JComboBox<Equipo> cmbEquipos;
    private final JButton btnGuardar, btnEditar, btnEliminar, btnCerrar;
    private final EquipoDAO equipoDAO = new EquipoDAO();

    public EquipoForm() {
        setTitle("Gestión de Equipos");
        setSize(500, 400);
        setLayout(new GridLayout(7, 2, 5, 5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Seleccionar Equipo:"));
        cmbEquipos = new JComboBox<>();
        add(cmbEquipos);
        cmbEquipos.addActionListener(e -> cargarEquipoSeleccionado());

        add(new JLabel("Marca:"));
        txtMarca = new JTextField(); add(txtMarca);

        add(new JLabel("Modelo:"));
        txtModelo = new JTextField(); add(txtModelo);

        add(new JLabel("Precio:"));
        txtPrecio = new JTextField(); add(txtPrecio);

        add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(new String[]{"Desktop", "Laptop"}); add(cmbTipo);

        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnCerrar = new JButton("Cerrar");

        btnGuardar.addActionListener(e -> guardarEquipo());
        btnEditar.addActionListener(e -> editarEquipo());
        btnEliminar.addActionListener(e -> eliminarEquipo());
        btnCerrar.addActionListener(e -> dispose());

        add(btnGuardar); add(btnEditar);
        add(btnEliminar); add(btnCerrar);

        cargarEquipos();
    }

    private void cargarEquipos() {
        cmbEquipos.removeAllItems();
        List<Equipo> lista = equipoDAO.listarEquipos();
        for (Equipo e : lista) cmbEquipos.addItem(e);
    }

    private void cargarEquipoSeleccionado() {
        Equipo e = (Equipo) cmbEquipos.getSelectedItem();
        if (e != null) {
            txtMarca.setText(e.getMarca());
            txtModelo.setText(e.getModelo());
            txtPrecio.setText(String.valueOf(e.getPrecio()));
            cmbTipo.setSelectedItem(e.getTipo());
        }
    }

    private void guardarEquipo() {
        Equipo e = new Equipo(0, txtMarca.getText(), txtModelo.getText(),
                Double.parseDouble(txtPrecio.getText()), (String) cmbTipo.getSelectedItem());
        if (equipoDAO.insertarEquipo(e)) {
            JOptionPane.showMessageDialog(this, "Equipo agregado");
            cargarEquipos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar");
        }
    }

    private void editarEquipo() {
        Equipo sel = (Equipo) cmbEquipos.getSelectedItem();
        if (sel == null) return;

        Equipo e = new Equipo(sel.getIdEquipo(), txtMarca.getText(), txtModelo.getText(),
                Double.parseDouble(txtPrecio.getText()), (String) cmbTipo.getSelectedItem());
        if (equipoDAO.actualizarEquipo(e)) {
            JOptionPane.showMessageDialog(this, "Equipo actualizado️");
            cargarEquipos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar");
        }
    }

    private void eliminarEquipo() {
        Equipo sel = (Equipo) cmbEquipos.getSelectedItem();
        if (sel == null) return;
        if (equipoDAO.eliminarEquipo(sel.getIdEquipo())) {
            JOptionPane.showMessageDialog(this, "Equipo eliminado");
            cargarEquipos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar");
        }
    }
}