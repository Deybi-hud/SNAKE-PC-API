package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.DetallePedido;




@Repository
public interface DetalleRepository extends JpaRepository<DetallePedido, Long> {

    

    
}
