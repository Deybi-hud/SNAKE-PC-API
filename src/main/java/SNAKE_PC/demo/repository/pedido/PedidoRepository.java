package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.Pedido;



@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    
    void deleteAllbyUsuarioId(Long id);
}
