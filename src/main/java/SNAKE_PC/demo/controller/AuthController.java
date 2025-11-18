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

import SNAKE_PC.demo.dto.usuario.UsuarioLoginDTO;
import SNAKE_PC.demo.service.usuario.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Autenticacion", description = "Gestión de autenticación de usuarios")
public class AuthController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión con correo/nombre de usuario y contraseña")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioLoginDTO loginRequest) {
        try {
            Map<String, Object> mensaje = sessionService.login(loginRequest.getCorreoONombreUsuario(), loginRequest.getContrasena());
            return ResponseEntity.ok(mensaje);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
