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

    // ✅ LISTAR TODOS LOS CONTACTOS (Admin)
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

    // ✅ BUSCAR USUARIO POR CORREO (Admin)
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

    // ✅ DESACTIVAR CUENTA DE USUARIO (Admin)
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

    // ✅ REACTIVAR CUENTA DE USUARIO (Admin)
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

    // ✅ OBTENER CONTACTO POR ID (Admin)
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