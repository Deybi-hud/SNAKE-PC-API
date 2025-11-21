package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.service.pedido.PedidoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente/pedidos")
public class PedidoClienteController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody Map<Long, Integer> productosYCantidades,
            Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Pedido pedido = pedidoService.crearPedido(productosYCantidades, correoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (RuntimeException e) {
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
            Pago pago = pedidoService.crearPago(pedidoId, correoUsuario, metodoPagoId);
            return ResponseEntity.ok(pago);
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerMisPedidos(Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(correoUsuario);
            return ResponseEntity.ok(pedidos);
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pedidoId}/total")
    public ResponseEntity<?> calcularTotal(@PathVariable Long pedidoId) {
        try {
            Double total = pedidoService.calcularTotalPedido(pedidoId);
            return ResponseEntity.ok(Map.of("total", total));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}