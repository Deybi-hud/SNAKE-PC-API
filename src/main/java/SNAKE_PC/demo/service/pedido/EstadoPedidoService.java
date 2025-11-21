package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.EstadoPedido;
import SNAKE_PC.demo.repository.pedido.EstadoPedidoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstadoPedidoService {
    

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;


    public EstadoPedido guardarEstado(EstadoPedido estadoPedido){
        if(estadoPedido.getNombre() == null || estadoPedido.getNombre().trim().isBlank()){
            throw new RuntimeException("Debe asignar un estado");
        }
        if(estadoPedido.getDescripcion() == null || estadoPedido.getDescripcion().isBlank()){
            throw new RuntimeException("Por favor añada un descripcion breve.");
        }
        return estadoPedidoRepository.findByNombreIgnoreCase(estadoPedido.getNombre())
            .map(estadoExistente ->{
                estadoExistente.setDescripcion(estadoPedido.getDescripcion());
                return estadoPedidoRepository.save(estadoExistente);
            })
            .orElseGet(()-> {
                EstadoPedido nuevoEstadoPedido = new EstadoPedido();
                nuevoEstadoPedido.setNombre(estadoPedido.getNombre());
                nuevoEstadoPedido.setDescripcion(estadoPedido.getDescripcion());
                return estadoPedidoRepository.save(nuevoEstadoPedido);
            });
    }


    public EstadoPedido obtenerEstadoPendiente(){
        return estadoPedidoRepository.findByNombreIgnoreCase("PENDIENTE")
        .orElseGet(()-> {
            EstadoPedido nuevo = new EstadoPedido();
            nuevo.setNombre("PENDIENTE");
            nuevo.setDescripcion("El pedido se está preparando");
            return guardarEstado(nuevo);

        }); 
    }


}
