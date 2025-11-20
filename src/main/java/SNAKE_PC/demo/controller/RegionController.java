package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.service.usuario.RegionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    // ✅ CREAR REGIÓN (Admin)
    @PostMapping
    public ResponseEntity<?> crearRegion(@RequestBody Region region) {
        try {
            Region regionCreada = regionService.save(region);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Región creada exitosamente");
            response.put("regionId", regionCreada.getId());
            response.put("nombreRegion", regionCreada.getNombreRegion());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // ✅ LISTAR TODAS LAS REGIONES
    @GetMapping
    public ResponseEntity<?> listarRegiones() {
        try {
            List<Region> regiones = regionService.findAllRegiones();
            return ResponseEntity.ok(regiones);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener regiones: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ✅ OBTENER REGIÓN POR ID
    @GetMapping("/{regionId}")
    public ResponseEntity<?> obtenerRegion(@PathVariable Long regionId) {
        try {
            Region region = regionService.findById(regionId);
            return ResponseEntity.ok(region);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ✅ OBTENER REGIÓN POR NOMBRE
    @GetMapping("/nombre/{nombreRegion}")
    public ResponseEntity<?> obtenerRegionPorNombre(@PathVariable String nombreRegion) {
        try {
            Region region = regionService.findByNombre(nombreRegion);
            return ResponseEntity.ok(region);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ✅ VERIFICAR SI REGIÓN EXISTE
    @GetMapping("/verificar")
    public ResponseEntity<?> verificarRegionExiste(@RequestParam String nombreRegion) {
        try {
            boolean existe = regionService.existeRegion(nombreRegion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("existe", existe);
            response.put("mensaje", existe ? 
                "La región ya existe" : "Región disponible");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al verificar región: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
