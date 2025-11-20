package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SNAKE_PC.demo.model.pedido.*;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.pedido.*;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import SNAKE_PC.demo.repository.producto.ProductoRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetalleRepository detallePedidoRepository;
    
    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;
    
    @Autowired
    private ContactoRepository contactoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    @Autowired
    private PagoRepository pagoRepository;

    // ✅ CREAR PEDIDO
    @Transactional
    public Pedido crearPedido(Map<Long, Integer> productosYCantidades, String correoUsuarioLogueado) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuarioLogueado)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!usuario.isActivo()) {
            throw new RuntimeException("Usuario desactivado, no puede crear pedidos");
        }
        
        List<Contacto> contactos = contactoRepository.findByUsuario(usuario);
        if (contactos.isEmpty()) {
            throw new RuntimeException("No se encontró un contacto para el usuario");
        }
        
        Contacto contacto = contactos.get(0);
        EstadoPedido estadoPendiente = estadoPedidoRepository.findByNombre("PENDIENTE")
            .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no configurado"));
        
        if (productosYCantidades == null || productosYCantidades.isEmpty()) {
            throw new RuntimeException("Debe agregar productos al pedido");
        }
        
        Pedido pedido = new Pedido();
        pedido.setFechaPedido(LocalDate.now());
        pedido.setNumeroPedido(generarNumeroPedido());
        pedido.setContacto(contacto);
        pedido.setEstado(estadoPendiente);
        
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        for (Map.Entry<Long, Integer> entry : productosYCantidades.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();
            
            if (cantidad <= 0) {
                throw new RuntimeException("La cantidad debe ser mayor a 0");
            }
            
            Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
            
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombreProducto());
            }
            
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
            
            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(producto);
            
            detallePedidoRepository.save(detalle);
        }
        
        return pedidoGuardado;
    }

    // ✅ CREAR PAGO PARA PEDIDO
    @Transactional
    public Pago crearPago(Long pedidoId, String correoUsuario, Long metodoPagoId) {
        // 1. Validar pedido y pertenencia
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        if (!pedido.getContacto().getUsuario().getCorreo().equals(correoUsuario)) {
            throw new RuntimeException("No tiene permisos para pagar este pedido");
        }
        
        // 2. Validar estado del pedido
        if (!pedido.getEstado().getNombre().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden pagar pedidos en estado PENDIENTE");
        }
        
        // 3. Validar método de pago
        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
            .orElseThrow(() -> new RuntimeException("Método de pago no válido"));
        
        // 4. Calcular total
        Double totalPedido = calcularTotalPedido(pedidoId);
        
        // 5. Crear pago
        Pago pago = new Pago();
        pago.setMonto(totalPedido);
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("PAGADO");
        pago.setMetodoPago(metodoPago);
        pago.setPedido(pedido);
        
        Pago pagoCreado = pagoRepository.save(pago);
        
        // 6. Actualizar estado del pedido
        actualizarEstadoPedido(pedidoId, "CONFIRMADO");
        
        return pagoCreado;
    }

    // ✅ CALCULAR TOTAL DEL PEDIDO
    public Double calcularTotalPedido(Long pedidoId) {
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoId);
        
        return detalles.stream()
            .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
            .sum();
    }

    // ✅ OBTENER PAGOS DEL USUARIO
    public List<Pago> obtenerPagosPorUsuario(String correoUsuario) {
        return pagoRepository.findByPedidoContactoUsuarioCorreo(correoUsuario);
    }

    // ✅ CANCELAR PEDIDO
    @Transactional
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

    // ✅ ACTUALIZAR ESTADO DEL PEDIDO
    @Transactional
    public Pedido actualizarEstadoPedido(Long pedidoId, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        EstadoPedido estado = estadoPedidoRepository.findByNombre(nuevoEstado.toUpperCase())
            .orElseThrow(() -> new RuntimeException("Estado no válido: " + nuevoEstado));
        
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    // ✅ MÉTODOS DE CONSULTA
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

    // ✅ PEDIDOS DEL MES ACTUAL
    public List<Pedido> obtenerPedidosDelMes() {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);
        LocalDate finMes = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return pedidoRepository.findByFechaPedidoBetween(inicioMes, finMes);
    }

    // ✅ PEDIDOS POR RANGO DE FECHAS
    public List<Pedido> obtenerPedidosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin);
    }

    // ✅ ESTADÍSTICAS DEL DÍA
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

    // ✅ ESTADÍSTICAS DEL MES
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


    // ✅ MÉTODOS PRIVADOS
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