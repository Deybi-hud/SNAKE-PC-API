package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.Pago;




@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    

    
}
