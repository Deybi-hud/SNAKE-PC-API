package SNAKE_PC.demo.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

    @Query("SELECT m FROM Marca m WHERE LOWER(m.marcaNombre) = LOWER(:marcaNombre)")
    Optional<Marca> findByMarcaNombre(@Param("marcaNombre") String marcaNombre);
    
    boolean existsByMarcaNombre(String marcaNombre);

}
