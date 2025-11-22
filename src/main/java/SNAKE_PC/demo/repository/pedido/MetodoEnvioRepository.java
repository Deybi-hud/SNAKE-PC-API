package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;


@Repository
public interface MetodoEnvioRepository extends JpaRepository<MetodoEnvio, Long> {

    

    
}
