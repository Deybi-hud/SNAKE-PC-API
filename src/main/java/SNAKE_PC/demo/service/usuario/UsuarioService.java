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

    public Usuario save(Usuario usuario,String confirmarContrasena){
        if(usuario.getNombreUsuario() == null || usuario.getNombreUsuario().trim().isEmpty()){
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if(usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())){
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if(usuario.getCorreo() != null){
            validarCorreo(usuario.getCorreo());
        } else {
            throw new RuntimeException("El correo es obligatorio");
        }
        
        if(usuario.getContrasena() != null){
            validarContrasena(usuario, confirmarContrasena);
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        } else {
            throw new RuntimeException("La contraseña es obligatoria");
        }
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

    public void validarContrasena(Usuario usuario, String confirmarContrasena) {
        String password = usuario.getContrasena();
        
        if(password == null || password.trim().isEmpty()) {
            throw new RuntimeException("La contraseña no puede estar vacía");
        }

        if (!password.equals(confirmarContrasena)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        if(password.length() < 8) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new RuntimeException("Debe contener al menos una mayúscula");
        }
        
        if (!password.matches(".*[a-z].*")) {
            throw new RuntimeException("Debe contener al menos una minúscula");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new RuntimeException("Debe contener al menos un número");
        }
        
        if (!password.matches(".*[@#$%^&+=!].*")) {
            throw new RuntimeException("Debe contener al menos un carácter especial (@#$%^&+=!)");
        }
        
        if (password.contains(" ")) {
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

    public Usuario actualizarContrasena(Long usuarioId,String nuevaContrasena, String confirmarContrasena){
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(()-> new RuntimeException("Usuario no encontrado")); 
        if(nuevaContrasena == null){
            throw new RuntimeException("Debe ingresar una contraseña");
        }
        if(!nuevaContrasena.equals(confirmarContrasena)){
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        usuario.setContrasena(passwordEncoder.encode(confirmarContrasena));
        return usuarioRepository.save(usuario);
    }


//------ administrador --------
    public Usuario obtenerPorCorreo(String correo){
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(()-> new RuntimeException("No se encontraron usuarios con ese correo"));

        return usuario;
    }

    
}
