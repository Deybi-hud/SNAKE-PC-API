package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;
import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoEnvioRepository extends JpaRepository<MetodoEnvio, Integer> {

    boolean existsByNombreMetodo(String nombreMetodo);

    boolean existsByNombreMetodoIgnoreCase(String nombreMetodo);

    List<MetodoEnvio> findByActivoTrue();

    Optional<MetodoEnvio> findByNombreMetodoIgnoreCase(String nombreMetodo);

}
