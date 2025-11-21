package SNAKE_PC.demo.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsByNombreRegion(String nombreRegion);
    
    Optional<Region> findByNombreRegion(String nombreRegion);

    List<Region> findAllByOrderByNombreRegionAsc();

    List<Region> findByNombreRegionContainingIgnoreCase(String nombre);
}
