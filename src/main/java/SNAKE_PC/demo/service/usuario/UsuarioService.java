package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Usuario crearUsuario(String nombreUsuario,String imagenUsuario, String correo, String contrasena,String confirmarContrasena){
        if(nombreUsuario == null || nombreUsuario.trim().isEmpty()){
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if(usuarioRepository.existsByNombreUsuario(nombreUsuario)){
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        validarCorreo(correo);

        String encriptarContrasena = null;
        validarContrasena(contrasena, confirmarContrasena);
        encriptarContrasena = passwordEncoder.encode(contrasena);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(nombreUsuario);
        nuevoUsuario.setImagenUsuario(imagenUsuario);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setContrasena(encriptarContrasena);
        nuevoUsuario.setActivo(true);

        return usuarioRepository.save(nuevoUsuario);

    }

     public Usuario actualizarContrasena(Long usuarioId,String nuevaContrasena, String confirmarContrasena){
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(()-> new RuntimeException("Usuario no encontrado")); 
        if(nuevaContrasena == null){
            throw new RuntimeException("Debe ingresar una contraseña");
        }
        if(!nuevaContrasena.equals(confirmarContrasena)){
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        validarContrasena(nuevaContrasena, confirmarContrasena);
        usuario.setContrasena(passwordEncoder.encode(confirmarContrasena));
        return usuarioRepository.save(usuario);
    }

    public void validarCorreo(String correo){
        if(correo == null || correo.trim().isEmpty()){
            throw new RuntimeException("El correo no puede estar vacío");
        }
        String formato = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if(!correo.matches(formato)){
            throw new RuntimeException("Formato de correo inválido");
        }
         if(usuarioRepository.existsByCorreo(correo)){
            throw new RuntimeException("El correo ya existe");
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


//------ administrador --------
    public Usuario obtenerPorCorreo(String correo){
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(()-> new RuntimeException("No se encontraron usuarios con ese correo"));

        return usuario;
    }

    
}
