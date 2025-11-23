package SNAKE_PC.demo.controller.publico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.service.usuario.ComunaService;
import SNAKE_PC.demo.service.usuario.RegionService;

@RestController
@RequestMapping("/api/v1/ubicaciones")
public class UbicacionController {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private RegionService regionService;

    @GetMapping("/comunas")
    public ResponseEntity<?> obtenerTodasLasComunas() {
        try {
            List<Comuna> comunas = comunaService.findAll();
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener comunas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/regiones/{regionId}/comunas")
    public ResponseEntity<?> obtenerComunasPorRegion(@PathVariable Long regionId) {
        try {
            List<Comuna> comunas = comunaService.findComunasByRegion(regionId);
            return ResponseEntity.ok(comunas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener comunas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/comunas/buscar")
    public ResponseEntity<?> buscarComunasPorNombre(@RequestParam String nombre) {
        try {
            Comuna comuna = comunaService.findByNombre(nombre);
            return ResponseEntity.ok(comuna);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

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

    @GetMapping("/{regionId}")
    public ResponseEntity<?> obtenerRegion(@PathVariable Long regionId) {
        try {
            Region region = regionService.findById(regionId);
            return ResponseEntity.ok(region);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/nombre/{nombreRegion}")
    public ResponseEntity<?> obtenerRegionPorNombre(@PathVariable String nombreRegion) {
        try {
            Region region = regionService.findByNombre(nombreRegion);
            return ResponseEntity.ok(region);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
