package SNAKE_PC.demo.service.pedido;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.DetallePedido;
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

  
    
    public DetallePedido crearDetalle(Long productoId, Integer cantidad, Pedido pedido){
        if(cantidad <= 0){
            throw new RuntimeException("No se puede generar un detalle con 0 productos");
        }

        Producto producto = productoService.buscarPorId(productoId);

        if(producto.getStock() < cantidad){
            throw new RuntimeException("Stock insuficiente para '" + producto.getNombreProducto() + 
            "'. Disponible: " + producto.getStock() + ", solicitado: " + cantidad);
        }

        DetallePedido detalleNuevo = new DetallePedido();
        detalleNuevo.setProducto(producto);
        detalleNuevo.setCantidad(cantidad);
        detalleNuevo.setPrecioUnitario(producto.getPrecio());
        detalleNuevo.setSubtotal(producto.getPrecio() * cantidad);
        detalleNuevo.setPedido(pedido);
        
        return detalleRepository.save(detalleNuevo);
    }
    
    public Double calcularTotalPedido(Long pedidoId) {
        List<DetallePedido> detalles = detalleRepository.findByPedidoId(pedidoId);

        return detalles.stream()
                .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
                .sum();
    }



}
