package SNAKE_PC.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/historial")
@Tag(name = "Historial", description = "Gestión del historial de transacciones y actividades")
public class HistorialController {

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener historial de un usuario", description = "Obtiene el historial de transacciones de un usuario específico")
    public ResponseEntity<?> obtenerHistorialUsuario(@PathVariable Long usuarioId) {
        try {
            // Validar ID
            if (usuarioId == null || usuarioId <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del usuario debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Implementar cuando el servicio de historial esté disponible
            return ResponseEntity.ok("Historial del usuario " + usuarioId);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/pedidos/{usuarioId}")
    @Operation(summary = "Obtener historial de pedidos", description = "Obtiene todos los pedidos realizados por un usuario")
    public ResponseEntity<?> obtenerHistorialPedidos(@PathVariable Long usuarioId) {
        try {
            // Validar ID
            if (usuarioId == null || usuarioId <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del usuario debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Implementar cuando el servicio de historial esté disponible
            return ResponseEntity.ok("Historial de pedidos del usuario " + usuarioId);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/productos/{usuarioId}")
    @Operation(summary = "Obtener historial de productos visualizados", description = "Obtiene el historial de productos que ha visto un usuario")
    public ResponseEntity<?> obtenerHistorialProductos(@PathVariable Long usuarioId) {
        try {
            // Validar ID
            if (usuarioId == null || usuarioId <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del usuario debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Implementar cuando el servicio de historial esté disponible
            return ResponseEntity.ok("Historial de productos del usuario " + usuarioId);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}
