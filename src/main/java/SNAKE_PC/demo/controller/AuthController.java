package SNAKE_PC.demo.controller;

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

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Autenticacion", description = "Aqui esta el autenticacion")
public class AuthController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    @Operation(summary = "Esta api permite iniciar sesion", description = "esta api se encarga de iniciar sesion")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            Map<String, Object> mensaje = sessionService.login(usuario.getCorreo(), usuario.getContrasena());
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
