package SNAKE_PC.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "/registrar", consumes = "multipart/form-data")
    public ResponseEntity<?> registrarCliente(
            @RequestPart(value = "imagenUsuario", required = false) String imagenUsuario,
            @RequestPart Contacto contacto,
            @RequestPart Usuario usuario,
            @RequestPart Direccion direccion,
            @RequestPart Long comunaId) {

        try {
            Contacto nuevoContacto = usuarioContactoService.RegistrarCliente(
                    contacto, usuario, "", direccion, comunaId);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "mensaje", "Cliente registrado exitosamente",
                            "contactoId", nuevoContacto.getId(),
                            "usuarioId", nuevoContacto.getUsuario().getId(),
                            "imagenUsuario", imagenUsuario));

        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al procesar: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerMiPerfil(Authentication authentication) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            Contacto contacto = usuarioContactoService.obtenerDatosContacto(
                    usuario.getContacto().getId());

            return ResponseEntity.ok(Map.of("usuario", usuario, "contacto", contacto));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> actualizarMiPerfil(
            @RequestPart Contacto contactoActualizado,
            @RequestPart(required = false) String calle,
            @RequestPart(required = false) String numero,
            @RequestPart(required = false) Long comunaId,
            Authentication authentication) {

        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());

            Direccion direccion = new Direccion();
            direccion.setCalle(calle);
            direccion.setNumero(numero);

            Contacto contactoActualizadoResult = usuarioContactoService.ActualizarContacto(
                    contactoActualizado, usuario, direccion, comunaId, authentication.getName());

            return ResponseEntity.ok(contactoActualizadoResult);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(
            @RequestPart String nuevaContrasena,
            @RequestPart String confirmarContrasena,
            Authentication authentication) {

        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());

            usuarioService.actualizarContrasena(usuario.getId(), nuevaContrasena, confirmarContrasena);

            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada exitosamente"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/desactivar-cuenta")
    public ResponseEntity<?> desactivarCuenta(Authentication authentication) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            usuarioService.desactivarCuenta(usuario.getId());
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta desactivada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/reactivar-cuenta")
    public ResponseEntity<?> reactivarCuenta(Authentication authentication) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(authentication.getName());
            usuarioService.reactivarCuenta(usuario.getId());
            return ResponseEntity.ok(Map.of("mensaje", "Cuenta reactivada exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}