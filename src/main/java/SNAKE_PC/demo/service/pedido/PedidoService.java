package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import SNAKE_PC.demo.model.pedido.DetallePedido;
import SNAKE_PC.demo.model.pedido.EstadoPedido;
import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.pedido.DetalleRepository;
import SNAKE_PC.demo.repository.pedido.PedidoRepository;
import SNAKE_PC.demo.repository.producto.ProductoRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import SNAKE_PC.demo.service.producto.ProductoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoRepository pedidoRepository;


    @Autowired
    private ProductoService productoService;

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    @Autowired
    private DetalleRepository detalleRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Pedido crearPedido(Pedido pedido, Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getContacto() == null) {
            throw new RuntimeException("Completa tus datos de contacto antes de comprar");
        }
        pedido.setUsuario(usuario);
        pedido.setEstado(estadoPedidoService.obtenerEstadoPendiente());
        pedido.setFechaPedido(LocalDate.now());
        pedido.setNumeroPedido(generarNumeroPedido());
        
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("Agrega al menos un producto");
        }
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = productoService.buscarPorId(detalle.getProducto().getId());
            
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Sin stock para: " + producto.getNombreProducto());
            }

            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
        }
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        pedido.getDetalles().forEach(detalle -> 
            productoService.actualizarStock(detalle.getProducto().getId(), detalle.getCantidad())
        );
        BigDecimal total = pedido.getDetalles().stream()
            .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        pedidoGuardado.setTotal(total);

        return pedidoRepository.save(pedidoGuardado);
    }

    public Pedido cancelarPedido(Long pedidoId, String correoUsuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getCorreo().equals(correoUsuario)) {
            throw new RuntimeException("No tiene permisos para cancelar este pedido");
        }

        if (!pedido.getEstado().getNombre().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden cancelar pedidos en estado PENDIENTE");
        }

        EstadoPedido estadoCancelado = estadoPedidoService.obtenerEstadoCancelado();
        devolverStock(pedidoId);
        pedido.setEstado(estadoCancelado);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        EstadoPedido estado = estadoPedidoService.obtenerPorNombre(nuevoEstado.toUpperCase());
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerPedidosPorUsuario(Long idUsuario) {
        return pedidoRepository.findByUsuarioId(idUsuario);
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAllWithDetails();
    }

    public Pedido obtenerPedidoPorId(Long pedidoId, String correoUsuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getUsuario().getCorreo().equals(correoUsuario) && 
            !usuarioService.obtenerPorCorreo(correoUsuario).getRolUsuario().getNombreRol().equals("ADMIN")) {
            throw new RuntimeException("No tiene permisos para ver este pedido");
        }

        return pedido;
    }

    public List<Pedido> obtenerPedidosDelDia() {
        LocalDate hoy = LocalDate.now();
        return pedidoRepository.findByFechaPedido(hoy);
    }

    public List<Pedido> obtenerPedidosDelMes() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return pedidoRepository.findByFechaPedidoBetween(inicioMes, finMes);
    }

    public List<Pedido> obtenerPedidosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin);
    }

    public Map<String, Object> obtenerEstadisticasDelDia() {
        Object[] resultado = pedidoRepository.estadisticasDelDiaNative();

        Long totalPedidos = resultado[0] != null ? ((Number) resultado[0]).longValue() : 0L;
        BigDecimal totalVentas = resultado[1] != null ? (BigDecimal) resultado[1] : BigDecimal.ZERO;

        Map<String, Object> stats = new HashMap<>();
        stats.put("fecha", LocalDate.now());
        stats.put("totalPedidos", totalPedidos);
        stats.put("totalVentas", totalVentas);
        stats.put("totalVentasFormateado", "$ " + totalVentas);

        return stats;
    }

    public Map<String, Object> obtenerEstadisticasDelMes() {
        Object[] resultado = pedidoRepository.estadisticasDelMesNative();

        Long totalPedidos = resultado[0] != null ? ((Number) resultado[0]).longValue() : 0L;
        BigDecimal totalVentas = resultado[1] != null ? (BigDecimal) resultado[1] : BigDecimal.ZERO;

        Map<String, Object> stats = new HashMap<>();
        stats.put("mes", LocalDate.now().getMonth().toString());
        stats.put("anio", LocalDate.now().getYear());
        stats.put("totalPedidos", totalPedidos);
        stats.put("totalVentas", totalVentas);
        stats.put("totalVentasFormateado", "$ " + totalVentas);

        return stats;
    }

    private String generarNumeroPedido() {
        String numeroPedido;
        do {
            numeroPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (pedidoRepository.findByNumeroPedido(numeroPedido).isPresent());
        return numeroPedido;
    }

    private void devolverStock(Long pedidoId) {
        detalleRepository.findByPedidoId(pedidoId).forEach(detalle -> {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        });
    }

    public BigDecimal calcularTotalPedido(Long pedidoId) {
        List<DetallePedido> detalles = detalleRepository.findByPedidoId(pedidoId);
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : detalles) {
            BigDecimal precio = detalle.getPrecioUnitario() != null ? detalle.getPrecioUnitario() : BigDecimal.ZERO;
            BigDecimal cantidad = BigDecimal.valueOf(detalle.getCantidad());
            BigDecimal subtotalProductos = precio.multiply(cantidad);

            BigDecimal costoEnvio = BigDecimal.ZERO;
            if (detalle.getMetodoEnvio() != null && detalle.getMetodoEnvio().getCostoEnvio() != null) {
                costoEnvio = detalle.getMetodoEnvio().getCostoEnvio();
            }

            total = total.add(subtotalProductos).add(costoEnvio);
        }

        return total;
    }
}