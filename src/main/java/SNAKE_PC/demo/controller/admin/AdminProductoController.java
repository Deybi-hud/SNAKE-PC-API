package SNAKE_PC.demo.controller.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/admin/producto")
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

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto completamente", description = "Actualiza completamente un producto existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        try {
            Producto producto = productoService.buscarPorId(id);
            producto.setNombreProducto((String) datos.getOrDefault("nombreProducto", producto.getNombreProducto()));
            producto.setSku((String) datos.getOrDefault("sku", producto.getSku()));
            producto.setPrecio(new BigDecimal(datos.getOrDefault("precio", producto.getPrecio()).toString()));
            producto.setStock(((Number) datos.getOrDefault("stock", producto.getStock())).intValue());
            if (datos.containsKey("peso") && datos.get("peso") != null) {
                producto.setPeso((String) datos.get("peso"));
            }

            Marca marca = null;
            Especificacion especificacion = null;
            ProductoCategoria productoCategoria = null;
            Categoria categoria = null;

            Producto updateProducto = productoService.guardarProducto(
                    producto, productoCategoria, categoria, marca, especificacion, null, null);
            return ResponseEntity.ok(updateProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un producto", description = "Actualiza parcialmente un producto existente")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> datos) {
        try {
            Producto producto = productoService.buscarPorId(id);

            if (datos.containsKey("nombreProducto")) {
                producto.setNombreProducto((String) datos.get("nombreProducto"));
            }
            if (datos.containsKey("precio")) {
                producto.setPrecio(new BigDecimal(datos.get("precio").toString()));
            }
            if (datos.containsKey("stock")) {
                producto.setStock(((Number) datos.get("stock")).intValue());
            }
            if (datos.containsKey("sku")) {
                producto.setSku((String) datos.get("sku"));
            }
            if (datos.containsKey("peso")) {
                producto.setPeso((String) datos.get("peso"));
            }

            Marca marca = null;
            Especificacion especificacion = null;

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

    @PostMapping
@Operation(summary = "Crear nuevo producto", description = "Solo admin")
public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
    try {
        // === PRODUCTO ===
        Producto producto = new Producto();
        producto.setNombreProducto((String) payload.get("nombreProducto"));
        producto.setSku((String) payload.get("sku"));
        producto.setPrecio(new BigDecimal(payload.get("precio").toString()));
        producto.setStock(((Number) payload.get("stock")).intValue());

        // === MARCA ===
        Marca marca = new Marca();
        marca.setMarcaNombre((String) payload.get("marcaNombre"));

        // === ESPECIFICACIÓN ===
        Especificacion especificacion = new Especificacion();
        especificacion.setFrecuencia((String) payload.get("frecuencia"));
        especificacion.setCapacidadAlmacenamiento((String) payload.get("capacidadAlmacenamiento"));
        especificacion.setConsumo((String) payload.get("consumo"));

        // === CATEGORÍA Y SUBCATEGORÍA ===
        ProductoCategoria productoCategoria = new ProductoCategoria();
        productoCategoria.setNombreSubCategoria((String) payload.get("nombreSubCategoria"));

        Categoria categoria = new Categoria();
        categoria.setNombreCategoria((String) payload.get("nombreCategoria"));

        // === IMÁGENES → LO MÁS IMPORTANTE: LE MANDAMOS LA LISTA TAL CUAL ===
        List<String> urlsImagenes = (List<String>) payload.get("urlsImagenes");
        // Si viene nulo, lo dejamos vacío para que no explote
        if (urlsImagenes == null) {
            urlsImagenes = new ArrayList<>();
        }

        // LLAMADA AL SERVICE (100% COMPATIBLE CON TU MÉTODO)
        Producto creado = productoService.guardarProducto(
            producto,
            productoCategoria,
            categoria,
            marca,
            especificacion,
            null,  // idCategoria (no lo usas, pasas el objeto categoria)
            urlsImagenes   // ← AQUÍ LE LLEGA PERFECTO AL imagenService
        );

        log.info("Producto creado SIN DTO → ID: {} | {} imágenes", creado.getId(), urlsImagenes.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);

    } catch (Exception e) {
        log.error("Error creando producto: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(Map.of("error", "No se pudo crear el producto", "detalle", e.getMessage()));
    }
}

    // ======================== CRUD MARCAS ========================

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