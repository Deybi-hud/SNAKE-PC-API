package SNAKE_PC.demo.controller.login;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.LoginService;
import SNAKE_PC.demo.service.usuario.UsuarioService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @RequestParam String confirmarContrasena) {
        try {
            usuarioService.crearUsuario(usuario, confirmarContrasena);
            return ResponseEntity.ok(usuario);
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

            Usuario usuario = loginService.iniciarSesion(
                    usuarioRequest.getCorreo(),
                    usuarioRequest.getContrasena());

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Login exitoso");
            response.put("usuario", mapearUsuarioResponse(usuario));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Correo o contraseña incorrectos"));
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
