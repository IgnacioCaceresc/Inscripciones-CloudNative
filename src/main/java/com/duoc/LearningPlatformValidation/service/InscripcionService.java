package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.dto.InscripcionDTO;
import com.duoc.LearningPlatformValidation.model.Curso;
import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.model.Usuario;
import com.duoc.LearningPlatformValidation.repository.InscripcionRepository;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;
import com.duoc.LearningPlatformValidation.exceptions.ResourceNotFoundException;
import com.duoc.LearningPlatformValidation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    public Inscripcion registrarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getCursoId() == null || inscripcion.getEstudianteId() == null) {
            throw new ValidationException("Los campos cursoId y estudianteId son obligatorios.");
        }
        if (!cursoRepository.existsById(inscripcion.getCursoId())) {
            throw new ResourceNotFoundException("No se puede inscribir: El curso con ID " + inscripcion.getCursoId() + " no existe.");
        }
        if (!usuarioRepository.existsById(inscripcion.getEstudianteId())) {
            throw new ResourceNotFoundException("No se puede inscribir: El usuario con ID " + inscripcion.getEstudianteId() + " no existe.");
        }
        inscripcion.setFechaInscripcion(new Date());
        return inscripcionRepository.save(inscripcion);
    }

    public List<InscripcionDTO> listarTodasConDetalle() {
        return inscripcionRepository.findAll().stream().map(i -> {
            String nombreCurso = cursoRepository.findById(i.getCursoId())
                .map(Curso::getNombre).orElse("Curso no encontrado");
            String nombreEstudiante = usuarioRepository.findById(i.getEstudianteId())
                .map(Usuario::getNombre).orElse("Estudiante no encontrado");
            return new InscripcionDTO(
                i.getId(),
                i.getCursoId(),
                nombreCurso,
                i.getEstudianteId(),
                nombreEstudiante,
                i.getFechaInscripcion()
            );
        }).collect(Collectors.toList());
    }

    public List<Inscripcion> listarPorCurso(Long cursoId) {
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