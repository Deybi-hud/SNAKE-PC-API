package SNAKE_PC.demo.repository.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import SNAKE_PC.demo.model.usuario.Comuna;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    Optional<Comuna> findByNombreComuna(String nombreComuna);
    boolean existsByNombreComuna(String nombreComuna);
    
}
