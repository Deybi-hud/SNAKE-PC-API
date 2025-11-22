package SNAKE_PC.demo.service.pedido;


import java.math.BigDecimal;

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
  
   public DetallePedido crearDetalle(Long productoId, Integer cantidad, Pedido pedido, Long metodoEnvioId){  
        Producto producto = productoService.buscarPorId(productoId);
        MetodoEnvio metodoEnvio = metodoEnvioService.seleccionarMetodoEnvio(metodoEnvioId);

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        detalle.setPedido(pedido);
        detalle.setMetodoEnvio(metodoEnvio);


        return detalleRepository.save(detalle);
    }
        

}
