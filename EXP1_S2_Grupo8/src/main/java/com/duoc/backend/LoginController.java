package com.duoc.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("/login")
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass) {

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // TRAMPA DEL PROFE SUPERADA: Usamos BCrypt para comparar la contraseña ingresada con el Hash de la BD
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        if (!encoder.matches(encryptedPass, userDetails.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtAuthenticationConfig.getJWTToken(username);
        return token;
    }
}  