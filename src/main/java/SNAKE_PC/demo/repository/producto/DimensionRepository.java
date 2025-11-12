package SNAKE_PC.demo.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Dimension;


@Repository
public interface DimensionRepository extends JpaRepository<Dimension, Long> {

    

    
}
