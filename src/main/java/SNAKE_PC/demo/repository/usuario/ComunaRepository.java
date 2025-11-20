package SNAKE_PC.demo.repository.usuario;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Comuna;
import java.util.List;
import java.util.Optional;



@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    List<Comuna> findByRegionId(Long regionId);

    Optional<Comuna> findByNombreComuna(String nombreComuna);

    boolean existsByNombreComunaAndRegionId(String nombreComuna, Long regionId);
}
