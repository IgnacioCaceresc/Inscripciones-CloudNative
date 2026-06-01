package com.duoc.LearningPlatformValidation.controller;

import com.duoc.LearningPlatformValidation.model.Curso;
import com.duoc.LearningPlatformValidation.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    // GET /api/cursos Devuelve 200 OK
    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listarTodos());
    }

    // GET /api/cursos/{id}  Devuelve 200 OK 
    @GetMapping("/{id}")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }

    // POST /api/cursos  Devuelve 201 CREATED 
    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
        return new ResponseEntity<>(cursoService.guardarCurso(curso), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable Long id, @RequestBody Curso cursoDetalles) {
        
        Curso cursoActualizado = cursoService.actualizar(id, cursoDetalles); 
        
        if (cursoActualizado != null) {
            return ResponseEntity.ok(cursoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/cursos/{id}  Devuelve 204 NO CONTENT (
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.noContent().build();
    }
}