package cinemagenta;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Menú Principal");
        setSize(400, 300);
        setLayout(new GridLayout(5, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Botones
        JButton btnAgregar = new JButton("Agregar Película");
        JButton btnModificar = new JButton("Modificar Película");
        JButton btnEliminar = new JButton("Eliminar Película");
        JButton btnListar = new JButton("Listar Películas");
        JButton btnSalir = new JButton("Salir");

        // Acciones con lambda
        btnAgregar.addActionListener(e -> new FormularioAgregar().setVisible(true));
        btnModificar.addActionListener(e -> new FormularioModificar().setVisible(true));
        btnEliminar.addActionListener(e -> new FormularioEliminar().setVisible(true));
        btnListar.addActionListener(e -> new FormularioListado().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));

        // Agregar botones
        add(btnAgregar);
        add(btnModificar);
        add(btnEliminar);
        add(btnListar);
        add(btnSalir);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
