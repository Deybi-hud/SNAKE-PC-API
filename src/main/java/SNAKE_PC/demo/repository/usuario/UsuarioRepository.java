package SNAKE_PC.demo.repository.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  Optional<Usuario> findByNombreUsuario(String nombreUsuario);

  Optional<Usuario> findByCorreo(String correo);

  boolean existsByCorreo(String correo);

  boolean existsByNombreUsuario(String nombreUsuario);

  boolean isActivo(boolean activo);

}
