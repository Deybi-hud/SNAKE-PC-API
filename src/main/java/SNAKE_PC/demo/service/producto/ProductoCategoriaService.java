package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.repository.producto.ProductoCategoriaRepository;
import jakarta.transaction.Transactional;

@SuppressWarnings("null")
@Service
@Transactional
public class ProductoCategoriaService {

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    @Autowired 
    private CategoriaService categoriaService;

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

    public ProductoCategoria guardarProductoCategoria(String nombreSubCategoria, String nombreCategoria) {
        if (nombreSubCategoria == null|| nombreSubCategoria.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la subcategoría no puede estar vacío.");
        }

        Categoria productoCategoria = categoriaService.guardarCategoria(nombreCategoria);
 
        ProductoCategoria nuevoProductoCategoria = new ProductoCategoria();
        nuevoProductoCategoria.setCategoria(productoCategoria);
        nuevoProductoCategoria.setNombreSubCategoria(nombreSubCategoria);
        return productoCategoriaRepository.save(nuevoProductoCategoria);
       
    }

    public ProductoCategoria actualizarProductoCategoria(String nombreSubCategoria, String nombreCategoria ) {
        ProductoCategoria existente = productoCategoriaRepository.findByNombreSubCategoria(nombreSubCategoria)
                .orElseThrow(() -> new RuntimeException("ProductoCategoria no encontrada."));  
        if (nombreSubCategoria != null && !nombreSubCategoria.trim().isEmpty()) {
            existente.setNombreSubCategoria(nombreSubCategoria.trim());
        }
        if (nombreCategoria != null) {
            Categoria categoria = categoriaService.guardarCategoria(nombreCategoria);
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
