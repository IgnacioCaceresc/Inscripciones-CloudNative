package main.java.com.duoc.LearningPlatformValidation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InscripcionDTO {
    private Long id;
    private Long cursoId;
    private String nombreCurso;
    private Long estudianteId;
    private String nombreEstudiante;
    private Date fechaInscripcion;
}