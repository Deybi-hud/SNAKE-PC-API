package SNAKE_PC.demo.service.pedido;


import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.DetallePedido;
import SNAKE_PC.demo.model.pedido.MetodoEnvio;
import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.repository.pedido.DetalleRepository;
import SNAKE_PC.demo.service.producto.ProductoService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DetallePedidoService {

    @Autowired
    private DetalleRepository detalleRepository;

    @Autowired 
    private ProductoService productoService;

    @Autowired
    private MetodoEnvioService metodoEnvioService;
  
    
    public DetallePedido crearDetalle(Long productoId, Integer cantidad, Pedido pedido, Long IdMetodo){
        if(cantidad == null || cantidad <= 0){
            throw new RuntimeException("No se puede generar un detalle con 0 productos");
        }

        Producto producto = productoService.buscarPorId(productoId);
        MetodoEnvio metodoEnvio = metodoEnvioService.seleccionarMetodoEnvio(IdMetodo);

        if (producto.getStock() == null || producto.getStock() < cantidad) {
            throw new RuntimeException(
                "Stock insuficiente para '" + producto.getNombreProducto() + 
                "'. Disponible: " + producto.getStock() + 
                ", solicitado: " + cantidad
            );
        }

      DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);                                       
        detalle.setPrecioUnitario(producto.getPrecio());                     
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad))); 
        detalle.setPedido(pedido);
        detalle.setMetodoEnvio(metodoEnvio);
        
        DetallePedido guardado = detalleRepository.save(detalle);

        return guardado;
    }
    
    public BigDecimal calcularTotalPedido(Long pedidoId) {
        return detalleRepository.findByPedidoId(pedidoId).stream()
            .map(d -> {
                BigDecimal precio = Objects.requireNonNullElse(d.getPrecioUnitario(), BigDecimal.ZERO);
                BigDecimal cantidad = BigDecimal.valueOf(d.getCantidad());
                BigDecimal costoEnvio = d.getMetodoEnvio() != null 
                    ? (BigDecimal) Objects.requireNonNullElse(d.getMetodoEnvio().getCostoEnvio(), BigDecimal.ZERO)
                    : BigDecimal.ZERO;

                return precio.multiply(cantidad).add(costoEnvio);
            })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
