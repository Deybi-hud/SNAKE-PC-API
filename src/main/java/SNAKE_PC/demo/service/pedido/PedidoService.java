package SNAKE_PC.demo.service.pedido;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.repository.pedido.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado.");
        }
        return pedido;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void deletePedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado.");
        }
        pedidoRepository.deleteById(id);
    }

    public Pedido updatePedido(Long id, Pedido pedido) {
        Pedido existingPedido = pedidoRepository.findById(id).orElse(null);
        if (existingPedido == null) {
            throw new IllegalArgumentException("Pedido no encontrado.");
        }
        
        if (pedido.getFechaPedido() != null) {
            existingPedido.setFechaPedido(pedido.getFechaPedido());
        }
        if (pedido.getNumeroPedido() != null) {
            existingPedido.setNumeroPedido(pedido.getNumeroPedido());
        }
        if (pedido.getContacto() != null) {
            existingPedido.setContacto(pedido.getContacto());
        }
        if (pedido.getEstado() != null) {
            existingPedido.setEstado(pedido.getEstado());
        }
        
        return pedidoRepository.save(existingPedido);
    }
}
