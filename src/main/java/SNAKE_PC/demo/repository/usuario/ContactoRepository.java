package SNAKE_PC.demo.repository.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Usuario;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    boolean existsByTelefono(String telefono);

    boolean existsByDireccion(Direccion direccion);

    long countByDireccion(Direccion direccion);

    List<Contacto> findByUsuario(Usuario usuario);

    @Query("SELECT c FROM Contacto c WHERE c.id = :id AND c.usuario.correo = :correo")
    Optional<Contacto> findByIdAndUsuarioCorreo(
            @Param("id") Long id,
            @Param("correo") String correo);

    boolean existsByTelefonoAndId(String telefono, Long contactoIdActual);

    boolean existsByTelefonoAndIdNot(String telefono, Long contactoIdActual);

    Optional<Contacto> findByUsuarioId(Long idUsuarioLogueado);
}
