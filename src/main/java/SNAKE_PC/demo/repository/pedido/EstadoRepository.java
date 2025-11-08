package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;

import SNAKE_PC.demo.model.pedido.EstadoPedido;

public interface EstadoRepository extends JpaRepository<EstadoPedido, Long> {

    

    
}
