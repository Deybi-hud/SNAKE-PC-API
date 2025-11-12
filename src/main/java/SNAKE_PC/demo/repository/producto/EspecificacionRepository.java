package SNAKE_PC.demo.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Especificacion;


@Repository
public interface EspecificacionRepository extends JpaRepository<Especificacion, Long> {

    

    
}
