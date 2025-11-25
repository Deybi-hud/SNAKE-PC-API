package SNAKE_PC.demo.controller.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.service.pedido.PedidoService;

@RestController
@RequestMapping("/api/v1/admin/dashboards")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/reportes/dia")
    public ResponseEntity<?> obtenerPedidosDelDia() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosDelDia();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reporte: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/mes")
    public ResponseEntity<?> obtenerPedidosDelMes() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosDelMes();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reporte: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/estadisticas-dia")
    public ResponseEntity<?> obtenerEstadisticasDelDia() {
        try {
            Map<String, Object> estadisticas = pedidoService.obtenerEstadisticasDelDia();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener estadísticas: " + e.getMessage()));
        }
    }

    @GetMapping("/reportes/estadisticas-mes")
    public ResponseEntity<?> obtenerEstadisticasDelMes() {
        try {
            Map<String, Object> estadisticas = pedidoService.obtenerEstadisticasDelMes();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
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
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Formato de fecha inválido. Use YYYY-MM-DD"));
        }
    }

}
