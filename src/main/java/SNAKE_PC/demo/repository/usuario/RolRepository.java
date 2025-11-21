package SNAKE_PC.demo.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.RolUsuario;

@Repository
public interface RolRepository extends JpaRepository<RolUsuario, Long> {

    boolean existsByNombreRol(String nombreRol);

    Optional<RolUsuario> findByNombreRol(String nombreRol);
    
    List<RolUsuario> findByNombreRolContainingIgnoreCase(String nombre);

}
