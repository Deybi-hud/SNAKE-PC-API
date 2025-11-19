package SNAKE_PC.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.service.producto.EspecificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/especificaciones")
@Tag(name = "Especificaciones", description = "Gestión de especificaciones técnicas de productos")
public class EspecificacionController {

    @Autowired
    private EspecificacionService especificacionService;

    @GetMapping
    @Operation(summary = "Obtener todas las especificaciones", description = "Obtiene la lista de todas las especificaciones registradas")
    public ResponseEntity<List<Especificacion>> listar() {
        List<Especificacion> especificaciones = especificacionService.buscarTodos();
        if (especificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(especificaciones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener especificación por ID", description = "Obtiene una especificación específica por su ID")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            Especificacion especificacion = especificacionService.buscarPorId(id);
            return ResponseEntity.ok(especificacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva especificación", description = "Crea una nueva especificación en el sistema")
    public ResponseEntity<?> crear(@RequestBody Especificacion especificacion) {
        try {
            if (especificacion == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La especificación no puede ser nula");
            }
            if (especificacion.getFrecuencia() == null || especificacion.getFrecuencia().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La frecuencia es obligatoria");
            }
            Especificacion nuevaEspecificacion = especificacionService.guardarEspecificacion(especificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEspecificacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar especificación", description = "Actualiza los datos de una especificación existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Especificacion especificacion) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            if (especificacion == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La especificación no puede ser nula");
            }
            Especificacion especificacionActualizada = especificacionService.actualizarEspecificacion(id, especificacion);
            return ResponseEntity.ok(especificacionActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar especificación", description = "Elimina una especificación del sistema")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            especificacionService.eliminarEspecificacion(id);
            return ResponseEntity.ok("Especificación eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
