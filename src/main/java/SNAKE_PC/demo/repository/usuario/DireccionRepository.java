package SNAKE_PC.demo.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Direccion;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {


    boolean existsByComuna(Comuna comuna);
    long countByComuna(Comuna comuna);
    
}
