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
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.service.producto.MarcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/marcas")
@Tag(name = "Marcas", description = "Gestión de marcas de productos")
public class MarcaController {

    @Autowired
    private MarcaService marcaService;

    @GetMapping
    @Operation(summary = "Obtener todas las marcas", description = "Obtiene la lista de todas las marcas registradas")
    public ResponseEntity<List<Marca>> listar() {
        List<Marca> marcas = marcaService.buscarTodos();
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener marca por ID", description = "Obtiene una marca específica por su ID")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            if (id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            Marca marca = marcaService.buscarPorId(id);
            return ResponseEntity.ok(marca);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Crear nueva marca", description = "Crea una nueva marca en el sistema")
    public ResponseEntity<?> crear(@RequestBody Marca marca) {
        try {
            if (marca == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La marca no puede ser nula");
            }
            if (marca.getNombre() == null || marca.getNombre().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de la marca es obligatorio");
            }
            Marca nuevaMarca = marcaService.guardarMarca(marca);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMarca);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza los datos de una marca existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Marca marca) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            if (marca == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La marca no puede ser nula");
            }
            Marca marcaActualizada = marcaService.actualizarMarca(id, marca);
            return ResponseEntity.ok(marcaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina una marca del sistema")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El ID debe ser mayor a 0");
            }
            marcaService.eliminarMarca(id);
            return ResponseEntity.ok("Marca eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
