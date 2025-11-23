package SNAKE_PC.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.service.usuario.RolUsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRolController {

    @Autowired
    private RolUsuarioService rolService;

    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody RolUsuario rolUsuario) {
        try {
            RolUsuario rolCreado = rolService.save(rolUsuario);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol creado exitosamente");
            response.put("rolId", rolCreado.getId());
            response.put("nombreRol", rolCreado.getNombreRol());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping
    public ResponseEntity<?> listarRoles() {
        try {
            List<RolUsuario> roles = rolService.findAll();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al obtener roles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{rolId}")
    public ResponseEntity<?> obtenerRol(@PathVariable Long rolId) {
        try {
            RolUsuario rol = rolService.findById(rolId);
            return ResponseEntity.ok(rol);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarRolExiste(@RequestParam String nombreRol) {
        try {
            boolean existe = rolService.existeRol(nombreRol);

            Map<String, Object> response = new HashMap<>();
            response.put("existe", existe);
            response.put("mensaje", existe ? "El rol ya existe" : "Rol disponible");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al verificar rol: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}