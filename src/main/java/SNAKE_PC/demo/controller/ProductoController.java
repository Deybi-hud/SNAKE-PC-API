package SNAKE_PC.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.service.producto.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/v1/productos")
@Tag(name = "Productos", description = "Aquí estan los productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Esta api llama a todos los productos", description = "Esta api se encarga de obtener todos los productos")
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.buscarTodo();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Esta api llama a un productos por su id", description = "esta api se encarga de obtener a un productos por id")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }

            Producto producto = productoService.buscarPorId(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/agregar")
    @Operation(summary = "Esta api agrega un producto", description = "esta api se encarga de agregar un producto")
    public ResponseEntity<?> agregar(@RequestBody Producto producto,
            @Parameter(description = "Nombre de la marca") @RequestParam String marcaNombre,
            @Parameter(description = "Nombre de la categoría") @RequestParam String categoriaNombre,
            @Parameter(description = "Frecuencia") @RequestParam String frecuencia,
            @Parameter(description = "Capacidad") @RequestParam String capacidad,
            @Parameter(description = "Consumo") @RequestParam String consumo,
            @Parameter(description = "ID de marca") @RequestParam Long idMarca,
            @Parameter(description = "ID de categoría") @RequestParam Long idCategoria,
            @Parameter(description = "ID de especificación") @RequestParam Long idEspecificacion) {
        try {
            if (producto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto no puede ser nulo");
            }
            if (producto.getNombreProducto() == null || producto.getNombreProducto().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre del producto es obligatorio");
            }

            Producto nuevoProducto = productoService.guardarProducto(producto, marcaNombre, categoriaNombre,
                    frecuencia, capacidad, consumo, idMarca, idCategoria, idEspecificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Esta api actualiza un producto", description = "esta api se encarga de actualizar a un producto existente")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @RequestBody Producto producto,
            @Parameter(description = "Nombre de la marca") @RequestParam String marcaNombre,
            @Parameter(description = "Nombre de la categoría") @RequestParam String categoriaNombre,
            @Parameter(description = "Frecuencia") @RequestParam String frecuencia,
            @Parameter(description = "Capacidad") @RequestParam String capacidad,
            @Parameter(description = "Consumo") @RequestParam String consumo,
            @Parameter(description = "ID de marca") @RequestParam Long idMarca,
            @Parameter(description = "ID de categoría") @RequestParam Long idCategoria,
            @Parameter(description = "ID de especificación") @RequestParam Long idEspecificacion) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del producto debe ser mayor a 0");
            }

            if (producto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto no puede ser nulo");
            }

            producto.setId(id);
            Producto updateProducto = productoService.guardarProducto(producto, marcaNombre, categoriaNombre,
                    frecuencia, capacidad, consumo, idMarca, idCategoria, idEspecificacion);
            if (updateProducto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updateProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Esta api actualiza parcialmente un producto", description = "esta api se encarga de actualizar parcialmente a un producto existente")
    public ResponseEntity<?> updateParcialProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del producto debe ser mayor a 0");
            }

            producto.setId(id);
            Producto updatedProducto = productoService.actualizacionParcialProducto(producto);
            return ResponseEntity.ok(updatedProducto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Esta api elimina a un producto", description = "esta api se encarga de eliminar a un producto existente")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID del producto debe ser mayor a 0");
            }

            productoService.borrarProducto(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
