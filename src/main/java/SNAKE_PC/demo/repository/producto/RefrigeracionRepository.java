package SNAKE_PC.demo.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Refrigeracion;




@Repository
public interface RefrigeracionRepository extends JpaRepository<Refrigeracion, Long> {


    
}
