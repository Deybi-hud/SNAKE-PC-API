package SNAKE_PC.demo.repository.usuario;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Comuna;
import java.util.List;
import java.util.Optional;



@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    boolean existsByNombreComunaAndRegionId(String nombreComuna, Long regionId);
    
    List<Comuna> findByRegionId(Long regionId);

    List<Comuna> findByRegionIdOrderByNombreComunaAsc(Long regionId);
    
    Optional<Comuna> findByNombreComuna(String nombreComuna);
    
    boolean existsByNombreComuna(String nombreComuna);
    
    List<Comuna> findByNombreComunaContainingIgnoreCase(String nombre);
    
    @Query("SELECT c FROM Comuna c JOIN FETCH c.region WHERE c.region.id = :regionId")
    List<Comuna> findByRegionIdWithRegion(@Param("regionId") Long regionId);
    
    @Query("SELECT c FROM Comuna c JOIN FETCH c.region")
    List<Comuna> findAllWithRegion();

    @Query("SELECT c FROM Comuna c JOIN FETCH c.region WHERE c.id = :comunaId")
    Optional<Comuna> findByIdWithRegion(@Param("comunaId") Long comunaId);
    
    @Query("SELECT c FROM Comuna c JOIN c.region r WHERE r.nombreRegion = :nombreRegion")
    List<Comuna> findByRegionNombre(@Param("nombreRegion") String nombreRegion);
}
