package SNAKE_PC.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.dto.usuario.ContactoDTO;
import SNAKE_PC.demo.dto.usuario.DireccionDTO;
import SNAKE_PC.demo.dto.usuario.UsuarioRegistroDTO;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/admin")
@Tag(name = "Administrador", description = "Funciones administrativas del sistema")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar-cliente")
    @Operation(summary = "Registrar un nuevo cliente", description = "Crea un nuevo cliente con información de contacto y dirección. Validación completa.")
    public ResponseEntity<?> registrarCliente(
            @Valid @RequestBody UsuarioRegistroDTO usuarioDTO) {
        try {
            // Validar que las contraseñas coincidan
            if (!usuarioDTO.getContrasena().equals(usuarioDTO.getConfirmarContrasena())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Las contraseñas no coinciden");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
            usuario.setCorreo(usuarioDTO.getCorreo());
            usuario.setContrasena(usuarioDTO.getContrasena());
            usuario.setActivo(true);

            // El servicio se encargará de guardar el usuario
            Contacto nuevoCliente = usuarioService.registrarCliente(
                null, usuario, null, usuarioDTO.getConfirmarContrasena(), null, "", "");
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/registrar-usuario")
    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un nuevo usuario con validaciones de contraseña. Validación completa.")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UsuarioRegistroDTO usuarioDTO) {
        try {
            // Validar que las contraseñas coincidan
            if (!usuarioDTO.getContrasena().equals(usuarioDTO.getConfirmarContrasena())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Las contraseñas no coinciden");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
            usuario.setCorreo(usuarioDTO.getCorreo());
            usuario.setContrasena(usuarioDTO.getContrasena());
            usuario.setActivo(true);

            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario, usuarioDTO.getConfirmarContrasena());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/usuario/{id}")
    @Operation(summary = "Obtener datos completos de un usuario", description = "Obtiene toda la información del usuario incluyendo relaciones")
    public ResponseEntity<?> obtenerUsuario(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del usuario debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            Usuario usuario = usuarioService.obtenerDatosDeUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/usuario/{id}")
    @Operation(summary = "Eliminar información de contacto de un usuario", description = "Elimina la información de contacto asociada a un usuario")
    public ResponseEntity<?> eliminarInformacionContacto(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ID del usuario debe ser mayor a 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            var resultado = usuarioService.eliminarInformacionContacto(id);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
