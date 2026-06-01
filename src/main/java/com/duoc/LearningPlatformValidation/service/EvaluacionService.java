package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Evaluacion;
import com.duoc.LearningPlatformValidation.repository.EvaluacionRepository;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;
import com.duoc.LearningPlatformValidation.exceptions.ResourceNotFoundException;
import com.duoc.LearningPlatformValidation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private CursoRepository cursoRepository; // Inyectamos esto para validar el curso

    // Método para crear una evaluación (POST)
    public Evaluacion crearEvaluacion(Evaluacion evaluacion) {
        // 1. Validaciones de negocio básicas
        if (evaluacion.getNombre() == null || evaluacion.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre de la evaluación es obligatorio.");
        }
       if (evaluacion.getPuntajeMaximo() <= 0) {
            throw new ValidationException("El puntaje máximo debe ser mayor a 0.");
        }
        if (evaluacion.getCursoId() == null) {
            throw new ValidationException("El ID del curso es obligatorio para crear una evaluación.");
        }

        // 2. Validar que el curso realmente exista en la base de datos
        if (!cursoRepository.existsById(evaluacion.getCursoId())) {
            throw new ResourceNotFoundException("No se puede crear la evaluación: El curso con ID " + evaluacion.getCursoId() + " no existe.");
        }

        // 3. Seteamos la fecha de aplicación si no viene en el JSON
        if (evaluacion.getFechaAplicacion() == null) {
            evaluacion.setFechaAplicacion(new Date());
        }

        return evaluacionRepository.save(evaluacion);
    }

    // Método para listar evaluaciones por curso (GET)
    public List<Evaluacion> listarPorCurso(Long cursoId) {
        // Validar si el curso existe antes de buscar sus evaluaciones
        if (!cursoRepository.existsById(cursoId)) {
            throw new ResourceNotFoundException("No se pueden listar las evaluaciones: El curso con ID " + cursoId + " no existe.");
        }
        
        return evaluacionRepository.findByCursoId(cursoId);
    }
    public Evaluacion actualizar(Long id, Evaluacion evaluacionExistente) {
    Evaluacion e = evaluacionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));
    e.setNombre(evaluacionExistente.getNombre());
    e.setPuntajeMaximo(evaluacionExistente.getPuntajeMaximo());
    return evaluacionRepository.save(e);
  }
}