package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SNAKE_PC.demo.model.pedido.*;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.pedido.*;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import SNAKE_PC.demo.service.producto.ProductoService;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import SNAKE_PC.demo.repository.producto.ProductoRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PedidoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioContactoService  usuarioContactoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Autowired 
    private ProductoService productoService;

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    @Autowired 
    private PedidoRepository pedidoRepository;

    @Autowired 
    private PagoRepository pagoRepository;


    public Pedido crearPedido(Map<Long, Integer> productosYCantidades, String correoUsuario) {
        Usuario usuario = usuarioService.validarActividad(correoUsuario);
        Contacto contacto = usuarioContactoService.obtenerDatosContacto(usuario.getId());
        EstadoPedido pedidoEstado = estadoPedidoService.obtenerEstadoPendiente();
            
        if (productosYCantidades == null || productosYCantidades.isEmpty()) {
            throw new RuntimeException("Debe agregar productos al pedido");
        }
        
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.now());
        pedido.setNumeroPedido(generarNumeroPedido());
        pedido.setContacto(contacto);
        pedido.setEstado(pedidoEstado);
        pedidoRepository.save(pedido);

        for (Map.Entry<Long, Integer> entry : productosYCantidades.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            if (cantidad <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            Producto producto = productoService.buscarPorId(productoId);

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }
            productoService.actualizarStock(productoId, cantidad);


            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setPedido(pedido);
            detallePedido.setProducto(producto);
            detallePedido.setCantidad(cantidad);
            detallePedido.setPrecioUnitario(producto.getPrecio());

            DetallePedido detalleGuardado = detallePedidoService.crearDetalle(detallePedido);
            pedido.getDetalles().add(detalleGuardado);  
        }

        return pedido;
    }

    public Pago crearPago(Long pedidoId, String correoUsuario, MetodoPago metodoPago) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getContacto().getUsuario().getCorreo().equals(correoUsuario)) {
            throw new RuntimeException("No tiene permisos para pagar este pedido");
        }

        if (!pedido.getEstado().getNombre().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden pagar pedidos en estado PENDIENTE");
        }

        MetodoPago metodoPagoSeleccionado = metodoPagoService.buscarTipoPago(metodoPago);
               

        Double totalPedido = calcularTotalPedido(pedidoId);

        Pago pago = new Pago();
        pago.setMonto(totalPedido);
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("PAGADO");
        pago.setMetodoPago(metodoPagoSeleccionado);
        pago.setPedido(pedido);

        Pago pagoCreado = pagoRepository.save(pago);

        actualizarEstadoPedido(pedidoId, "CONFIRMADO");

        return pagoCreado;
    }


    public Pedido cancelarPedido(Long pedidoId, String correoUsuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getContacto().getUsuario().getCorreo().equals(correoUsuario)) {
            throw new RuntimeException("No tiene permisos para cancelar este pedido");
        }

        if (!pedido.getEstado().getNombre().equals("PENDIENTE") &&
                !pedido.getEstado().getNombre().equals("CONFIRMADO")) {
            throw new RuntimeException("No se puede cancelar un pedido en estado: " + pedido.getEstado().getNombre());
        }

        EstadoPedido estadoCancelado = estadoPedidoRepository.findByNombre("CANCELADO")
                .orElseThrow(() -> new RuntimeException("Estado CANCELADO no configurado"));

        devolverStock(pedidoId);
        pedido.setEstado(estadoCancelado);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        EstadoPedido estado = estadoPedidoRepository.findByNombre(nuevoEstado.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Estado no válido: " + nuevoEstado));

        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> obtenerPedidosPorUsuario(String correoUsuario) {
        return pedidoRepository.findByUsuarioCorreo(correoUsuario);
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return pedidoRepository.findAllWithDetails();
    }

    public Pedido obtenerPedidoPorId(Long pedidoId, String correoUsuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        if (!pedido.getContacto().getUsuario().getCorreo().equals(correoUsuario)) {
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
        List<Pedido> pedidosDelDia = obtenerPedidosDelDia();

        long totalPedidos = pedidosDelDia.size();
        double totalVentas = pedidosDelDia.stream()
                .mapToDouble(pedido -> calcularTotalPedido(pedido.getId()))
                .sum();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("fecha", LocalDate.now());
        estadisticas.put("totalPedidos", totalPedidos);
        estadisticas.put("totalVentas", totalVentas);
        estadisticas.put("pedidos", pedidosDelDia);

        return estadisticas;
    }

    public Map<String, Object> obtenerEstadisticasDelMes() {
        List<Pedido> pedidosDelMes = obtenerPedidosDelMes();

        long totalPedidos = pedidosDelMes.size();
        double totalVentas = pedidosDelMes.stream()
                .mapToDouble(pedido -> calcularTotalPedido(pedido.getId()))
                .sum();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("mes", LocalDate.now().getMonth().toString());
        estadisticas.put("anio", LocalDate.now().getYear());
        estadisticas.put("totalPedidos", totalPedidos);
        estadisticas.put("totalVentas", totalVentas);
        estadisticas.put("pedidos", pedidosDelMes);

        return estadisticas;
    }

    private String generarNumeroPedido() {
        String numeroPedido;
        do {
            numeroPedido = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (pedidoRepository.findByNumeroPedido(numeroPedido).isPresent());
        return numeroPedido;
    }

    private void devolverStock(Long pedidoId) {
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);
        for (DetallePedido detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
    }
}