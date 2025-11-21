package SNAKE_PC.demo.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.ProductoCategoria;

@Repository
public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria, Long> {

    Optional<ProductoCategoria> findByNombreSubCategoria(String nombreSubCategoria);

    boolean existsByNombreSubCategoria(String nombreSubCategoria);

    boolean existsByNombreSubCategoriaAndCategoria(String nombreSubCategoria, Categoria categoria);
}
