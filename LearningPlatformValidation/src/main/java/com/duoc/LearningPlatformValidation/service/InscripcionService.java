package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.repository.InscripcionRepository;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;
import com.duoc.LearningPlatformValidation.exceptions.ResourceNotFoundException;
import com.duoc.LearningPlatformValidation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Necesario para validar estudiante

    @Autowired
    private CursoRepository cursoRepository; // Necesario para validar curso

    // Método para registrar una nueva inscripción (POST)
    public Inscripcion registrarInscripcion(Inscripcion inscripcion) {
        // 1. Validar que el cuerpo no sea nulo
        if (inscripcion.getCursoId() == null || inscripcion.getEstudianteId() == null) {
            throw new ValidationException("Los campos cursoId y estudianteId son obligatorios.");
        }

        // 2. Validar que el curso exista en Oracle Cloud
        if (!cursoRepository.existsById(inscripcion.getCursoId())) {
            throw new ResourceNotFoundException("No se puede inscribir: El curso con ID " + inscripcion.getCursoId() + " no existe.");
        }

        // 3. Validar que el estudiante exista
        if (!usuarioRepository.existsById(inscripcion.getEstudianteId())) {
            throw new ResourceNotFoundException("No se puede inscribir: El usuario con ID " + inscripcion.getEstudianteId() + " no existe.");
        }

        // Seteamos la fecha actual automáticamente
        inscripcion.setFechaInscripcion(new Date());
        return inscripcionRepository.save(inscripcion);
    }

    // Consultar por curso
    public List<Inscripcion> listarPorCurso(Long cursoId) {
        // Validación extra: ¿Existe el curso antes de pedir la lista?
        if (!cursoRepository.existsById(cursoId)) {
            throw new ResourceNotFoundException("No se pueden listar inscripciones: El curso con ID " + cursoId + " no existe.");
        }
        return inscripcionRepository.findByCursoId(cursoId);
    }
    public void eliminarInscripcion(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: La inscripción con ID " + id + " no existe.");
        }
        inscripcionRepository.deleteById(id);
    }
}