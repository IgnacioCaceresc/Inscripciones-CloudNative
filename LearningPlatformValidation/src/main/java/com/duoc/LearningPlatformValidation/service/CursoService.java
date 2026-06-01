package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Curso;
import com.duoc.LearningPlatformValidation.repository.CursoRepository;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;
import com.duoc.LearningPlatformValidation.exceptions.ResourceNotFoundException;
import com.duoc.LearningPlatformValidation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; 

    public Curso guardarCurso(Curso curso) {
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty()) {
            throw new ValidationException("El nombre del curso es obligatorio.");
        }

        if (curso.getProfesorId() != null && !usuarioRepository.existsById(curso.getProfesorId())) {
            throw new ResourceNotFoundException(
                    "No se puede crear el curso: El profesor con ID " + curso.getProfesorId() + " no existe.");
        }

        return cursoRepository.save(curso);
    }

    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso con ID " + id + " no encontrado."));
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public void eliminarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: El curso con ID " + id + " no existe.");
        }
        cursoRepository.deleteById(id);
    }

    public Curso actualizar(Long id, Curso cursoExistente) {
        Curso c = buscarPorId(id);

        if (cursoExistente.getProfesorId() != null && !usuarioRepository.existsById(cursoExistente.getProfesorId())) {
            throw new ResourceNotFoundException(
                    "No se puede actualizar: El profesor con ID " + cursoExistente.getProfesorId() + " no existe.");
        }

        c.setNombre(cursoExistente.getNombre());
        c.setDescripcion(cursoExistente.getDescripcion());
        c.setProfesorId(cursoExistente.getProfesorId());

        return cursoRepository.save(c);
    }
}