package com.duoc.LearningPlatformValidation.repository;

import com.duoc.LearningPlatformValidation.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    List<Inscripcion> findByCursoId(Long cursoId);
    
    List<Inscripcion> findByEstudianteId(Long estudianteId);

    @Query("SELECT COUNT(i) FROM Inscripcion i WHERE i.curso.id = :cursoId")
    Integer contarInscripcionesPorCurso(@Param("cursoId") Long cursoId);
}  