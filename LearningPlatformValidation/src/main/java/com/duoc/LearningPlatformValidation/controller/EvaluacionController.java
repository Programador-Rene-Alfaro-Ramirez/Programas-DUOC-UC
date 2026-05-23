package com.duoc.LearningPlatformValidation.controller;

import com.duoc.LearningPlatformValidation.model.Evaluacion;
import com.duoc.LearningPlatformValidation.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @GetMapping
    public ResponseEntity<List<Evaluacion>> consultarTodasEvaluaciones() {
        return ResponseEntity.ok(evaluacionService.consultarTodasEvaluaciones());
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Evaluacion>> consultarEvaluacionesPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(evaluacionService.consultarEvaluacionesPorCurso(cursoId));
    }

    @PostMapping
    public ResponseEntity<Evaluacion> registrarEvaluacion(@RequestBody Evaluacion evaluacion) {
        Evaluacion nuevaEvaluacion = evaluacionService.registrarEvaluacion(evaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEvaluacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> modificarEvaluacion(@PathVariable Long id, @RequestBody Evaluacion evaluacion) {
        Evaluacion evaluacionActualizada = evaluacionService.modificarEvaluacion(id, evaluacion);
        return ResponseEntity.ok(evaluacionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evaluacionService.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }
} 