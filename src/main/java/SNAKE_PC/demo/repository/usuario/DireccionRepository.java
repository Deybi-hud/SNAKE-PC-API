package SNAKE_PC.demo.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Direccion;


public interface DireccionRepository extends JpaRepository<Direccion, Long> {


    boolean existsByComuna(Comuna comuna);
    long countByComuna(Comuna comuna);
    
}
