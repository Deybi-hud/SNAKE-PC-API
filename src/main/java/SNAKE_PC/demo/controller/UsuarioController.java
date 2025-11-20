package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioContactoService usuarioContactoService;

    // ✅ REGISTRAR CLIENTE (público - sin autenticación)
    @PostMapping(value = "/registrar", consumes = "multipart/form-data")
    public ResponseEntity<?> registrarCliente(
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam String nombreUsuario,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam String confirmarContrasena,
            @RequestParam String calle,
            @RequestParam String numero,
            @RequestParam Long comunaId,
            @RequestParam Long idRol,
            @ModelAttribute Contacto contacto) {
        
        try {
            Contacto nuevoContacto = usuarioContactoService.RegistrarCliente(
                contacto, imagen, nombreUsuario, correo, contrasena, 
                confirmarContrasena, calle, numero, comunaId, idRol
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Cliente registrado exitosamente");
            response.put("contactoId", nuevoContacto.getId());
            response.put("usuarioId", nuevoContacto.getUsuario().getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ OBTENER MI PERFIL
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerMiPerfil(Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Usuario usuario = usuarioService.obtenerPorCorreo(correoUsuario);
            
            // Buscar el contacto asociado al usuario
            Contacto contacto = usuarioContactoService.obtenerDatosContacto(
                usuario.getContactos().get(0).getId()
            );
            
            Map<String, Object> perfil = new HashMap<>();
            perfil.put("usuario", usuario);
            perfil.put("contacto", contacto);
            
            return ResponseEntity.ok(perfil);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ ACTUALIZAR MI PERFIL
    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarMiPerfil(
            @RequestBody Contacto datosActualizados,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) Long comunaId,
            Authentication authentication) {
        
        try {
            String correoUsuario = authentication.getName();
            Usuario usuario = usuarioService.obtenerPorCorreo(correoUsuario);
            
            // Obtener el ID del contacto del usuario
            Long contactoId = usuario.getContactos().get(0).getId();
            
            Contacto contactoActualizado = usuarioContactoService.ActualizarContacto(
                contactoId, datosActualizados, calle, numero, comunaId
            );
            
            return ResponseEntity.ok(contactoActualizado);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ CAMBIAR MI CONTRASEÑA
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(
            @RequestParam String nuevaContrasena,
            @RequestParam String confirmarContrasena,
            Authentication authentication) {
        
        try {
            String correoUsuario = authentication.getName();
            Usuario usuario = usuarioService.obtenerPorCorreo(correoUsuario);
            
            Usuario usuarioActualizado = usuarioService.actualizarContrasena(
                usuario.getId(), nuevaContrasena, confirmarContrasena
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Contraseña actualizada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ DESACTIVAR MI CUENTA
    @PutMapping("/desactivar-cuenta")
    public ResponseEntity<?> desactivarCuenta(Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Usuario usuario = usuarioService.obtenerPorCorreo(correoUsuario);
            
            usuarioService.desactivarCuenta(usuario.getId());
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta desactivada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ REACTIVAR MI CUENTA (si está desactivada)
    @PutMapping("/reactivar-cuenta")
    public ResponseEntity<?> reactivarCuenta(Authentication authentication) {
        try {
            String correoUsuario = authentication.getName();
            Usuario usuario = usuarioService.obtenerPorCorreo(correoUsuario);
            
            usuarioService.reactivarCuenta(usuario.getId());
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta reactivada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}