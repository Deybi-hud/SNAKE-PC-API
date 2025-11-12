package SNAKE_PC.demo.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Region;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    Optional<Comuna> findByNombreComuna(String nombreComuna);
    boolean existsByRegion(Region region);
    long countByRegion(Region region);
    Optional<Comuna> findByNombreComunaAndRegion(String nombreComuna, Region region);
    
}
