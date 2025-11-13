package SNAKE_PC.demo.service.producto;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Color;
import SNAKE_PC.demo.model.producto.ColorProducto;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.repository.producto.CategoriaRepository;
import SNAKE_PC.demo.repository.producto.ColorRepository;
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
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ProductoCategoriaRepository productoCategoriaRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto findById(long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public void delete(long id) {
        productoRepository.deleteById(id);
    }

    public Producto patchProducto(Long id, Producto producto) {
        Optional<Producto> productoOptional = productoRepository.findById(id);
        if (productoOptional.isPresent()) {
            Producto productoUpdate = productoOptional.get();
            if (producto.getNombreProducto() != null) {
                productoUpdate.setNombreProducto(producto.getNombreProducto());
            }
            if (producto.getStock() != null) {
                productoUpdate.setStock(producto.getStock());
            }
            if (producto.getPrecio() != null) {
                productoUpdate.setPrecio(producto.getPrecio());
            }
            if (producto.getSku() != null) {
                productoUpdate.setSku(producto.getSku());
            }
            return productoRepository.save(productoUpdate);
        } else {
            return null;
        }
    }

    public Producto creaProducto(Producto producto, String nombreCategoria, String colorProducto){
        if(producto.getNombreProducto() == null){
            throw new RuntimeException("Debe ingresar el nombre del producto");
        }
        if(producto.getStock() <= 0){
            throw new RuntimeException("Debe ingresar un número positivo");
        }
        if(producto.getPrecio() <= 0){
            throw new RuntimeException("El precio del producto no puede ser 0");
        }
        if(producto.getSku() == null){
            throw new RuntimeException("El producto debe tener un sku");
        }
        if(nombreCategoria == null || colorProducto == null){
            throw new RuntimeException("Debe ingresar categoria del producto y un color a su producto");
        }
        
        Color color = colorRepository.findByNombreColor(colorProducto)
            .orElseGet(()-> {
                Color nuevoColor = new Color();
                nuevoColor.setNombreColor(colorProducto);
                return colorRepository.save(nuevoColor);
            });
        color.setNombreColor(colorProducto);

        Categoria nuevaCategoria = categoriaRepository.findByNombreCategoria(nombreCategoria)
            .orElseGet(()->{
                Categoria cat = new Categoria();
                cat.setNombreCategoria(nombreCategoria);
                return categoriaRepository.save(cat);
            });
        nuevaCategoria.setNombreCategoria(nombreCategoria);
        
       

        return productoRepository.save(producto);
    }
}
