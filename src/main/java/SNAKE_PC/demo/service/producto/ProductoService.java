package SNAKE_PC.demo.service.producto;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.repository.producto.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("null")
@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ProductoCategoriaService productoCategoriaService;

    public List<Producto> buscarTodo() {
        List<Producto> productos = productoRepository.findAll();
        return productos;
    }

    public Producto buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));
        return producto;
    }

    public Producto guardarProducto(Producto producto, ProductoCategoria productoCategoria, Categoria categoria,
            Marca marca) {

        validarProducto(producto);
        return productoRepository.findByNombreProducto(producto.getNombreProducto())
                .orElseGet(() -> {
                    ProductoCategoria productoCategoriaGuardada = productoCategoriaService
                            .guardarProductoCategoria(productoCategoria, categoria);
                    producto.setMarca(marca);
                    producto.setProductoCategoria(productoCategoriaGuardada);
                    return productoRepository.save(producto);
                });

    }

    public void validarProducto(Producto producto) {
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isBlank()) {
            throw new RuntimeException("Debe asignar un nombre al producto");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new RuntimeException("El stock debe ser mayor o igual a 0");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El valor del producto no puede ser 0");
        }
        if (producto.getSku() == null || producto.getSku().trim().isBlank()) {
            throw new RuntimeException("Debe asignar un sku");
        }
    }

    public void borrarProducto(Long id) {
        productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoRepository.deleteById(id);
    }

    public Producto actualizacionParcialProducto(Producto producto, Marca marca) {
        Producto existenteProducto = productoRepository.findById(producto.getId())
                .orElseThrow(() -> new RuntimeException("El producto no fue encontrado"));

        if (producto.getNombreProducto() != null) {
            existenteProducto.setNombreProducto(producto.getNombreProducto());
        }
        if (producto.getStock() != null) {
            existenteProducto.setStock(producto.getStock());
        }
        if (producto.getPrecio() != null) {
            existenteProducto.setPrecio(producto.getPrecio());
        }
        if (producto.getSku() != null) {
            existenteProducto.setSku(producto.getSku());
        }
        if (producto.getProductoCategoria() != null) {
            existenteProducto.setProductoCategoria(producto.getProductoCategoria());
        }
        if (marca.getMarcaNombre() != null && !marca.getMarcaNombre().trim().isBlank()) {
            Marca marcaNueva = marcaService.guardarMarca(marca);
            existenteProducto.setMarca(marcaNueva);
        }
        return productoRepository.save(existenteProducto);
    }

    public Producto buscarPorNombre(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            throw new RuntimeException("Debe ingresar un nombre para buscar.");
        }
        Producto existente = productoRepository.findByNombreProducto(nombreProducto)
                .orElseThrow(() -> new RuntimeException("No se encontraron productos con ese nombre"));

        return existente;
    }

    public Producto actualizarStock(Long idProducto, int cantidad) {
        if (cantidad < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }
        Producto existente = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        int nuevoStock = existente.getStock() - cantidad;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente");
        }
        existente.setStock(nuevoStock);
        return productoRepository.save(existente);
    }

}
