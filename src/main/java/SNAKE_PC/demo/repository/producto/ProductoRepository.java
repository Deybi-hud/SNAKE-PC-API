package SNAKE_PC.demo.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;

import SNAKE_PC.demo.model.producto.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {


}
