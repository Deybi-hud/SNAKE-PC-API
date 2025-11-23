package SNAKE_PC.demo.controller.cliente;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import SNAKE_PC.demo.service.ImagenService;
import SNAKE_PC.demo.service.usuario.UsuarioContactoService;
import SNAKE_PC.demo.service.usuario.UsuarioService;

@RestController
@RequestMapping("/api/v1/perfil")
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioContactoService usuarioContactoService;

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
    public ResponseEntity<?> actualizarMiPerfil(@RequestBody Map<String, Object> datos,
            @AuthenticationPrincipal Usuario usuarioLogueado) {

        try {

            Contacto contactoActualizado = new Contacto();
            contactoActualizado.setNombre((String) datos.get("nombre"));
            contactoActualizado.setApellido((String) datos.get("apellido"));
            contactoActualizado.setTelefono((String) datos.get("telefono"));

            Direccion direccionActualizada = new Direccion();
            direccionActualizada.setCalle((String) datos.get("calle"));
            direccionActualizada.setNumero((String) datos.get("numero"));

            String nuevoCorreo = (String)datos.get("nuevoCorreo");

            Long idComuna = Long.valueOf(datos.get("idComuna").toString());
            usuarioContactoService.ActualizarContacto(contactoActualizado, direccionActualizada, 
                nuevoCorreo, idComuna, usuarioLogueado.getId());

            return ResponseEntity.ok(Map.of("mensaje", "Perfil actualizado con éxito"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @RestController
    @RequestMapping("/usuario")
    public class UsuarioController {

        @Autowired
        private ImagenService imagenService;

        @Autowired
        private UsuarioRepository usuarioRepository;

        @PutMapping("/subir-foto")
        public ResponseEntity<?> subirFoto(
                @RequestParam("file") MultipartFile file,
                Authentication authentication) {

            try {

                String url = imagenService.subirImagen(file);
                Usuario usuario = (Usuario) authentication.getPrincipal();
                usuario.setImagenUsuario(url);
                usuarioRepository.save(usuario);

                return ResponseEntity.ok(Map.of(
                        "mensaje", "Foto actualizada correctamente",
                        "url", url
                ));

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", e.getMessage()
                ));
            }
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
