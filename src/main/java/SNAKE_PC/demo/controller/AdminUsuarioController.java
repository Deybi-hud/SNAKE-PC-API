package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioContactoService usuarioContactoService;

    @GetMapping("/contactos")
    public ResponseEntity<?> listarTodosLosContactos() {
        try {
            List<Contacto> contactos = usuarioContactoService.findAll();
            return ResponseEntity.ok(contactos);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al obtener contactos: " + e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarioPorCorreo(@RequestParam String correo) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(correo);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{usuarioId}/desactivar")
    public ResponseEntity<?> desactivarCuentaUsuario(@PathVariable Long usuarioId) {
        try {
            usuarioService.desactivarCuenta(usuarioId);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta desactivada exitosamente");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{usuarioId}/reactivar")
    public ResponseEntity<?> reactivarCuentaUsuario(@PathVariable Long usuarioId) {
        try {
            usuarioService.reactivarCuenta(usuarioId);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta reactivada exitosamente");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/contactos/{contactoId}")
    public ResponseEntity<?> obtenerContacto(@PathVariable Long contactoId) {
        try {
            Contacto contacto = usuarioContactoService.obtenerDatosContacto(contactoId);
            return ResponseEntity.ok(contacto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}