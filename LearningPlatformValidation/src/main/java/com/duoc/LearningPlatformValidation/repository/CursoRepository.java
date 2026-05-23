package com.duoc.LearningPlatformValidation.repository;

import com.duoc.LearningPlatformValidation.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByProfesorId(Long profesorId);
    
    @Query("SELECT c FROM Curso c WHERE c.nombre LIKE %:nombre%")
    List<Curso> buscarPorNombre(@Param("nombre") String nombre);
}
