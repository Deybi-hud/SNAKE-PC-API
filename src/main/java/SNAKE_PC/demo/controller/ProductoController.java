package SNAKE_PC.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.dto.producto.ProductoActualizarDTO;
import SNAKE_PC.demo.dto.producto.ProductoCrearDTO;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.service.producto.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/productos")
@Tag(name = "Productos", description = "Gestión de productos del catálogo")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Obtiene el listado completo de productos disponibles")
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID", description = "Obtiene los detalles de un producto específico")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        try {
            if (id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Producto producto = productoService.findById(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el catálogo con validaciones completas")
    public ResponseEntity<?> agregar(@Valid @RequestBody ProductoCrearDTO productoDTO) {
        try {
            Producto producto = new Producto();
            producto.setNombreProducto(productoDTO.getNombreProducto());
            producto.setStock(productoDTO.getStock());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setSku(productoDTO.getSku());
            
            Producto nuevoProducto = productoService.save(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza completamente la información de un producto")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoActualizarDTO productoDTO) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del producto debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Producto producto = new Producto();
            producto.setId(id);
            producto.setNombreProducto(productoDTO.getNombreProducto());
            producto.setStock(productoDTO.getStock());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setSku(productoDTO.getSku());
            
            Producto updateProducto = productoService.save(producto);
            if (updateProducto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updateProducto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza solo los campos específicos de un producto")
    public ResponseEntity<?> patchProducto(@PathVariable Long id, @RequestBody ProductoActualizarDTO productoDTO) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del producto debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            Producto producto = new Producto();
            producto.setNombreProducto(productoDTO.getNombreProducto());
            producto.setStock(productoDTO.getStock());
            producto.setPrecio(productoDTO.getPrecio());
            producto.setSku(productoDTO.getSku());
            
            Producto updateProducto = productoService.patchProducto(id, producto);
            return ResponseEntity.ok(updateProducto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto del catálogo")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        try{
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del producto debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            
            productoService.delete(id);
            Map<String, String> success = new HashMap<>();
            success.put("mensaje", "Producto eliminado correctamente");
            return ResponseEntity.ok(success);
        }catch(Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
