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

import SNAKE_PC.demo.model.producto.Dimension;
import SNAKE_PC.demo.service.producto.DimensionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/dimensiones")
@Tag(name = "Dimensiones", description = "Gestión de dimensiones de productos")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    @GetMapping
    @Operation(summary = "Obtener todas las dimensiones", description = "Obtiene la lista de todas las dimensiones registradas")
    public ResponseEntity<List<Dimension>> listar() {
        List<Dimension> dimensiones = dimensionService.buscarTodos();
        if (dimensiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dimensiones);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dimensión por ID", description = "Obtiene una dimensión específica por su ID")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            Dimension dimension = dimensionService.buscarPorId(id);
            return ResponseEntity.ok(dimension);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva dimensión", description = "Crea una nueva dimensión en el sistema")
    public ResponseEntity<?> crear(@RequestBody Dimension dimension) {
        try {
            if (dimension == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La dimensión no puede ser nula");
            }
            if (dimension.getLargo() == null || dimension.getLargo().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El largo es obligatorio");
            }
            if (dimension.getAncho() == null || dimension.getAncho().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ancho es obligatorio");
            }
            if (dimension.getAlto() == null || dimension.getAlto().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El alto es obligatorio");
            }
            Dimension nuevaDimension = dimensionService.guardarDimension(dimension);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDimension);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dimensión", description = "Actualiza los datos de una dimensión existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Dimension dimension) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            if (dimension == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La dimensión no puede ser nula");
            }
            Dimension dimensionActualizada = dimensionService.actualizarDimension(id, dimension);
            return ResponseEntity.ok(dimensionActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dimensión", description = "Elimina una dimensión del sistema")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            dimensionService.eliminarDimension(id);
            return ResponseEntity.ok("Dimensión eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
