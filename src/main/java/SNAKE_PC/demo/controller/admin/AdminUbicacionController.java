package SNAKE_PC.demo.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.service.usuario.ComunaService;
import SNAKE_PC.demo.service.usuario.RegionService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/admin/ubicacion")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUbicacionController {

    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionService regionService;

    @DeleteMapping("/comunas/{comunaId}")
    public ResponseEntity<?> borrarComuna(@PathVariable Long comunaId) {
        try {
            if (!comunaRepository.existsById(comunaId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Comuna no encontrada"));
            }
            comunaRepository.deleteById(comunaId);
            return ResponseEntity.ok(Map.of("mensaje", "Comuna eliminada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
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

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearRegion(@RequestBody Region region) {
        try {
            Region regionCreada = regionService.save(region);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Región creada exitosamente");
            response.put("regionId", regionCreada.getId());
            response.put("nombreRegion", regionCreada.getNombreRegion());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

}
