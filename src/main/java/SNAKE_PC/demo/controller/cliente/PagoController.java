package SNAKE_PC.demo.controller.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.pedido.MetodoPago;
import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.pedido.MetodoPagoRepository;
import SNAKE_PC.demo.repository.pedido.PagoRepository;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cliente/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @GetMapping
    @Operation(summary = "Obtener mis pagos", description = "Retorna todos los pagos realizados por el usuario autenticado")
    public ResponseEntity<?> obtenerMisPagos(@AuthenticationPrincipal Usuario usuarioLogueado) {
        try {
            if (usuarioLogueado == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
            }

            List<Pago> pagos = pagoRepository.findByPedidoUsuarioCorreo(usuarioLogueado.getCorreo());
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/metodos-disponibles")
    @Operation(summary = "Obtener métodos de pago disponibles", description = "Retorna una lista de todos los métodos de pago disponibles")
    public ResponseEntity<?> obtenerMetodosDisponibles() {
        try {
            List<MetodoPago> metodos = metodoPagoRepository.findAll();
            return ResponseEntity.ok(metodos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}