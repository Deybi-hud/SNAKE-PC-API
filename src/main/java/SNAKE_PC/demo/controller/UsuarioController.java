package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;

import java.io.IOException;
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
            @RequestParam(value = "imagenUsuario", required = false) String imagenUsuario,
            @RequestParam String nombreUsuario,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam String confirmarContrasena,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String telefono,
            @RequestParam String calle,
            @RequestParam String numero,
            @RequestParam Long comunaId,
            @RequestParam Long idRol) {
        
        try {
            // Construir objeto Contacto
            Contacto contacto = new Contacto();
            contacto.setNombre(nombre);
            contacto.setApellido(apellido);
            contacto.setTelefono(telefono);
            
            // Construir objeto Usuario
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setCorreo(correo);
            usuario.setContrasena(contrasena);
            
            // Construir objeto Dirección
            Direccion direccion = new Direccion();
            direccion.setCalle(calle);
            direccion.setNumero(numero);
            
            // Registrar cliente con los objetos construidos
            Contacto nuevoContacto = usuarioContactoService.RegistrarCliente(
                contacto, usuario, confirmarContrasena, direccion, comunaId
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                    "mensaje", "Cliente registrado exitosamente",
                    "contactoId", nuevoContacto.getId(),
                    "usuarioId", nuevoContacto.getUsuario().getId(),
                    "imagenUsuario", imagenUsuario
                )
            );
            
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
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            Contacto contacto = usuarioContactoService.obtenerDatosContacto(
                usuario.getContacto().getId()
            );

            return ResponseEntity.ok(Map.of("usuario", usuario, "contacto", contacto));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ ACTUALIZAR MI PERFIL
    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarMiPerfil(
            @RequestBody Contacto contactoActualizado,
            @RequestParam(required = false) String calle,
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) Long comunaId,
            Authentication authentication) {

        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            
            // Construir objeto dirección
            Direccion direccion = new Direccion();
            direccion.setCalle(calle);
            direccion.setNumero(numero);
            
            Contacto contactoActualizadoResult = usuarioContactoService.ActualizarContacto(
                    contactoActualizado, usuario, direccion, comunaId);

            return ResponseEntity.ok(contactoActualizadoResult);

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
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());

            usuarioService.actualizarContrasena(usuario.getId(), nuevaContrasena, confirmarContrasena);

            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ DESACTIVAR MI CUENTA
    @PutMapping("/desactivar-cuenta")
    public ResponseEntity<?> desactivarCuenta(Authentication authentication) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            usuarioService.desactivarCuenta(usuario.getId());
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta desactivada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ REACTIVAR MI CUENTA (si está desactivada)
    @PutMapping("/reactivar-cuenta")
    public ResponseEntity<?> reactivarCuenta(Authentication authentication) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            usuarioService.reactivarCuenta(usuario.getId());
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta reactivada exitosamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
}