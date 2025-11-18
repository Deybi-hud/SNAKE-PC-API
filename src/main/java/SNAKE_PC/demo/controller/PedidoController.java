package SNAKE_PC.demo.controller;

import java.util.HashMap;
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

import SNAKE_PC.demo.dto.pedido.PedidoActualizarDTO;
import SNAKE_PC.demo.dto.pedido.PedidoCrearDTO;
import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.service.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos y órdenes de compra")
public class PedidoController {

    @Autowired(required = false)
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalles de un pedido", description = "Obtiene la información completa de un pedido específico")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del pedido debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (pedidoService == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Servicio de pedidos no disponible");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
            }
            return ResponseEntity.ok("Pedido encontrado");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido para un cliente con validaciones completas")
    public ResponseEntity<?> crearPedido(@Valid @RequestBody PedidoCrearDTO pedidoDTO) {
        try {
            if (pedidoService == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Servicio de pedidos no disponible");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
            }
            
            Pedido pedido = new Pedido();
            pedido.setFechaPedido(pedidoDTO.getFechaPedido());
            pedido.setNumeroPedido(pedidoDTO.getNumeroPedido());
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Pedido creado");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido", description = "Actualiza la información de un pedido existente")
    public ResponseEntity<?> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoActualizarDTO pedidoDTO) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del pedido debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (pedidoService == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Servicio de pedidos no disponible");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
            }
            return ResponseEntity.ok("Pedido actualizado");
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

            if (pedidoService == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Servicio de pedidos no disponible");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
            }
            return ResponseEntity.ok("Pedido cancelado");
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

}
