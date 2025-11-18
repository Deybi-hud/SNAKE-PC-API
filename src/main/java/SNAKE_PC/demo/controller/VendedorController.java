package SNAKE_PC.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.service.producto.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/vendedores")
@Tag(name = "Vendedores", description = "Gestión de productos por vendedores")
public class VendedorController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos")
    @Operation(summary = "Listar todos los productos disponibles", description = "Obtiene la lista de todos los productos registrados")
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/productos/{id}")
    @Operation(summary = "Obtener detalles de un producto", description = "Obtiene la información detallada de un producto específico")
    public ResponseEntity<?> obtenerProducto(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del producto debe ser mayor a 0");
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

    @PostMapping("/productos")
    @Operation(summary = "Registrar un nuevo producto", description = "Crea un nuevo producto con sus especificaciones")
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            if (producto == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El producto no puede ser nulo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Producto nuevoProducto = productoService.saveProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/productos/{id}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza la información completa de un producto")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del producto debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (producto == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El producto no puede ser nulo");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            producto.setId(id);
            Producto productoActualizado = productoService.saveProducto(producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/productos/{id}/parcial")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza solo los campos especificados de un producto")
    public ResponseEntity<?> actualizarProductoParcial(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del producto debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            producto.setId(id);
            Producto productoActualizado = productoService.partialUpdateProducto(producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

}
