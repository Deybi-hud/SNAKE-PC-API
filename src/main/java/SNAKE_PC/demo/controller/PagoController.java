package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.service.pedido.PedidoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente/pagos")
public class PagoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<?> obtenerMisPagos(Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            List<Pago> pagos = pedidoService.obtenerPagosPorUsuario(correoUsuario);
            return ResponseEntity.ok(pagos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}