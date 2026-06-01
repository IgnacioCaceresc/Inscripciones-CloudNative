package com.duoc.LearningPlatformValidation.controller;

import com.duoc.LearningPlatformValidation.model.Evaluacion;
import com.duoc.LearningPlatformValidation.service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<Evaluacion> crear(@RequestBody Evaluacion evaluacion) {
        return new ResponseEntity<>(evaluacionService.crearEvaluacion(evaluacion), HttpStatus.CREATED);
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Evaluacion>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(evaluacionService.listarPorCurso(cursoId));
    }
}