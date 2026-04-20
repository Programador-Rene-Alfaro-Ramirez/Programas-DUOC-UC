package com.duoc.backend;

import com.duoc.backend.JWTAuthenticationConfig;
import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.User;
import org.springframework.web.util.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder; // Importado para seguridad
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    // Inyectamos el codificador para validar contraseñas de forma segura
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public String login(@RequestBody User loginRequest) {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        // CORRECCIÓN SAST: Validamos usando matches() para evitar exposición de texto plano
        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Invalid login");
        }

        // Sanitizamos el username para evitar XSS (tu mejora previa)
        String sanitizedUsername = HtmlUtils.htmlEscape(loginRequest.getUsername()); [cite: 96, 97]
        
        String token = jwtAuthtenticationConfig.getJWTToken(sanitizedUsername);
        return token;
    }
}      