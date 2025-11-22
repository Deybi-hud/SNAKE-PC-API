package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.service.pedido.PedidoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/pedidos")
public class PedidoAdminController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<?> obtenerTodosLosPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerTodosLosPedidos();
            return ResponseEntity.ok(pedidos);
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/reportes/dia")
    public ResponseEntity<?> obtenerPedidosDelDia() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosDelDia();
            return ResponseEntity.ok(pedidos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reporte: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/mes")
    public ResponseEntity<?> obtenerPedidosDelMes() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosDelMes();
            return ResponseEntity.ok(pedidos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reporte: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/estadisticas-dia")
    public ResponseEntity<?> obtenerEstadisticasDelDia() {
        try {
            Map<String, Object> estadisticas = pedidoService.obtenerEstadisticasDelDia();
            return ResponseEntity.ok(estadisticas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/estadisticas-mes")
    public ResponseEntity<?> obtenerEstadisticasDelMes() {
        try {
            Map<String, Object> estadisticas = pedidoService.obtenerEstadisticasDelMes();
            return ResponseEntity.ok(estadisticas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/rango")
    public ResponseEntity<?> obtenerPedidosPorRango(
            @RequestParam String inicio,
            @RequestParam String fin) {
        try {
            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorRangoFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(pedidos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Formato de fecha inválido. Use YYYY-MM-DD"));
        }
    }
}