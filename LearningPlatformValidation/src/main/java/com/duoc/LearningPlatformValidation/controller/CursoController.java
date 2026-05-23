package com.duoc.LearningPlatformValidation.controller;

import com.duoc.LearningPlatformValidation.model.Curso;
import com.duoc.LearningPlatformValidation.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> consultarTodosCursos() {
        return ResponseEntity.ok(cursoService.consultarTodosCursos());
    }

    @PostMapping
    public ResponseEntity<Curso> registrarCurso(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.registrarCurso(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCurso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> modificarCurso(@PathVariable Long id, @RequestBody Curso curso) {
        Curso cursoActualizado = cursoService.modificarCurso(id, curso);
        return ResponseEntity.ok(cursoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}  