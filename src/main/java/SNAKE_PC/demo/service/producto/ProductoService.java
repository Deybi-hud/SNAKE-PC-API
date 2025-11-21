package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.repository.producto.EspecificacionRepository;
import SNAKE_PC.demo.repository.producto.MarcaRepository;
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
    private EspecificacionRepository especificacionRepository;

    @Autowired 
    private MarcaService marcaService;

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

    public Producto guardarProducto(String nombreProducto, Double precio, String sku, Integer stock, String marcaNombre, String categoriaNombre,
            String frecuencia, String capacidad, String consumo, Long idMarca, Long idCategoria,Long idEspecificacion) {

        
        validarProducto(nombreProducto,precio,sku,stock);

        Marca marca = marcaRepository.findByNombre(marcaNombre)
                .orElseGet(() -> {
                    Marca nuevaMarca = marcaService.guardarMarca(marcaNombre);
                    return marcaRepository.save(nuevaMarca);
                });

        Especificacion especificacion = especificacionRepository
                .findByFrecuenciaAndCapacidadAlmacenamientoAndConsumo(frecuencia, capacidad, consumo)
                .orElseGet(() -> {
                    Especificacion nuevaEspecificacion = new Especificacion();
                    nuevaEspecificacion.setFrecuencia(frecuencia);
                    nuevaEspecificacion.setCapacidadAlmacenamiento(capacidad);
                    nuevaEspecificacion.setConsumo(consumo);
                    return especificacionRepository.save(nuevaEspecificacion);
                });

        Producto nuevProducto = new Producto();
        nuevProducto.setNombreProducto(nombreProducto);
        nuevProducto.setStock(stock);
        nuevProducto.setPrecio(precio);
        nuevProducto.setSku(sku);
        nuevProducto.setMarca(marca);
        nuevProducto.setEspecificacion(especificacion);

        return productoRepository.save(nuevProducto);

    }

    public void validarProducto(String nombreProducto, Double precio, String sku, Integer stock){
        if(nombreProducto == null || nombreProducto.trim().isEmpty()){
            throw new RuntimeException("Debe asignar un nombre al producto");
        }
        if(stock == null || stock < 0){
            throw new RuntimeException("El stock debe ser mayor o igual a 0");
        }
        if(precio == null || precio <= 0){
            throw new RuntimeException("El valor del producto no puede ser 0");
        }
        if(sku == null || sku.trim().isEmpty()){
            throw new RuntimeException("Debe asignar un sku");
        }


    }






    public void borrarProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        productoRepository.deleteById(id);
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

    public Producto actualizarProducto(String nombreProducto, Double precio, String sku, Integer stock, String marcaNombre, String categoriaNombre,
            String frecuencia, String capacidad, String consumo, Long idMarca, Long idCategoria,
            Long idEspecificacion) {
        return guardarProducto(nombreProducto, precio, sku, stock, marcaNombre, categoriaNombre, frecuencia, capacidad, consumo,
                idMarca, idCategoria, idEspecificacion);
    }
}
