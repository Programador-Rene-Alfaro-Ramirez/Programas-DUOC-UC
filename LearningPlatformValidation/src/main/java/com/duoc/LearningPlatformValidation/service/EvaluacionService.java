package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Evaluacion;
import com.duoc.LearningPlatformValidation.repository.EvaluacionRepository;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public List<Evaluacion> consultarTodasEvaluaciones() {
        return evaluacionRepository.findAll();
    }

    public List<Evaluacion> consultarEvaluacionesPorCurso(Long cursoId) {
        return evaluacionRepository.findByCursoId(cursoId);
    }

    public Evaluacion registrarEvaluacion(Evaluacion evaluacion) {
        cursoRepository.findById(evaluacion.getCursoId())
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + evaluacion.getCursoId()));
        return evaluacionRepository.save(evaluacion);
    }

    public Evaluacion modificarEvaluacion(Long id, Evaluacion evaluacionActualizada) {
        return evaluacionRepository.findById(id)
            .map(evaluacion -> {
                evaluacion.setCursoId(evaluacionActualizada.getCursoId());
                evaluacion.setNombre(evaluacionActualizada.getNombre());
                evaluacion.setPuntajeMaximo(evaluacionActualizada.getPuntajeMaximo());
                evaluacion.setFechaAplicacion(evaluacionActualizada.getFechaAplicacion());
                return evaluacionRepository.save(evaluacion);
            })
            .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + id));
    }

    public void eliminarEvaluacion(Long id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new RuntimeException("Evaluación no encontrada con ID: " + id);
        }
        evaluacionRepository.deleteById(id);
    }
} 