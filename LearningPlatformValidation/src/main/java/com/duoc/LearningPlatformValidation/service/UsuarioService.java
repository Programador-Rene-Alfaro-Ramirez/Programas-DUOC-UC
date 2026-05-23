package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Usuario;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Usuario> consultarTodosUsuarios() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> consultarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()) != null) {
            throw new RuntimeException("El correo ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }
    
    public Usuario modificarUsuario(Long id, Usuario usuarioActualizado) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombre(usuarioActualizado.getNombre());
                usuario.setCorreo(usuarioActualizado.getCorreo());
                usuario.setContrasena(usuarioActualizado.getContrasena());
                usuario.setRol(usuarioActualizado.getRol());
                return usuarioRepository.save(usuario);
            })
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
    
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    public List<Usuario> consultarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }
}