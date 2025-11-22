package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;


@Repository
public interface MetodoEnvioRepository extends JpaRepository<MetodoEnvio, Long> {

    boolean existsByNombreIgnoreCase(String metodoEnvio);

    boolean existsById(Integer id);

    boolean existsByNombreMetodo(Integer id);

    boolean existsByNombreMetodo(String nombreMetodo);

    boolean existsByNombreMetodoIgnoreCase(String nombre);

    

    
}
