package com.duoc.LearningPlatformValidation.repository;

import com.duoc.LearningPlatformValidation.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByRol(String rol);
    Usuario findByCorreo(String correo);
}