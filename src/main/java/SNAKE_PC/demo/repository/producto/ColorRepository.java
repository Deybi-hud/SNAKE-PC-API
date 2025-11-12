package SNAKE_PC.demo.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Color;


@Repository
public interface ColorRepository extends JpaRepository<Color, Long>{

    

    
}
