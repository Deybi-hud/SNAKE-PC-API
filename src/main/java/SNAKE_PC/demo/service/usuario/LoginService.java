package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

@Service
public class LoginService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired PasswordEncoder passwordEncoder;

    public Usuario iniciarSesion(String correo, String contrasena){
        Usuario usuario= usuarioRepository.findByCorreo(correo)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        if(!passwordEncoder.matches(contrasena, usuario.getContrasena())){
            throw new RuntimeException("Contraseña incorrecta");
        }
        if (!usuario.isActivo()) {
        throw new RuntimeException("Cuenta desactivada");
        }
        return usuario;
    }
    

}
