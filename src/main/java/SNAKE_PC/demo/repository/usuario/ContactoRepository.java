package SNAKE_PC.demo.repository.usuario;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Usuario;


@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    boolean existsByTelefono(Integer id);
    boolean existsByDireccion(Direccion direccion);
    long countByDireccion(Direccion direccion);
    List<Contacto> findByUsuario(Usuario usuario);
}
