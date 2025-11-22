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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Obtiene un producto específico por su ID (acceso público)")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        try {
            Producto producto = productoService.buscarPorId(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto")
    public ResponseEntity<?> crear(@RequestPart Producto producto,
            @RequestPart(required = false) Marca marca,
            @RequestPart(required = false) ProductoCategoria productoCategoria,
            @RequestPart(required = false) Categoria categoria,
            @RequestPart(required = false) Especificacion especificacion) {
        try {
            Producto nuevoProducto = productoService.guardarProducto(
                    producto, productoCategoria, categoria, marca, especificacion, null, null, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Actualizar producto completamente", description = "Actualiza completamente un producto existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestPart Producto producto,
            @RequestPart(required = false) Marca marca,
            @RequestPart(required = false) ProductoCategoria productoCategoria,
            @RequestPart(required = false) Categoria categoria,
            @RequestPart(required = false) Especificacion especificacion) {
        try {
            producto.setId(id);
            Producto updateProducto = productoService.guardarProducto(
                    producto, productoCategoria, categoria, marca, especificacion, null, null, null);
            return ResponseEntity.ok(updateProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza parcialmente un producto existente")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id, @RequestPart Producto producto,
            @RequestPart(required = false) Marca marca,
            @RequestPart(required = false) Especificacion especificacion) {
        try {
            producto.setId(id);
            Producto updatedProducto = productoService.actualizacionParcialProducto(producto, marca, especificacion);
            return ResponseEntity.ok(updatedProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            productoService.borrarProducto(id);
            return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
