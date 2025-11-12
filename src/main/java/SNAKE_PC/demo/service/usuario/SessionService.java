package SNAKE_PC.demo.service.usuario;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SessionService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<Long, String> sesionesActivas = new ConcurrentHashMap<>();


//-------------------------------------------- Login Service -------------------------------------------------

    public Map <String, Object> login(String usernameOrEmail, String contrasena){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(usernameOrEmail)
            .or(()-> usuarioRepository.findByEmail(usernameOrEmail));
        if(usuarioOpt.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        if(!usuario.isActivo()){
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("cuentaDesactivada", true);
            response.put("usuarioId",usuario.getId());
            response.put("mensaje", "Tu cuenta ha sido eliminada. ¿Quieres recuperarla?");
            return response;
        }
        if(!passwordEncoder.matches(contrasena, usuario.getContrasena())){
            throw new RuntimeException("Contraseña incorrecta");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("usuarioId",usuario.getId());
        response.put("nombreUsuario", usuario.getNombreUsuario());
        response.put("correo",usuario.getCorreo());
        response.put("mensaje", "Login exitoso");
        response.put("success", true);
        
        return response;
    }

//-------------------------------------------- Logout Service --------------------------------------------------

    public Map<String, Object> cerrarSesion(Long usuarioId){
        if(sesionesActivas.containsKey(usuarioId)){
            sesionesActivas.remove(usuarioId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Sesión cerrada correctamente");
            return response;
        }
        throw new RuntimeException("No hay sesión activa para este usuario");
    }

//------------------------------------------- Desactivar Cuenta ------------------------------------------------

  public Map<String, Object> desactivarCuenta(Long id, String token){
        String tokenActivo = sesionesActivas.get(id);
        if(tokenActivo == null || tokenActivo.equals(token)){
            throw new RuntimeException("No tienes permiso para desactivar esta cuenta");
        }
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        if(!usuario.isActivo()){
            throw new RuntimeException("La cuenta ya está desactivada");
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        if(verificarSesionActiva(id)){
           cerrarSesion(id);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("mensaje","Cuenta desactivada correctamente");
        response.put("usuarioId",id);
        response.put("fechaDesactivacion", LocalDateTime.now());

        return response;
    }

    public boolean verificarSesionActiva(Long usuarioId){
        return sesionesActivas.containsKey(usuarioId);
    }

//------------------------------------------- Listar sesiones por actividad ---------------------------------------

}
