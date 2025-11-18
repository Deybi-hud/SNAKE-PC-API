package SNAKE_PC.demo.service.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.producto.CategoriaRepository;
import SNAKE_PC.demo.repository.producto.MarcaRepository;
import SNAKE_PC.demo.repository.producto.ProductoCategoriaRepository;
import SNAKE_PC.demo.repository.producto.ProductoRepository;

import jakarta.transaction.Transactional;

@SuppressWarnings("null")
@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    public List<Producto> findAll() {
        List<Producto> productos = productoRepository.findAll();
        return productos;
    }

    public Producto findById(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        return producto;
    }

    public Producto saveProducto(Producto producto) {
        // Validar que marca existe
        if (producto.getMarca() != null && producto.getMarca().getId() != null) {
            if (!marcaRepository.existsById(producto.getMarca().getId())) {
                throw new IllegalArgumentException("La marca no existe");
            }
        }
        
        // Validar que categoría existe
        if (producto.getProductoCategoria() != null && producto.getProductoCategoria().getId() != null) {
            if (!categoriaRepository.existsById(producto.getProductoCategoria().getId())) {
                throw new IllegalArgumentException("La categoría no existe");
            }
        }
        
        return productoRepository.save(producto);
    }

    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        productoRepository.deleteById(id);
    }

    public Producto partialUpdateProducto(Producto producto) {
        Producto existingProducto = productoRepository.findById(producto.getId()).orElse(null);
        if (existingProducto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        if (existingProducto != null) {
            if (producto.getNombreProducto() != null) {
                existingProducto.setNombreProducto(producto.getNombreProducto());
            }
            if (producto.getStock() != null) {
                existingProducto.setStock(producto.getStock());
            }

            if (producto.getPrecio() != null) {
                existingProducto.setPrecio(producto.getPrecio());
            }

            if (producto.getSku() != null) {
                existingProducto.setSku(producto.getSku());
            }

            if (producto.getProductoCategoria() != null) {
                existingProducto.setProductoCategoria(producto.getProductoCategoria());
            }

            if (producto.getMarca() != null) {
                existingProducto.setMarca(producto.getMarca());
            }

            if (producto.getDimension() != null) {
                existingProducto.setDimension(producto.getDimension());
            }

            if (producto.getEspecificacion() != null) {
                existingProducto.setEspecificacion(producto.getEspecificacion());
            }

            return productoRepository.save(existingProducto);
        }
        throw new IllegalArgumentException("Producto no encontrado.");
    }

    public Producto updateProducto(Producto producto) {
        return saveProducto(producto);
    }
}