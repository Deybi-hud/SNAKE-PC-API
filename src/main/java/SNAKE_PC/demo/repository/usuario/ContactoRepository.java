package SNAKE_PC.demo.repository.usuario;



import org.springframework.data.jpa.repository.JpaRepository;

import SNAKE_PC.demo.model.usuario.Contacto;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    boolean existsByTelefono(Integer id);
        
    
}
