package SNAKE_PC.demo.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

        Optional<Region> findByNombreRegion(String nombreRegion);
    
    
}
