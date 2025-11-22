package SNAKE_PC.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.service.producto.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/productos")
@Tag(name = "Productos", description = "Gestión de productos")
public class ProductController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista de todos los productos (acceso público)")
    public ResponseEntity<?> listar() {
        try {
            List<Producto> productos = productoService.buscarTodo();
            if (productos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID (acceso público)")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID debe ser mayor a 0"));
            }
            Producto producto = productoService.buscarPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto")
    public ResponseEntity<?> crear(@RequestBody Producto producto,
            @RequestParam(required = false) Marca marca,
            @RequestParam(required = false) ProductoCategoria productoCategoria,
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) Especificacion especificacion) {
        try {
            Producto nuevoProducto = productoService.guardarProducto(
                    producto, productoCategoria, categoria, marca, especificacion, null, null, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto completamente", description = "Actualiza completamente un producto existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto,
            @RequestParam(required = false) Marca marca,
            @RequestParam(required = false) ProductoCategoria productoCategoria,
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) Especificacion especificacion) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID del producto debe ser mayor a 0"));
            }
            producto.setId(id);
            Producto updateProducto = productoService.guardarProducto(
                    producto, productoCategoria, categoria, marca, especificacion, null, null, null);
            return ResponseEntity.ok(updateProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza parcialmente un producto existente")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id, @RequestBody Producto producto,
            @RequestParam(required = false) Marca marca,
            @RequestParam(required = false) Especificacion especificacion) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID del producto debe ser mayor a 0"));
            }
            producto.setId(id);
            Producto updatedProducto = productoService.actualizacionParcialProducto(producto, marca, especificacion);
            return ResponseEntity.ok(updatedProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El ID del producto debe ser mayor a 0"));
            }
            productoService.borrarProducto(id);
            return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
