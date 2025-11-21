package SNAKE_PC.demo.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

  Optional<Producto> findByNombreProducto(String nombreProducto);

}
