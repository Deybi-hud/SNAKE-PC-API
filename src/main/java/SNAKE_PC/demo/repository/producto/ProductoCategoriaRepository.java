package SNAKE_PC.demo.repository.producto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.ProductoCategoria;

@Repository
public interface ProductoCategoriaRepository extends JpaRepository<ProductoCategoria, Long> {

    Optional<ProductoCategoria> findByNombreSubCategoria(String nombreSubCategoria);

    @Query("SELECT pc FROM ProductoCategoria pc WHERE LOWER(pc.nombreSubCategoria) = LOWER(:nombreSubCategoria) AND pc.categoria = :categoria")
    Optional<ProductoCategoria> findByNombreSubCategoriaAndCategoria(@Param("nombreSubCategoria") String nombreSubCategoria, @Param("categoria") Categoria categoria);

    boolean existsByNombreSubCategoria(String nombreSubCategoria);

    boolean existsByNombreSubCategoriaAndCategoria(String nombreSubCategoria, Categoria categoria);

}
