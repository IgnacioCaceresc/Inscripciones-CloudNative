package com.duoc.LearningPlatformValidation.repository;

import com.duoc.LearningPlatformValidation.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    // Este método cumple con el requerimiento de filtrar por curso
    List<Inscripcion> findByCursoId(Long cursoId);
}