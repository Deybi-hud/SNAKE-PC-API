package SNAKE_PC.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;
import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.service.pedido.MetodoEnvioService;
import SNAKE_PC.demo.service.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/pedidos")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener pedidos: " + e.getMessage()));
        }
    }

    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Long pedidoId,
            @RequestParam String estado) {
        try {
            Pedido pedido = pedidoService.actualizarEstadoPedido(pedidoId, estado);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ======================== CRUD METODO ENVIO ========================

    @PostMapping("/metodo-envio")
    @Operation(summary = "Crear método de envío", description = "Crea un nuevo método de envío")
    public ResponseEntity<?> crearMetodoEnvio(@RequestBody MetodoEnvio metodoEnvio) {
        try {
            MetodoEnvio nuevoMetodo = metodoEnvioService.crearMetodoEnvio(metodoEnvio);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMetodo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}