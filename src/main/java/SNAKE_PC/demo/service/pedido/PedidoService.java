package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SNAKE_PC.demo.model.pedido.*;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.pedido.*;
import SNAKE_PC.demo.repository.producto.ProductoRepository;
import SNAKE_PC.demo.service.producto.ProductoService;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired 
    private ProductoService productoService;

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    @Autowired 
    private DetalleRepository detalleRepository;
 
    @Autowired 
    private ProductoRepository productoRepository;


    public Pedido crearPedido(Map<Long, Integer> productosYCantidades,Map<Long, Long> metodosEnvio, String correoUsuario) {
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
        pedido.setDetalles(new ArrayList<>());
       
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
            Long metodoEnvioId = metodosEnvio.get(productoId);
            if (metodoEnvioId == null) {
                throw new RuntimeException("Falta método de envío para producto ID: " + productoId);
            }
            DetallePedido detalle = detallePedidoService.crearDetalle(
                productoId, cantidad, pedido, metodoEnvioId
            );
            pedido.getDetalles().add(detalle);
        }
        pedido = pedidoRepository.save(pedido);
        for(Map.Entry<Long,Integer> entry : productosYCantidades.entrySet()){
            productoService.actualizarStock(entry.getKey(),entry.getValue());
        }

        pedido.setTotal(calcularTotalPedido(pedido.getId()));
        return pedidoRepository.save(pedido);
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
        Object[] resultado = pedidoRepository.estadisticasDelDiaNative(); // o estadisticasDelDiaNative()

        Long totalPedidos = (Long) resultado[0];
        BigDecimal totalVentas = (BigDecimal) resultado[1];

        Map<String, Object> stats = new HashMap<>();
        stats.put("fecha", LocalDate.now());
        stats.put("totalPedidos", totalPedidos);
        stats.put("totalVentas", totalVentas);
        stats.put("totalVentasFormateado", formatearPesos(totalVentas));

        return stats;
    }

    public Map<String, Object> obtenerEstadisticasDelMes() {
        Object[] resultado = pedidoRepository.estadisticasDelMesNative(); // o estadisticasDelMesNative()

        Long totalPedidos = (Long) resultado[0];
        BigDecimal totalVentas = (BigDecimal) resultado[1];

        Map<String, Object> stats = new HashMap<>();
        stats.put("mes", LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "CL")).toUpperCase());
        stats.put("anio", LocalDate.now().getYear());
        stats.put("totalPedidos", totalPedidos);
        stats.put("totalVentas", totalVentas);
        stats.put("totalVentasFormateado", formatearPesos(totalVentas));

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
            Producto p = detalle.getProducto();
            p.setStock(p.getStock() + detalle.getCantidad());
            productoRepository.save(p);
        });
    }

    public BigDecimal calcularTotalPedido(Long pedidoId) {
        if (pedidoId == null) {
            return BigDecimal.ZERO;
        }
        List<DetallePedido> detalles = detalleRepository.findByPedidoId(pedidoId);
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido d : detalles) {
            BigDecimal precio = d.getPrecioUnitario() != null ? d.getPrecioUnitario() : BigDecimal.ZERO;
            BigDecimal cantidad = BigDecimal.valueOf(d.getCantidad());
            BigDecimal subtotalProductos = precio.multiply(cantidad);

            BigDecimal costoEnvio = BigDecimal.ZERO;
            if (d.getMetodoEnvio() != null && d.getMetodoEnvio().getCostoEnvio() != null) {
                costoEnvio = d.getMetodoEnvio().getCostoEnvio();
            }
            total = total.add(subtotalProductos).add(costoEnvio);
        }

        return total;
    }

    private String formatearPesos(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) == 0) {
            return "$ 0";
        }
        DecimalFormat df = new DecimalFormat("$ #,###");
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(new Locale("es", "CL")));
        
        return df.format(monto.longValue());
    }


}