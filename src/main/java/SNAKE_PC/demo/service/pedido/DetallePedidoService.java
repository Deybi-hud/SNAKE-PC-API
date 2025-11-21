package SNAKE_PC.demo.service.pedido;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.DetallePedido;
import SNAKE_PC.demo.repository.pedido.DetalleRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DetallePedidoService {

    @Autowired
    private DetalleRepository detalleRepository;

  
    
    public DetallePedido crearDetalle(DetallePedido detallePedido){
        if(detallePedido.getProducto() == null){
            throw new RuntimeException("No se ha podido asociar el producto");
        }
        if(detallePedido.getCantidad() <= 0){
            throw new RuntimeException("No se puede generar un detalle con 0 productos");
        }
        if(detallePedido.getPrecioUnitario()<=0){
            throw new RuntimeException("El valor del producto no puede estar en 0");
        }

        detallePedido.setSubtotal( detallePedido.getPrecioUnitario() * detallePedido.getCantidad());
        return detalleRepository.save(detallePedido);
    }
    

}
