package SNAKE_PC.demo.service.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.producto.CategoriaRepository;
import SNAKE_PC.demo.repository.producto.EspecificacionRepository;
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

    @Autowired
    private EspecificacionRepository especificacionRepository;

    public List<Producto> buscarTodo() {
        List<Producto> productos = productoRepository.findAll();
        return productos;
    }

    public Producto buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        return producto;
    }

    public Producto guardarProducto(Producto producto, String marcaNombre, String categoriaNombre,
            String frecuencia, String capacidad, String consumo, Long idMarca, Long idCategoria,
            Long idEspecificacion) {

        if (producto.getNombreProducto() == null) {
            throw new RuntimeException("El nombre del producto no puede estar vacio.");
        }
        if (producto.getStock() == null || producto.getStock() <= 0) {
            throw new RuntimeException("El stock del producto no puede ser menor a cero.");
        }
        if (producto.getPrecio() == null || producto.getPrecio() <= 0) {
            throw new RuntimeException("El precio del producto no puede ser menor o igual a cero.");
        }
        if (producto.getSku() == null) {
            throw new RuntimeException("El SKU del producto no puede estar vacio.");
        }

        if (marcaNombre == null || marcaNombre.isBlank()) {
            throw new RuntimeException("El nombre de la marca no puede estar vacio.");
        }
        Marca marca = marcaRepository.findByNombre(marcaNombre)
                .orElseGet(() -> {
                    Marca nuevaMarca = new Marca();
                    nuevaMarca.setNombre(marcaNombre);
                    return marcaRepository.save(nuevaMarca);
                });

        if (categoriaNombre == null || categoriaNombre.isBlank()) {
            throw new RuntimeException("El nombre de la categoria no puede estar vacio.");
        }
        Categoria categoria = categoriaRepository.findByNombreCategoria(categoriaNombre)
                .orElseGet(() -> {
                    Categoria nuevaCategoria = new Categoria();
                    nuevaCategoria.setNombreCategoria(categoriaNombre);
                    return categoriaRepository.save(nuevaCategoria);
                });

        ProductoCategoria productoCategoria = new ProductoCategoria();
        productoCategoria.setCategoria(categoria);
        productoCategoria.setProducto(producto);
        ProductoCategoria nuevaProductoCategoria = productoCategoriaRepository.save(productoCategoria);

        Especificacion especificacion = especificacionRepository
                .findByFrecuenciaAndCapacidadAlmacenamientoAndConsumo(frecuencia, capacidad, consumo)
                .orElseGet(() -> {
                    Especificacion nuevaEspecificacion = new Especificacion();
                    nuevaEspecificacion.setFrecuencia(frecuencia);
                    nuevaEspecificacion.setCapacidadAlmacenamiento(capacidad);
                    nuevaEspecificacion.setConsumo(consumo);
                    return especificacionRepository.save(nuevaEspecificacion);
                });

        producto.setMarca(marca);
        producto.setProductoCategoria(nuevaProductoCategoria);
        producto.setEspecificacion(especificacion);

        return productoRepository.save(producto);

    }

    public void borrarProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
    }

    public Producto actualizacionParcialProducto(Producto producto) {
        Producto existingProducto = productoRepository.findById(producto.getId()).orElse(null);
        if (existingProducto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
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

    public Producto actualizarProducto(Producto producto, String marcaNombre, String categoriaNombre,
            String frecuencia, String capacidad, String consumo, Long idMarca, Long idCategoria,
            Long idEspecificacion) {
        return guardarProducto(producto, marcaNombre, categoriaNombre, frecuencia, capacidad, consumo,
                idMarca, idCategoria, idEspecificacion);
    }
}
