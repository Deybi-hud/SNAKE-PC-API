package SNAKE_PC.demo.controller.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;
import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.pedido.MetodoEnvioService;
import SNAKE_PC.demo.service.pedido.PagoService;
import SNAKE_PC.demo.service.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import SNAKE_PC.demo.repository.pedido.PedidoRepository;
import SNAKE_PC.demo.repository.pedido.PagoRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cliente/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PagoService pagoService;

    @Autowired
    private MetodoEnvioService metodoEnvioService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @PostMapping
    @Operation(summary = "Crear pedido", description = "Crea un nuevo pedido para el usuario autenticado. Requiere estar logueado")
    public ResponseEntity<?> crearPedido(
            @Valid @RequestBody Pedido pedido,
            @AuthenticationPrincipal Usuario usuarioLogueado) {

        if (usuarioLogueado == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
        }

        if (pedido == null || pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debes agregar al menos un producto"));
        }

        try {
            Pedido creado = pedidoService.crearPedido(pedido, usuarioLogueado.getId());
            return ResponseEntity.status(201).body(Map.of(
                    "mensaje", "Pedido creado con éxito",
                    "pedido", creado.getNumeroPedido(),
                    "id", creado.getId(),
                    "total", creado.getTotal()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{pedidoId}/pagar")
    @Operation(summary = "Pagar pedido", description = "Inicia el procedimiento de pago para un pedido. Requiere estar logueado. Enviar metodoPagoId como query param")
    public ResponseEntity<?> pagarPedido(
            @PathVariable Long pedidoId,
            @RequestParam(required = false) Long metodoPagoId,
            @AuthenticationPrincipal Usuario usuarioLogueado) {
        try {
            if (usuarioLogueado == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
            }

            if (metodoPagoId == null || metodoPagoId <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Debes seleccionar un método de pago"));
            }

            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (!pedido.getUsuario().getId().equals(usuarioLogueado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para pagar este pedido"));
            }

            List<Pago> pagosExistentes = pagoRepository.findByPedidoId(pedidoId);

            if (!pagosExistentes.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Este pedido ya ha sido pagado"));
            }

            Pago pagoCreado = pagoService.crearPago(pedidoId, usuarioLogueado.getCorreo(), metodoPagoId);
            return ResponseEntity.status(201).body(Map.of(
                    "mensaje", "Pago realizado exitosamente",
                    "idPago", pagoCreado.getId(),
                    "monto", pagoCreado.getMonto(),
                    "fechaPago", pagoCreado.getFechaPago()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{pedidoId}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela un pedido. Requiere estar logueado y ser propietario del pedido")
    public ResponseEntity<?> cancelarPedido(
            @PathVariable Long pedidoId,
            @AuthenticationPrincipal Usuario usuarioLogueado) {
        try {
            if (usuarioLogueado == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
            }

            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (!pedido.getUsuario().getId().equals(usuarioLogueado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para cancelar este pedido"));
            }

            Pedido cancelado = pedidoService.cancelarPedido(pedidoId, usuarioLogueado.getCorreo());
            return ResponseEntity.ok(cancelado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Obtener mis pedidos", description = "Retorna todos los pedidos del usuario autenticado. Requiere estar logueado")
    public ResponseEntity<?> obtenerMisPedidos(@AuthenticationPrincipal Usuario usuarioLogueado) {
        try {
            if (usuarioLogueado == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
            }

            List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(usuarioLogueado.getId());
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pedidoId}")
    @Operation(summary = "Obtener un pedido", description = "Retorna un pedido específico. Requiere estar logueado y ser propietario del pedido")
    public ResponseEntity<?> obtenerMiPedido(
            @PathVariable Long pedidoId,
            @AuthenticationPrincipal Usuario usuarioLogueado) {
        try {
            if (usuarioLogueado == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
            }

            Pedido pedido = pedidoService.obtenerPedidoPorId(pedidoId, usuarioLogueado.getCorreo());
            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{pedidoId}/total")
    @Operation(summary = "Calcular total del pedido", description = "Calcula el total de un pedido incluyendo costo de envío. Requiere estar logueado y ser propietario")
    public ResponseEntity<?> calcularTotal(
            @PathVariable Long pedidoId,
            @AuthenticationPrincipal Usuario usuarioLogueado) {
        try {
            if (usuarioLogueado == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado"));
            }

            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            if (!pedido.getUsuario().getId().equals(usuarioLogueado.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permiso para ver el total de este pedido"));
            }

            java.math.BigDecimal total = pedidoService.calcularTotalPedido(pedidoId);
            return ResponseEntity.ok(Map.of("total", total));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ======================== METODOS DE ENVIO ========================

    @GetMapping("/metodo-envio/todos")
    @Operation(summary = "Obtener todos los métodos de envío", description = "Retorna una lista de todos los métodos de envío disponibles")
    public ResponseEntity<?> obtenerTodosLosMetodos() {
        try {
            List<MetodoEnvio> metodos = metodoEnvioService.obtenerTodosLosMetodos();
            return ResponseEntity.ok(metodos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener métodos de envío: " + e.getMessage()));
        }
    }

    @GetMapping("/metodo-envio/activos")
    @Operation(summary = "Obtener métodos de envío activos", description = "Retorna solo los métodos de envío que están activos")
    public ResponseEntity<?> obtenerMetodosActivos() {
        try {
            List<MetodoEnvio> metodos = metodoEnvioService.obtenerMetodosActivos();
            return ResponseEntity.ok(metodos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener métodos activos: " + e.getMessage()));
        }
    }

    @GetMapping("/metodo-envio/buscar")
    @Operation(summary = "Buscar método de envío por nombre", description = "Busca un método de envío por su nombre")
    public ResponseEntity<?> buscarMetodoPorNombre(@RequestParam String nombre) {
        try {
            MetodoEnvio metodo = metodoEnvioService.obtenerPorNombre(nombre);
            return ResponseEntity.ok(metodo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/metodo-envio/{id}")
    @Operation(summary = "Obtener método de envío por ID", description = "Obtiene un método de envío específico por su ID")
    public ResponseEntity<?> obtenerMetodoPorId(@PathVariable Integer id) {
        try {
            MetodoEnvio metodo = metodoEnvioService.obtenerPorId(id);
            return ResponseEntity.ok(metodo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}