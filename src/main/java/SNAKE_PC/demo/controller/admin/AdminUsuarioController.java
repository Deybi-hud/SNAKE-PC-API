package SNAKE_PC.demo.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioContactoService usuarioContactoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

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

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarUsuarioPorCorreo(@RequestParam String correo) {
        try {
            Usuario usuario = usuarioService.obtenerPorCorreo(correo);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{usuarioId}/desactivar")
    public ResponseEntity<?> desactivarCuentaUsuario(@PathVariable Long usuarioId) {
        try {
            usuarioService.desactivarCuenta(usuarioId);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta desactivada exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{usuarioId}/reactivar")
    public ResponseEntity<?> reactivarCuentaUsuario(@PathVariable Long usuarioId) {
        try {
            usuarioService.reactivarCuenta(usuarioId);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Cuenta reactivada exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/contactos/{contactoId}")
    public ResponseEntity<?> obtenerContacto(@PathVariable Long contactoId) {
        try {
            Contacto contacto = usuarioContactoService.obtenerDatosContacto(contactoId);
            return ResponseEntity.ok(contacto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> borrarUsuario(@PathVariable Long usuarioId) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Usuario usuario = usuarioOpt.get();
            Contacto contacto = usuario.getContacto();

            if (contacto != null && contacto.getDireccion() != null) {
                Direccion direccion = contacto.getDireccion();
                direccionRepository.delete(direccion);
            }

            if (contacto != null) {
                contactoRepository.delete(contacto);
            }

            usuarioRepository.delete(usuario);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Usuario eliminado exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al eliminar usuario: " + e.getMessage()));
        }
    }
}