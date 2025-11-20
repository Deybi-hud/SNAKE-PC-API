package SNAKE_PC.demo.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.RolUsuario;

@Repository
public interface RolRepository extends JpaRepository<RolUsuario, Long> {

      // ✅ VERIFICAR SI EXISTE ROL POR NOMBRE
    boolean existsByNombreRol(String nombreRol);
    
    // ✅ BUSCAR ROL POR NOMBRE
    Optional<RolUsuario> findByNombreRol(String nombreRol);
    
    // ✅ BUSCAR ROLES QUE CONTENGAN TEXTO EN EL NOMBRE
    List<RolUsuario> findByNombreRolContainingIgnoreCase(String nombre);

}
