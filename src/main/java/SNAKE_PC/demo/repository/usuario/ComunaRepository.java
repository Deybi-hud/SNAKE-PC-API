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

    // ✅ VERIFICAR SI EXISTE COMUNA EN REGIÓN
    boolean existsByNombreComunaAndRegionId(String nombreComuna, Long regionId);
    
    // ✅ OBTENER COMUNAS POR REGIÓN
    List<Comuna> findByRegionId(Long regionId);
    
    // ✅ OBTENER COMUNAS POR REGIÓN ORDENADAS
    List<Comuna> findByRegionIdOrderByNombreComunaAsc(Long regionId);
    
    // ✅ OBTENER COMUNAS POR NOMBRE
    Optional<Comuna> findByNombreComuna(String nombreComuna);
    
    // ✅ VERIFICAR SI EXISTE COMUNA POR NOMBRE
    boolean existsByNombreComuna(String nombreComuna);
    
    // ✅ BUSCAR COMUNAS QUE CONTENGAN TEXTO EN EL NOMBRE
    List<Comuna> findByNombreComunaContainingIgnoreCase(String nombre);
    
    // ✅ OBTENER COMUNAS POR REGIÓN CON JOIN FETCH
    @Query("SELECT c FROM Comuna c JOIN FETCH c.region WHERE c.region.id = :regionId")
    List<Comuna> findByRegionIdWithRegion(@Param("regionId") Long regionId);
    
    // ✅ OBTENER TODAS LAS COMUNAS CON REGIÓN (EAGER)
    @Query("SELECT c FROM Comuna c JOIN FETCH c.region")
    List<Comuna> findAllWithRegion();
    
    // ✅ OBTENER COMUNA POR ID CON REGIÓN
    @Query("SELECT c FROM Comuna c JOIN FETCH c.region WHERE c.id = :comunaId")
    Optional<Comuna> findByIdWithRegion(@Param("comunaId") Long comunaId);
    
    // ✅ OBTENER COMUNAS POR NOMBRE DE REGIÓN
    @Query("SELECT c FROM Comuna c JOIN c.region r WHERE r.nombreRegion = :nombreRegion")
    List<Comuna> findByRegionNombre(@Param("nombreRegion") String nombreRegion);
}
