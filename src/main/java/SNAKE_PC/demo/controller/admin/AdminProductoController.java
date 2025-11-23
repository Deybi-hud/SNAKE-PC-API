package SNAKE_PC.demo.controller.admin;

import java.math.BigDecimal;
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
import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.model.producto.ProductoCategoria;
import SNAKE_PC.demo.service.producto.CategoriaService;
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
    private ProductoCategoriaService productoCategoriaService;

    @Autowired
    private ProductoService productoService;


    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> datos) {

        Producto productoParcial = new Producto();
        productoParcial.setId(id); 

        if(datos.containsKey("nombreProducto")) productoParcial.setNombreProducto((String) datos.get("nombreProducto"));
        if(datos.containsKey("stock")) productoParcial.setStock(((Number) datos.get("stock")).intValue());
        if(datos.containsKey("precio")) productoParcial.setPrecio(new BigDecimal(datos.get("precio").toString()));
        if(datos.containsKey("sku")) productoParcial.setSku((String) datos.get("sku"));
        if(datos.containsKey("imagen")) productoParcial.setImagen((String) datos.get("imagen"));

        Marca marcaParcial = new Marca();
        if (datos.containsKey("marcaNombre")) {
            marcaParcial.setMarcaNombre((String) datos.get("marcaNombre"));
        }

        Producto actualizado = productoService.actualizacionParcialProducto(productoParcial, marcaParcial);

        return ResponseEntity.ok(actualizado);
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
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> payload) {
        try{
            Producto producto = new Producto();
            producto.setNombreProducto((String) payload.get("nombreProducto"));
            producto.setSku((String) payload.get("sku"));
            producto.setPrecio(new BigDecimal(payload.get("precio").toString()));
            producto.setStock(((Number) payload.get("stock")).intValue());

            Marca marca = new Marca();
            marca.setMarcaNombre((String) payload.get("marcaNombre"));

            Categoria categoria = new Categoria();
            categoria.setNombreCategoria((String) payload.get("nombreCategoria"));

            ProductoCategoria subCategoria = new ProductoCategoria();
            subCategoria.setNombreSubCategoria((String) payload.get("subCategoria"));

            Producto creado = productoService.guardarProducto(producto, subCategoria, categoria, marca);
        
            return ResponseEntity.status(201).body(creado);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: "+ e.getMessage());

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