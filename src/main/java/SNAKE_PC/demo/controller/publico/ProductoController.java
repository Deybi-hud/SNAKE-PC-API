package SNAKE_PC.demo.controller.publico;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.service.producto.CategoriaService;
import SNAKE_PC.demo.service.producto.ProductoService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // ======================== READ PRODUCTOS ========================

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

    // ======================== READ MARCAS ========================

    // @GetMapping("/marcas")
    // @Operation(summary = "Listar marcas", description = "Obtiene todas las
    // marcas")
    // public ResponseEntity<?> listarMarcas() {
    // List<Marca> marcas = marcaService.buscarTodos();
    // List<Producto> productos = productoService.buscarTodo();
    // if (marcas.isEmpty()) {
    // return ResponseEntity.noContent().build();
    // }
    // return ResponseEntity.ok(marcas).ok(productos);
    // }

    // ======================== READ CATEGORIAS ========================

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorias", description = "Obtiene todas las categorias")
    public ResponseEntity<?> listarCategorias() {
        List<Categoria> categorias = categoriaService.buscarTodos();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    // @GetMapping("/categorias/{id}")
    // @Operation(summary = "Obtener categoria por ID", description = "Obtiene una
    // categoria específica")
    // public ResponseEntity<?> obtenerCategoria(@PathVariable Long id) {
    // try {
    // Categoria categoria = categoriaService.buscarPorId(id);
    // return ResponseEntity.ok(categoria);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " +
    // e.getMessage());
    // }
    // }

}
