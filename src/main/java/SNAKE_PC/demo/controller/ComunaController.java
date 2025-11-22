package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.service.usuario.ComunaService;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/ubicacion")
public class ComunaController {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaRepository comunaRepository;

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

    @GetMapping("/comunas/{comunaId}")
    public ResponseEntity<?> obtenerComunaPorId(@PathVariable Long comunaId) {
        try {
            Comuna comuna = comunaService.findById(comunaId);
            return ResponseEntity.ok(comuna);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/comunas")
    public ResponseEntity<?> crearComuna(
            @RequestParam String nombreComuna,
            @RequestParam Long regionId) {
        try {
            Comuna comuna = new Comuna();
            comuna.setNombreComuna(nombreComuna);
            Comuna comunaCreada = comunaService.save(comuna, regionId);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Comuna creada exitosamente");
            response.put("comunaId", comunaCreada.getId());
            response.put("nombreComuna", comunaCreada.getNombreComuna());
            response.put("region", comunaCreada.getRegion().getNombreRegion());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/comunas/buscar")
    public ResponseEntity<?> buscarComunasPorNombre(@RequestParam String nombre) {
        try {
            Comuna comuna = comunaService.findByNombre(nombre);
            return ResponseEntity.ok(comuna);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/comunas/verificar")
    public ResponseEntity<?> verificarComunaExiste(
            @RequestParam String nombreComuna,
            @RequestParam Long regionId) {
        try {
            boolean existe = comunaService.existeComuna(nombreComuna, regionId);

            Map<String, Object> response = new HashMap<>();
            response.put("existe", existe);
            response.put("mensaje", existe ? "La comuna ya existe en esta región" : "Comuna disponible");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al verificar comuna: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/comunas/{comunaId}")
    public ResponseEntity<?> borrarComuna(@PathVariable Long comunaId) {
        try {
            if (!comunaRepository.existsById(comunaId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Comuna no encontrada"));
            }
            comunaRepository.deleteById(comunaId);
            return ResponseEntity.ok(Map.of("mensaje", "Comuna eliminada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}