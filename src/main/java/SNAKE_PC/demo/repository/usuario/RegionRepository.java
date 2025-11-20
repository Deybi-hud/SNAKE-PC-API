package SNAKE_PC.demo.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

        // ✅ VERIFICAR SI EXISTE REGIÓN POR NOMBRE
    boolean existsByNombreRegion(String nombreRegion);
    
    // ✅ BUSCAR REGIÓN POR NOMBRE
    Optional<Region> findByNombreRegion(String nombreRegion);
    
    // ✅ OBTENER REGIONES ORDENADAS POR NOMBRE
    List<Region> findAllByOrderByNombreRegionAsc();
    
    // ✅ BUSCAR REGIONES QUE CONTENGAN TEXTO EN EL NOMBRE
    List<Region> findByNombreRegionContainingIgnoreCase(String nombre);
}
