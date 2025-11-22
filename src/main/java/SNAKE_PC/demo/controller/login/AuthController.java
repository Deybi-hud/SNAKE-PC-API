package SNAKE_PC.demo.controller.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.LoginService;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AuthenticationManager authenticationManager;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioRequest) {
        try {
            if (usuarioRequest.getCorreo() == null || usuarioRequest.getCorreo().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El correo es obligatorio"));
            }

            if (usuarioRequest.getContrasena() == null || usuarioRequest.getContrasena().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "La contraseña es obligatoria"));
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuarioRequest.getCorreo(),
                            usuarioRequest.getContrasena()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Usuario usuario = loginService.obtenerPorCorreo(usuarioRequest.getCorreo());

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("usuario", mapearUsuarioResponse(usuario));

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Error en autenticación: " + e.getMessage()));
        }
    }

    private Map<String, Object> mapearUsuarioResponse(Usuario usuario) {
        Map<String, Object> usuarioResponse = new HashMap<>();
        usuarioResponse.put("id", usuario.getId());
        usuarioResponse.put("nombreUsuario", usuario.getNombreUsuario());
        usuarioResponse.put("correo", usuario.getCorreo());
        usuarioResponse.put("imagenUsuario", usuario.getImagenUsuario());
        usuarioResponse.put("activo", usuario.isActivo());

        if (usuario.getRolUsuario() != null) {
            usuarioResponse.put("rol", usuario.getRolUsuario().getNombreRol());
        }

        return usuarioResponse;
    }
}
