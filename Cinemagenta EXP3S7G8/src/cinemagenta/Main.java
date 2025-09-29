package cinemagenta;

import javax.swing.*;
import cinemagenta.FormularioAgregar;
import cinemagenta.FormularioModificar;
import cinemagenta.FormularioEliminar;
import cinemagenta.FormularioListado;

public class Main extends JFrame {
    public Main() {
        setTitle("Cine Magenta");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        JButton btnAgregar = new JButton("Agregar Pelicula");
        btnAgregar.setBounds(50, 50, 200, 30);
        btnAgregar.addActionListener(e -> {
            new FormularioAgregar().setVisible(true);
        });
        add(btnAgregar);

        JButton btnModificar = new JButton("Modificar Pelicula");
        btnModificar.setBounds(50, 100, 200, 30);
        btnModificar.addActionListener(e -> {
            new FormularioModificar().setVisible(true);
        });
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar Pelicula");
        btnEliminar.setBounds(50, 150, 200, 30);
        btnEliminar.addActionListener(e -> {
            new FormularioEliminar().setVisible(true);
        });
        add(btnEliminar);
        
        JButton btnListar = new JButton("Listar Peliculas");
        btnListar.setBounds(50, 200, 200, 30);
        btnListar.addActionListener(e -> {
            new FormularioListado().setVisible(true);
        });
        add(btnListar);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}

