package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolUsuarioService rolUsuariosService;


    public Usuario crearUsuario(Usuario usuario, String confirmarContrasena){
        if(usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isBlank()){
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if(usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())){
            throw new RuntimeException("El nombre de usuario ya existe");
        }
   
        RolUsuario rolUsuario = rolUsuariosService.findByNombre("CLIENTE");
        usuario.setRolUsuario(rolUsuario);
        
        validarCorreo(usuario.getCorreo());
        validarContrasena(usuario.getContrasena(), confirmarContrasena);
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }


    public Usuario actualizarCorreo(Long usuarioId, String correo){
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrad"));
        validarCorreo(correo);
        usuario.setCorreo(correo);
        return usuarioRepository.save(usuario);
    }


    public Usuario actualizarContrasena(Long usuarioId,String nuevaContrasena, String confirmarContrasena){
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(()-> new RuntimeException("Usuario no encontrado")); 
        validarContrasena(nuevaContrasena, confirmarContrasena);
        usuario.setContrasena(passwordEncoder.encode(confirmarContrasena));
        return usuarioRepository.save(usuario);
    }

    public void validarCorreo(String correo){
        if(correo == null || correo.trim().isEmpty()){
            throw new RuntimeException("El correo no puede estar vacío");
        }
        String normalizado = correo.trim().toLowerCase();
        String formato = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if(!normalizado.matches(formato)){
            throw new RuntimeException("Formato de correo inválido");
        }
        if(usuarioRepository.existsByCorreo(normalizado)){
            throw new RuntimeException("El correo ya existe");
        }

    }

    public void validarCorreoParaActualizacion(String correoNuevo, Long usuarioIdActual) {
        if (correoNuevo == null || correoNuevo.trim().isEmpty()) {
            throw new RuntimeException("El correo no puede estar vacío");
        }
        String normalizado = correoNuevo.trim().toLowerCase();
        String formato = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!normalizado.matches(formato)) {
            throw new RuntimeException("Formato de correo inválido");
        }
        if (usuarioRepository.existsByCorreoAndIdNot(normalizado, usuarioIdActual)) {
            throw new RuntimeException("El correo ya está registrado por otro usuario");
        }
    }

    public void validarContrasena(String contrasena, String confirmarContrasena) {
        if(contrasena == null || contrasena.trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        if (!contrasena.equals(confirmarContrasena)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        if(contrasena.length() < 8) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres");
        }
        
        if (!contrasena.matches(".*[A-Z].*")) {
            throw new RuntimeException("Debe contener al menos una mayúscula");
        }
        
        if (!contrasena.matches(".*[a-z].*")) {
            throw new RuntimeException("Debe contener al menos una minúscula");
        }
        
        if (!contrasena.matches(".*\\d.*")) {
            throw new RuntimeException("Debe contener al menos un número");
        }
        
        if (!contrasena.matches(".*[@#$%^&+=!].*")) {
            throw new RuntimeException("Debe contener al menos un carácter especial (@#$%^&+=!)");
        }
        
        if (contrasena.contains(" ")) {
            throw new RuntimeException("No puede contener espacios");
        }
    }

    public void desactivarCuenta(Long usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public void reactivarCuenta(Long usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public Usuario validarActividad(String correo){
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(()-> new RuntimeException("usuario no encontrado"));
        if(!usuario.isActivo()){
            throw new RuntimeException("El usuario está desactivado");
        }
        return usuario;
    }
        
       
//------ administrador --------
    public Usuario obtenerPorCorreo(String correo){
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(()-> new RuntimeException("No se encontraron usuarios con ese correo"));

        return usuario;
    }



    
}
