package SNAKE_PC.demo.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByMarcaNombre(String marcaNombre);
    boolean existsByMarcaNombre(String marcaNombre);
    Marca findByProductoId(Long id);

}
