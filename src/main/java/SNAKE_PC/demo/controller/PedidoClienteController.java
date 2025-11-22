package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.service.pedido.PedidoService;
import SNAKE_PC.demo.repository.pedido.PedidoRepository;
import SNAKE_PC.demo.repository.pedido.PagoRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cliente/pedidos")
public class PedidoClienteController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody Map<Long, Object> pedidoRequest,
            Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();

            @SuppressWarnings("unchecked")
            Map<Long, Integer> productosYCantidades = (Map<Long, Integer>) pedidoRequest.get("productos");

            @SuppressWarnings("unchecked")
            Map<Long, Long> metodosEnvio = (Map<Long, Long>) pedidoRequest.get("metodosEnvio");

            if (productosYCantidades == null || metodosEnvio == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Se requieren 'productos' y 'metodosEnvio' en el request"));
            }

            Pedido pedido = pedidoService.crearPedido(productosYCantidades, metodosEnvio, correoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{pedidoId}/pagar")
    public ResponseEntity<?> pagarPedido(
            @PathVariable Long pedidoId,
            @RequestParam Long metodoPagoId,
            Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);

            if (!pedidoOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Pedido pedido = pedidoOpt.get();

            // Validar que el pedido pertenece al usuario autenticado
            if (!pedido.getContacto().getUsuario().getCorreo().equals(correoUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para pagar este pedido"));
            }

            // Buscar pagos existentes del pedido
            List<Pago> pagosExistentes = pagoRepository.findByPedidoId(pedidoId);

            if (!pagosExistentes.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Este pedido ya ha sido pagado"));
            }

            return ResponseEntity.ok(Map.of("mensaje", "Procedimiento de pago iniciado para pedido: " + pedidoId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{pedidoId}/cancelar")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Long pedidoId,
            Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Pedido pedido = pedidoService.cancelarPedido(pedidoId, correoUsuario);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerMisPedidos(Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(correoUsuario);
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<?> obtenerMiPedido(
            @PathVariable Long pedidoId,
            Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Pedido pedido = pedidoService.obtenerPedidoPorId(pedidoId, correoUsuario);
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pedidoId}/total")
    public ResponseEntity<?> calcularTotal(@PathVariable Long pedidoId) {
        try {
            java.math.BigDecimal total = pedidoService.calcularTotalPedido(pedidoId);
            return ResponseEntity.ok(Map.of("total", total));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}