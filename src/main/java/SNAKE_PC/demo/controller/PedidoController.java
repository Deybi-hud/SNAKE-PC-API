package SNAKE_PC.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.service.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos", description = "Obtiene la lista de todos los pedidos registrados")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.findAll();
        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de un pedido", description = "Obtiene la información de un pedido específico")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del pedido debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Pedido pedido = pedidoService.findById(id);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido para un cliente")
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            if (pedido == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El pedido no puede ser nulo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Pedido nuevoPedido = pedidoService.savePedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido", description = "Actualiza la información de un pedido")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del pedido debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (pedido == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El pedido no puede ser nulo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Pedido pedidoActualizado = pedidoService.updatePedido(id, pedido);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar un pedido", description = "Cancela un pedido existente")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del pedido debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            pedidoService.deletePedido(id);
            return ResponseEntity.ok("Pedido cancelado correctamente");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}
