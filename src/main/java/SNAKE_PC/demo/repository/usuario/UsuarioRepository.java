package SNAKE_PC.demo.repository.usuario;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Usuario;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    
  @Query("SELECT u FROM Usuario u "         +
        "LEFT JOIN FETCH u.contactos c "    +
        "LEFT JOIN FETCH c.direccion d "    +
        "LEFT JOIN FETCH d.comuna co "      +
        "LEFT JOIN FETCH c.rolUsuario r "   +
        "WHERE u.id = :usuarioId")
        Optional<Usuario> findByIdWithRelations(@Param("usuarioId") Long usuarioId);

}
