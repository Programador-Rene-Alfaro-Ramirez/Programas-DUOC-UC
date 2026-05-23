package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Curso;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Curso> consultarTodosCursos() {
        return cursoRepository.findAll();
    }

    public Optional<Curso> consultarCursoPorId(Long id) {
        return cursoRepository.findById(id);
    }

    public Curso registrarCurso(Curso curso) {
        // Corregido: Validamos que el profesor exista usando el objeto profesor
        if (curso.getProfesor() == null || curso.getProfesor().getId() == null) {
            throw new RuntimeException("Debe asignar un profesor válido al curso.");
        }
        
        usuarioRepository.findById(curso.getProfesor().getId())
            .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + curso.getProfesor().getId()));
            
        return cursoRepository.save(curso);
    }

    public Curso modificarCurso(Long id, Curso cursoActualizado) {
        return cursoRepository.findById(id)
            .map(curso -> {
                curso.setNombre(cursoActualizado.getNombre());
                curso.setDescripcion(cursoActualizado.getDescripcion());
                // Corregido: Actualizamos el objeto profesor completo
                curso.setProfesor(cursoActualizado.getProfesor());
                return cursoRepository.save(curso);
            })
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }

    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado con ID: " + id);
        }
        cursoRepository.deleteById(id);
    }

    public List<Curso> consultarCursosPorProfesor(Long profesorId) {
        return cursoRepository.findByProfesorId(profesorId);
    }
} 