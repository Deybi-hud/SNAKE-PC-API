package SNAKE_PC.demo.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.service.producto.CategoriaService;
import SNAKE_PC.demo.service.producto.EspecificacionService;
import SNAKE_PC.demo.service.producto.MarcaService;
import SNAKE_PC.demo.service.producto.ProductoCategoriaService;
import SNAKE_PC.demo.service.producto.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/admin/catalogo")
@Tag(name = "Admin - Catalogo de Productos", description = "Gestion de marcas, categorias, especificaciones y subCategorias")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductoController {

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private EspecificacionService especificacionService;

    @Autowired
    private ProductoCategoriaService productoCategoriaService;

    @Autowired
    private ProductoService productoService;

    // ======================== CRUD PRODUCTOS ========================

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

    // ======================== CRUD MARCAS ========================

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
            marcaService.eliminarMarca(id);
            return ResponseEntity.ok("Marca eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== CRUD CATEGORIAS ========================

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorias", description = "Obtiene todas las categorias")
    public ResponseEntity<?> listarCategorias() {
        List<Categoria> categorias = categoriaService.buscarTodos();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/categorias/{id}")
    @Operation(summary = "Obtener categoria por ID", description = "Obtiene una categoria específica")
    public ResponseEntity<?> obtenerCategoria(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.buscarPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/categorias")
    @Operation(summary = "Crear categoria", description = "Crea una nueva categoria")
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria nuevaCategoria = categoriaService.guardarCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/categorias/{id}")
    @Operation(summary = "Actualizar categoria", description = "Actualiza una categoria existente")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        try {
            Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria del sistema")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.ok("Categoria eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== CRUD ESPECIFICACIONES ========================

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
    @Operation(summary = "Obtener especificacion por ID", description = "Obtiene una especificacion específica")
    public ResponseEntity<?> obtenerEspecificacion(@PathVariable Long id) {
        try {
            Especificacion especificacion = especificacionService.buscarPorId(id);
            return ResponseEntity.ok(especificacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/especificaciones")
    @Operation(summary = "Crear especificacion", description = "Crea una nueva especificacion")
    public ResponseEntity<?> crearEspecificacion(@RequestBody Especificacion especificacion) {
        try {
            Especificacion nuevaEspecificacion = especificacionService.guardarEspecificacion(especificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEspecificacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/especificaciones/{id}")
    @Operation(summary = "Actualizar especificacion", description = "Actualiza una especificacion existente")
    public ResponseEntity<?> actualizarEspecificacion(@PathVariable Long id,
            @RequestBody Especificacion especificacion) {
        try {
            Especificacion especificacionActualizada = especificacionService.actualizarEspecificacion(id,
                    especificacion);
            return ResponseEntity.ok(especificacionActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/especificaciones/{id}")
    @Operation(summary = "Eliminar especificacion", description = "Elimina una especificacion del sistema")
    public ResponseEntity<?> eliminarEspecificacion(@PathVariable Long id) {
        try {
            especificacionService.eliminarEspecificacion(id);
            return ResponseEntity.ok("especificacion eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // ======================== CRUD SUBCATEOGORIA ========================

    @GetMapping("/producto-categorias")
    @Operation(summary = "Listar subCategorias de productos", description = "Obtiene todas las subCategorias")
    public ResponseEntity<?> listarProductoCategorias() {
        List<ProductoCategoria> productoCategorias = productoCategoriaService.buscarTodos();
        if (productoCategorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productoCategorias);
    }

    @GetMapping("/producto-categorias/{id}")
    @Operation(summary = "Buscar subCategoria por ID", description = "Obtiene una subCategoria en especifico")
    public ResponseEntity<?> obtenerProductoCategoria(@PathVariable Long id) {
        try {
            ProductoCategoria productoCategoria = productoCategoriaService.buscarPorId(id);
            return ResponseEntity.ok(productoCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/producto-categorias", consumes = "multipart/form-data")
    @Operation(summary = "Crear subCategoria", description = "Crea una nueva subCategoria que ya existe")
    public ResponseEntity<?> crearProductoCategoria(@RequestPart ProductoCategoria productoCategoria,
            @Parameter(description = "ID de la categoría padre") @RequestPart Categoria categoria) {
        try {
            ProductoCategoria nuevaProductoCategoria = productoCategoriaService.guardarProductoCategoria(
                    productoCategoria, categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaProductoCategoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping(value = "/producto-categorias/{id}", consumes = "multipart/form-data")
    @Operation(summary = "Actualizar subCategoria", description = "Actualiza una subCategoria que ya existe")
    public ResponseEntity<?> actualizarProductoCategoria(@PathVariable Long id,
            @RequestPart ProductoCategoria productoCategoria,
            @Parameter(description = "Categoria") @RequestPart(required = false) Categoria categoria) {
        try {
            ProductoCategoria productoCategoriaActualizada = productoCategoriaService
                    .actualizarProductoCategoria(productoCategoria.getNombreSubCategoria(), categoria);
            return ResponseEntity.ok(productoCategoriaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/producto-categorias/{id}")
    @Operation(summary = "Eliminar subCategoria", description = "Elimina una subCategoria del sistema")
    public ResponseEntity<?> eliminarProductoCategoria(@PathVariable Long id) {
        try {
            productoCategoriaService.eliminarProductoCategoria(id);
            return ResponseEntity.ok("subCategoria eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}