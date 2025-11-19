package SNAKE_PC.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.service.producto.CategoriaService;
import SNAKE_PC.demo.service.producto.EspecificacionService;
import SNAKE_PC.demo.service.producto.MarcaService;
import SNAKE_PC.demo.service.producto.ProductoCategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/admin/catalogo")
@Tag(name = "Admin - Catálogo de Productos", description = "Gestión de marcas, categorías, especificaciones y subcategorías")
public class AdminProductController {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private EspecificacionService especificacionService;

    @Autowired
    private ProductoCategoriaService productoCategoriaService;

    // ======================== MARCAS ========================

    @GetMapping("/marcas")
    @Operation(summary = "Listar marcas", description = "Obtiene todas las marcas")
    public ResponseEntity<?> listarMarcas() {
        List<Marca> marcas = marcaService.buscarTodos();
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/marcas/{id}")
    @Operation(summary = "Obtener marca por ID", description = "Obtiene una marca específica")
    public ResponseEntity<?> obtenerMarca(@PathVariable Long id) {
        try {
            validarId(id);
            Marca marca = marcaService.buscarPorId(id);
            return ResponseEntity.ok(marca);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/marcas")
    @Operation(summary = "Crear marca", description = "Crea una nueva marca")
    public ResponseEntity<?> crearMarca(@RequestBody Marca marca) {
        try {
            validarMarca(marca);
            Marca nuevaMarca = marcaService.guardarMarca(marca);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMarca);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/marcas/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza una marca existente")
    public ResponseEntity<?> actualizarMarca(@PathVariable Long id, @RequestBody Marca marca) {
        try {
            validarId(id);
            validarMarca(marca);
            Marca marcaActualizada = marcaService.actualizarMarca(id, marca);
            return ResponseEntity.ok(marcaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/marcas/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina una marca del sistema")
    public ResponseEntity<?> eliminarMarca(@PathVariable Long id) {
        try {
            validarId(id);
            marcaService.eliminarMarca(id);
            return ResponseEntity.ok("Marca eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== CATEGORÍAS ========================

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorías", description = "Obtiene todas las categorías")
    public ResponseEntity<?> listarCategorias() {
        List<Categoria> categorias = categoriaService.buscarTodos();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/categorias/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Obtiene una categoría específica")
    public ResponseEntity<?> obtenerCategoria(@PathVariable Long id) {
        try {
            validarId(id);
            Categoria categoria = categoriaService.buscarPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/categorias")
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría")
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        try {
            validarCategoria(categoria);
            Categoria nuevaCategoria = categoriaService.guardarCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/categorias/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            validarId(id);
            validarCategoria(categoria);
            Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            validarId(id);
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.ok("Categoría eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== ESPECIFICACIONES ========================

    @GetMapping("/especificaciones")
    @Operation(summary = "Listar especificaciones", description = "Obtiene todas las especificaciones")
    public ResponseEntity<?> listarEspecificaciones() {
        List<Especificacion> especificaciones = especificacionService.buscarTodos();
        if (especificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(especificaciones);
    }

    @GetMapping("/especificaciones/{id}")
    @Operation(summary = "Obtener especificación por ID", description = "Obtiene una especificación específica")
    public ResponseEntity<?> obtenerEspecificacion(@PathVariable Long id) {
        try {
            validarId(id);
            Especificacion especificacion = especificacionService.buscarPorId(id);
            return ResponseEntity.ok(especificacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/especificaciones")
    @Operation(summary = "Crear especificación", description = "Crea una nueva especificación")
    public ResponseEntity<?> crearEspecificacion(@RequestBody Especificacion especificacion) {
        try {
            validarEspecificacion(especificacion);
            Especificacion nuevaEspecificacion = especificacionService.guardarEspecificacion(especificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEspecificacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/especificaciones/{id}")
    @Operation(summary = "Actualizar especificación", description = "Actualiza una especificación existente")
    public ResponseEntity<?> actualizarEspecificacion(@PathVariable Long id, @RequestBody Especificacion especificacion) {
        try {
            validarId(id);
            validarEspecificacion(especificacion);
            Especificacion especificacionActualizada = especificacionService.actualizarEspecificacion(id, especificacion);
            return ResponseEntity.ok(especificacionActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/especificaciones/{id}")
    @Operation(summary = "Eliminar especificación", description = "Elimina una especificación del sistema")
    public ResponseEntity<?> eliminarEspecificacion(@PathVariable Long id) {
        try {
            validarId(id);
            especificacionService.eliminarEspecificacion(id);
            return ResponseEntity.ok("Especificación eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== PRODUCTO CATEGORÍA (SUBCATEGORÍAS) ========================

    @GetMapping("/producto-categorias")
    @Operation(summary = "Listar subcategorías de productos", description = "Obtiene todas las subcategorías")
    public ResponseEntity<?> listarProductoCategorias() {
        List<ProductoCategoria> productoCategorias = productoCategoriaService.buscarTodos();
        if (productoCategorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productoCategorias);
    }

    @GetMapping("/producto-categorias/{id}")
    @Operation(summary = "Obtener subcategoría por ID", description = "Obtiene una subcategoría específica")
    public ResponseEntity<?> obtenerProductoCategoria(@PathVariable Long id) {
        try {
            validarId(id);
            ProductoCategoria productoCategoria = productoCategoriaService.buscarPorId(id);
            return ResponseEntity.ok(productoCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/producto-categorias")
    @Operation(summary = "Crear subcategoría", description = "Crea una nueva subcategoría para una categoría existente")
    public ResponseEntity<?> crearProductoCategoria(@RequestBody ProductoCategoria productoCategoria,
            @Parameter(description = "ID de la categoría padre") @RequestParam Long idCategoria) {
        try {
            validarProductoCategoria(productoCategoria);
            ProductoCategoria nuevaProductoCategoria = productoCategoriaService.guardarProductoCategoria(
                    productoCategoria, idCategoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaProductoCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/producto-categorias/{id}")
    @Operation(summary = "Actualizar subcategoría", description = "Actualiza una subcategoría existente")
    public ResponseEntity<?> actualizarProductoCategoria(@PathVariable Long id,
            @RequestBody ProductoCategoria productoCategoria,
            @Parameter(description = "ID de la categoría") @RequestParam(required = false) Long idCategoria) {
        try {
            validarId(id);
            ProductoCategoria productoCategoriaActualizada = productoCategoriaService
                    .actualizarProductoCategoria(id, productoCategoria, idCategoria);
            return ResponseEntity.ok(productoCategoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/producto-categorias/{id}")
    @Operation(summary = "Eliminar subcategoría", description = "Elimina una subcategoría del sistema")
    public ResponseEntity<?> eliminarProductoCategoria(@PathVariable Long id) {
        try {
            validarId(id);
            productoCategoriaService.eliminarProductoCategoria(id);
            return ResponseEntity.ok("Subcategoría eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== MÉTODOS VALIDACIÓN PRIVADOS ========================

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
    }

    private void validarMarca(Marca marca) {
        if (marca == null) {
            throw new IllegalArgumentException("La marca no puede ser nula");
        }
        if (marca.getNombre() == null || marca.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la marca es obligatorio");
        }
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        if (categoria.getNombreCategoria() == null || categoria.getNombreCategoria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
    }

    private void validarEspecificacion(Especificacion especificacion) {
        if (especificacion == null) {
            throw new IllegalArgumentException("La especificación no puede ser nula");
        }
        if (especificacion.getFrecuencia() == null || especificacion.getFrecuencia().isBlank()) {
            throw new IllegalArgumentException("La frecuencia es obligatoria");
        }
    }

    private void validarProductoCategoria(ProductoCategoria productoCategoria) {
        if (productoCategoria == null) {
            throw new IllegalArgumentException("La subcategoría no puede ser nula");
        }
        if (productoCategoria.getNombreSubcategoria() == null
                || productoCategoria.getNombreSubcategoria().isBlank()) {
            throw new IllegalArgumentException("El nombre de la subcategoría es obligatorio");
        }
    }
}
