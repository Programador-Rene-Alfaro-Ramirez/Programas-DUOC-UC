package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    public List<Inscripcion> consultarTodasInscripciones() {
        return (List<Inscripcion>) inscripcionRepository.findAll();
    }

    public Inscripcion registrarInscripcion(Inscripcion inscripcion) {
        return inscripcionRepository.save(inscripcion);
    }

    public void eliminarInscripcion(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new RuntimeException("Inscripción no encontrada con ID: " + id);
        }
        inscripcionRepository.deleteById(id);
    }
}  