package com.duoc.backend;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecuredController {

    // SOLUCIÓN HALLAZGO 7: Endpoint real para agendar citas (Eliminamos el greetings)
    @PostMapping("/citas")
    public String agendarCita(
            @RequestParam(value="fecha") String fecha,
            @RequestParam(value="hora") String hora,
            @RequestParam(value="motivo") String motivo,
            @RequestParam(value="veterinario") String veterinario) {
        
        // Simulación de guardado en base de datos
        return "Cita médica confirmada para el " + fecha + " a las " + hora + " hrs. Motivo: " + motivo + ". Atiende: Dr(a). " + veterinario;
    }
} 