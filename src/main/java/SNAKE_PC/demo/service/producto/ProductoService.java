package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.repository.producto.ProductoRepository;
import jakarta.transaction.Transactional;

@SuppressWarnings("null")
@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EspecificacionService especificacionService;

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
            .orElseThrow(()-> new RuntimeException("Producto no encontrado."));
        return producto;
    }

    public Producto guardarProducto(Producto producto, ProductoCategoria productoCategoria, Categoria categoria,  Marca marca, Especificacion especificacion,
        Long idMarca, Long idCategoria,Long idEspecificacion) {

        
        validarProducto(producto);
        Marca marcaNueva = marcaService.guardarMarca(marca);
        Especificacion especificacionNuevaOexistente = especificacionService.guardarEspecificacion(especificacion);
        ProductoCategoria nuevoProductoCategoria = productoCategoriaService.guardarProductoCategoria(productoCategoria, categoria);

        producto.setMarca(marcaNueva);
        producto.setEspecificacion(especificacionNuevaOexistente);
        producto.setProductoCategoria(nuevoProductoCategoria);

        return productoRepository.save(producto);

    }

    public void validarProducto(Producto producto){
        if(producto.getNombreProducto() == null || producto.getNombreProducto().trim().isBlank()){
            throw new RuntimeException("Debe asignar un nombre al producto");
        }
        if(producto.getStock() == null || producto.getStock() < 0){
            throw new RuntimeException("El stock debe ser mayor o igual a 0");
        }
        if(producto.getPrecio() == null || producto.getPrecio() <= 0){
            throw new RuntimeException("El valor del producto no puede ser 0");
        }
        if(producto.getSku() == null || producto.getSku().trim().isBlank()){
            throw new RuntimeException("Debe asignar un sku");
        }
        if(producto.getPeso() == null || producto.getPeso().trim().isBlank()){
            throw new RuntimeException("Debe asignar un peso en (kg / gr)");
        }

    }

    public void borrarProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        productoRepository.deleteById(id);
    }

    public Producto actualizacionParcialProducto(Producto producto, Marca marca, Especificacion especificacion) {
        Producto existingProducto = productoRepository.findById(producto.getId())
            .orElseThrow(()-> new RuntimeException("El producto no fue encontrado"));

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
        if (marca.getMarcaNombre() != null && !marca.getMarcaNombre().trim().isBlank()) {
            Marca marcaNueva = marcaService.guardarMarca(marca);
            existingProducto.setMarca(marcaNueva);
        }
        boolean cambiarEspecificaciones = especificacion.getFrecuencia() != null || especificacion.getCapacidadAlmacenamiento() != null || especificacion.getConsumo() != null; 
        if (cambiarEspecificaciones) {
            Especificacion nuevaEspecificacion = especificacionService.guardarEspecificacion(especificacion);
            existingProducto.setEspecificacion(nuevaEspecificacion);
        }
        return productoRepository.save(existingProducto);
    }

    public Producto buscarPorNombre(String nombreProducto){
        if(nombreProducto == null || nombreProducto.trim().isEmpty()){
            throw new RuntimeException("Debe ingresar un nombre para buscar.");
        }
        Producto existente = productoRepository.findByNombreProducto(nombreProducto)
            .orElseThrow(()-> new RuntimeException("No se encontraron productos con ese nombre"));
        
        return existente;
    }

    public Producto actualizarStock(Long idProducto, int cantidad){
        if(cantidad <0 ){
            throw new RuntimeException("La cantidad no puede ser negativa");
        }
        Producto existente = productoRepository.findById(idProducto)
            .orElseThrow(()-> new RuntimeException("Producto no encontrado"));
        int nuevoStock = existente.getStock() -  cantidad;
        if(nuevoStock < 0 ){
              throw new RuntimeException("Stock insuficiente");
        }
        existente.setStock(nuevoStock);
        return productoRepository.save(existente);
    }

}
