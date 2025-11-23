package SNAKE_PC.demo.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> parent of e8606bd (asdasd)
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Especificacion;

@Repository
public interface EspecificacionRepository extends JpaRepository<Especificacion, Long> {

        Optional<Especificacion> findByFrecuenciaAndCapacidadAlmacenamientoAndConsumo(String frecuencia,
                        String capacidad,
                        String consumo);

        boolean existsByFrecuenciaAndCapacidadAlmacenamientoAndConsumo(String frecuencia, String capacidad,
                        String consumo);

<<<<<<< HEAD
=======
        @Modifying
        @Query("DELETE FROM Especificacion e WHERE :productoId MEMBER OF e.productos")
        void deleteByProductoId(@Param("productoId") Long productoId);

>>>>>>> parent of e8606bd (asdasd)
}
