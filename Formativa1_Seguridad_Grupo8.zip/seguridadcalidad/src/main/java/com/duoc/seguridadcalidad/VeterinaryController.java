package com.duoc.seguridadcalidad;

import com.duoc.seguridadcalidad.model.Cita;
import com.duoc.seguridadcalidad.model.Paciente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VeterinaryController {

    // Listas en memoria para simular la base de datos 
    private static List<Paciente> listaPacientes = new ArrayList<>();
    private static List<Cita> listaCitas = new ArrayList<>();

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Ruta Privada: Registro de Pacientes [cite: 511]
    @GetMapping("/pacientes")
    public String listarPacientes(Model model) {
        model.addAttribute("pacientes", listaPacientes);
        return "pacientes";
    }

    @PostMapping("/pacientes/registrar")
    public String registrarPaciente(@RequestParam String nombre, @RequestParam String especie,
                                     @RequestParam String raza, @RequestParam int edad,
                                     @RequestParam String dueño) {
        listaPacientes.add(new Paciente(nombre, especie, raza, edad, dueño));
        return "redirect:/pacientes";
    }

    // Ruta Privada: Programación de Citas [cite: 513]
    @GetMapping("/citas")
    public String listarCitas(Model model) {
        model.addAttribute("citas", listaCitas);
        return "citas";
    }

    @PostMapping("/citas/programar")
    public String programarCita(@RequestParam String fecha, @RequestParam String hora,
                                 @RequestParam String motivo, @RequestParam String veterinario) {
        listaCitas.add(new Cita(fecha, hora, motivo, veterinario));
        return "redirect:/citas";
    }
}