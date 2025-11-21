package SNAKE_PC.demo.repository.pedido;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import SNAKE_PC.demo.model.pedido.MetodoPago;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {

    Optional<MetodoPago>findByTipoPago(String tipoPago);

    boolean existsByTipoPago(String tipoPago);
}