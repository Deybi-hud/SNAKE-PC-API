package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        return producto;
    }

    public Producto guardarProducto(String nombreProducto, Double precio, String sku, Integer stock,String peso, String marcaNombre, String categoriaNombre,
            String nombreSubCategoria, String frecuencia, String capacidadAlmacenamiento, String consumo, Long idMarca, Long idCategoria,Long idEspecificacion) {

        
        validarProducto(nombreProducto, precio, sku, stock, peso);
        Marca marca = marcaService.guardarMarca(marcaNombre);

        Especificacion especificacionNuevaOexistente = especificacionService.guardarEspecificacion(frecuencia, capacidadAlmacenamiento, consumo);
        ProductoCategoria nuevoProductoCategoria = productoCategoriaService.guardarProductoCategoria(nombreSubCategoria, categoriaNombre);

        
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombreProducto(nombreProducto);
        nuevoProducto.setStock(stock);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setSku(sku);
        nuevoProducto.setPeso(peso);
        nuevoProducto.setMarca(marca);
        nuevoProducto.setEspecificacion(especificacionNuevaOexistente);
        nuevoProducto.setProductoCategoria(nuevoProductoCategoria);

        return productoRepository.save(nuevoProducto);

    }

    public void validarProducto(String nombreProducto, Double precio, String sku, Integer stock, String peso){
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
        if(peso == null || peso.trim().isEmpty()){
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

    public Producto actualizacionParcialProducto(Producto producto, String marcaNombre, String frecuencia, String consumo, String capacidadAlmacenamiento) {
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
        if (marcaNombre != null && !marcaNombre.trim().isEmpty()) {
            Marca marcaNueva = marcaService.guardarMarca(marcaNombre);
            existingProducto.setMarca(marcaNueva);
        }
        boolean cambiarEspecificaciones = frecuencia != null || consumo != null || capacidadAlmacenamiento != null; 
        if (cambiarEspecificaciones) {
            Especificacion nuevaEspecificacion = especificacionService.guardarEspecificacion(
                frecuencia != null ? frecuencia.trim() : null, 
                capacidadAlmacenamiento != null? capacidadAlmacenamiento.trim() : null, 
                consumo != null ? consumo.trim() : null
            );
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


}
