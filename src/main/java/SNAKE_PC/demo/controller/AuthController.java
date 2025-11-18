package SNAKE_PC.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Autenticacion", description = "Aqui esta el autenticacion")
public class AuthController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    @Operation(summary = "Esta api permite iniciar sesion", description = "esta api se encarga de iniciar sesion")
    public ResponseEntity<?> login(@Valid @RequestBody Usuario usuario) {
        try {

            if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El correo es obligatorio");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "La contraseña es obligatoria");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Map<String, Object> mensaje = sessionService.login(usuario.getCorreo(), usuario.getContrasena());
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
