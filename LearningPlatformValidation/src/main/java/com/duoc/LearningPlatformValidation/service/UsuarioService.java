package com.duoc.LearningPlatformValidation.service;

import com.duoc.LearningPlatformValidation.model.Usuario;
import com.duoc.LearningPlatformValidation.repository.UsuarioRepository;
import com.duoc.LearningPlatformValidation.exceptions.ResourceNotFoundException;
import com.duoc.LearningPlatformValidation.exceptions.EmailAlreadyExistsException;
import com.duoc.LearningPlatformValidation.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado."));
    }

    public Usuario guardar(Usuario usuario) {
        // Validar que el correo no sea nulo/vacío
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new ValidationException("El correo electrónico es obligatorio para el registro.");
        }

        // Validar si el correo ya existe en la base de datos
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new EmailAlreadyExistsException("El correo " + usuario.getCorreo() + " ya está registrado.");
        }
        
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar: Usuario con ID " + id + " no existe.");
        }
        usuarioRepository.deleteById(id);
    }
    public Usuario actualizar(Long id, Usuario usuarioExistente) {
    Usuario u = buscarPorId(id); // Reutiliza el método que ya lanza ResourceNotFoundException
    u.setNombre(usuarioExistente.getNombre());
    u.setCorreo(usuarioExistente.getCorreo());
    u.setContrasena(usuarioExistente.getContrasena());
    u.setRol(usuarioExistente.getRol());
    return usuarioRepository.save(u);
   }
}