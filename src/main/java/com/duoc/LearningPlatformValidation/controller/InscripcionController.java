package com.duoc.LearningPlatformValidation.controller;

import com.duoc.LearningPlatformValidation.dto.InscripcionDTO;
import com.duoc.LearningPlatformValidation.model.Inscripcion;
import com.duoc.LearningPlatformValidation.service.InscripcionService;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @PostMapping
    public ResponseEntity<Inscripcion> inscribir(@RequestBody Inscripcion inscripcion) {
        Inscripcion nueva = inscripcionService.registrarInscripcion(inscripcion);
        String contenidoResumen = "RESUMEN DE INSCRIPCIÓN\n" +
                                  "----------------------\n" +
                                  "ID Inscripción: " + nueva.getId() + "\n" +
                                  "ID Estudiante: " + nueva.getEstudianteId() + "\n" +
                                  "ID Curso: " + nueva.getCursoId() + "\n" +
                                  "Fecha: " + nueva.getFechaInscripcion();
        String rutaS3 = nueva.getId() + "/resumen.txt";
        s3Template.upload(bucketName, rutaS3, new ByteArrayInputStream(contenidoResumen.getBytes()));
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> listarTodas() {
        return ResponseEntity.ok(inscripcionService.listarTodasConDetalle());
    }

    @GetMapping("/s3/descargar/{id}")
    public ResponseEntity<byte[]> descargarResumen(@PathVariable String id) throws IOException {
        String rutaS3 = id + "/resumen.txt";
        S3Resource recurso = s3Template.download(bucketName, rutaS3);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header("Content-Disposition", "attachment; filename=\"resumen_" + id + ".txt\"")
                .body(recurso.getContentAsByteArray());
    }

    @PutMapping("/s3/actualizar/{id}")
    public ResponseEntity<String> actualizarResumen(@PathVariable String id, @RequestBody String nuevoContenido) {
        String rutaS3 = id + "/resumen.txt";
        s3Template.upload(bucketName, rutaS3, new ByteArrayInputStream(nuevoContenido.getBytes()));
        return ResponseEntity.ok("Archivo en S3 actualizado correctamente");
    }

    @DeleteMapping("/s3/borrar/{id}")
    public ResponseEntity<Void> eliminarS3(@PathVariable String id) {
        String rutaS3 = id + "/resumen.txt";
        s3Template.deleteObject(bucketName, rutaS3);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Inscripcion>> listarPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(inscripcionService.listarPorCurso(cursoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}