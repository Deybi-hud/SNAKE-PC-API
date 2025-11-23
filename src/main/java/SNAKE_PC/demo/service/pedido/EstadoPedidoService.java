package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.EstadoPedido;
import SNAKE_PC.demo.repository.pedido.EstadoPedidoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstadoPedidoService {
    

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;


    @PostConstruct
    public void inicializarEstados(){
        crearEstadoSiNoExiste("PENDIENTE", "El pedido está esperando ser procesado");
        crearEstadoSiNoExiste("PAGADO", "El pedido ha sido pagado");
        crearEstadoSiNoExiste("DESPACHADO","El pedido va en camino");
        crearEstadoSiNoExiste("ENTREGADO", "El pedido fue entregado exitosamente");
        crearEstadoSiNoExiste("CANCELADO", "El pedido fue cancelado");
    }

    private void crearEstadoSiNoExiste(String nombre, String descripcion){
        if(!estadoPedidoRepository.existsByNombreIgnoreCase(nombre)){
            EstadoPedido estado = new EstadoPedido();
            estado.setNombre(nombre.toUpperCase().trim());
            estado.setDescripcion(descripcion);
            estadoPedidoRepository.save(estado);
        }
    }

    public EstadoPedido obtenerEstadoPendiente(){
        return obtenerPorNombre("PENDIENTE");
    }

    public EstadoPedido obtenerEstadoPagado(){
        return obtenerPorNombre("PAGADO");
    }

    public EstadoPedido obtenerEstadoDespachado(){
        return obtenerPorNombre("DESPACHADO");
    }

    public EstadoPedido obtenerEstadoEntregado(){
        return obtenerPorNombre("ENTREGADO");
    }

    public EstadoPedido obtenerEstadoCancelado(){
        return obtenerPorNombre("CANCELADO");
    }

    public EstadoPedido obtenerPorNombre(String nombre){
        return estadoPedidoRepository.findByNombreIgnoreCase(nombre)
            .orElseThrow(()-> new RuntimeException("Estado de pedido no encontrado"));
    }

}
