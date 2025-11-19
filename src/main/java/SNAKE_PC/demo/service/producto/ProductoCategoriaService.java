package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.repository.producto.CategoriaRepository;
import SNAKE_PC.demo.repository.producto.ProductoCategoriaRepository;

import jakarta.transaction.Transactional;

@SuppressWarnings("null")
@Service
@Transactional
public class ProductoCategoriaService {

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProductoCategoria> buscarTodos() {
        return productoCategoriaRepository.findAll();
    }

    public ProductoCategoria buscarPorId(Long id) {
        ProductoCategoria productoCategoria = productoCategoriaRepository.findById(id).orElse(null);
        if (productoCategoria == null) {
            throw new IllegalArgumentException("ProductoCategoria no encontrada.");
        }
        return productoCategoria;
    }

    public ProductoCategoria guardarProductoCategoria(ProductoCategoria productoCategoria, Long idCategoria) {

        if (productoCategoria.getNombreSubcategoria() == null
                || productoCategoria.getNombreSubcategoria().isBlank()) {
            throw new RuntimeException("El nombre de la subcategoría no puede estar vacío.");
        }

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));

        productoCategoria.setCategoria(categoria);

        return productoCategoriaRepository.save(productoCategoria);
    }

    public ProductoCategoria actualizarProductoCategoria(Long id, ProductoCategoria productoCategoria,
            Long idCategoria) {

        ProductoCategoria existente = productoCategoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductoCategoria no encontrada."));

        if (productoCategoria.getNombreSubcategoria() != null
                && !productoCategoria.getNombreSubcategoria().isBlank()) {
            existente.setNombreSubcategoria(productoCategoria.getNombreSubcategoria());
        }

        if (idCategoria != null) {
            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada."));
            existente.setCategoria(categoria);
        }

        return productoCategoriaRepository.save(existente);
    }

    public void eliminarProductoCategoria(Long id) {
        ProductoCategoria productoCategoria = productoCategoriaRepository.findById(id).orElse(null);
        if (productoCategoria == null) {
            throw new IllegalArgumentException("ProductoCategoria no encontrada.");
        }
        productoCategoriaRepository.deleteById(id);
    }
}
