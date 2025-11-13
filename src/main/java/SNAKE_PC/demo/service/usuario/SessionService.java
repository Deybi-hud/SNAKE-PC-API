package SNAKE_PC.demo.service.usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;

@SuppressWarnings("null")
@Service
@Transactional
public class SessionService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<Long, String> sesionesActivas = new ConcurrentHashMap<>();
    private Map<Long, LocalDateTime> expiracionSesiones = new ConcurrentHashMap<>();

    // ---------------------------- LOGIN SERVICE -------------------------------------------------
    public Map<String, Object> login(String usernameOrEmail, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(usernameOrEmail)
            .or(() -> usuarioRepository.findByCorreo(usernameOrEmail));
            
        if(usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        
        if(!usuario.isActivo()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("cuentaDesactivada", true);
            response.put("usuarioId", usuario.getId());
            response.put("mensaje", "Tu cuenta está desactivada. ¿Quieres reactivarla?");
            return response;
        }
        
        if(!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Crear sesión
        String sessionId = generarSessionId();
        sesionesActivas.put(usuario.getId(), sessionId);
        expiracionSesiones.put(usuario.getId(), LocalDateTime.now().plusHours(2));

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("usuarioId", usuario.getId());
        response.put("nombreUsuario", usuario.getNombreUsuario());
        response.put("correo", usuario.getCorreo());
        response.put("sessionId", sessionId);
        response.put("mensaje", "Login exitoso");
        
        return response;
    }

    // ---------------------------- LOGOUT SERVICE -------------------------------------------------
    public Map<String, Object> cerrarSesion(Long usuarioId) {
        if(sesionesActivas.containsKey(usuarioId)) {
            sesionesActivas.remove(usuarioId);
            expiracionSesiones.remove(usuarioId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mensaje", "Sesión cerrada correctamente");
            return response;
        }
        throw new RuntimeException("No hay sesión activa para este usuario");
    }

    // ---------------------------- DESACTIVAR CUENTA ---------------------------------------------
    public Map<String, Object> desactivarCuenta(Long id, String sessionId) {
        // Verificar que la sesión pertenece al usuario que desactiva
        String sessionIdActivo = sesionesActivas.get(id);
        if(sessionIdActivo == null || !sessionIdActivo.equals(sessionId)) {
            throw new RuntimeException("No tienes permiso para desactivar esta cuenta");
        }
        
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(!usuario.isActivo()) {
            throw new RuntimeException("La cuenta ya está desactivada");
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        // Cerrar sesión
        if(verificarSesionActiva(id)) {
            cerrarSesion(id);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("mensaje", "Cuenta desactivada correctamente");
        response.put("usuarioId", id);
        response.put("fechaDesactivacion", LocalDateTime.now());

        return response;
    }

    // ---------------------------- VERIFICAR SESIÓN ACTIVA ---------------------------------------
    public boolean verificarSesionActiva(Long usuarioId) {
        if(!sesionesActivas.containsKey(usuarioId)) {
            return false;
        }
        
        // Verificar expiración
        LocalDateTime expiracion = expiracionSesiones.get(usuarioId);
        if(expiracion != null && LocalDateTime.now().isAfter(expiracion)) {
            sesionesActivas.remove(usuarioId);
            expiracionSesiones.remove(usuarioId);
            return false;
        }
        
        return true;
    }

    // ---------------------------- OBTENER INFORMACIÓN DE SESIÓN --------------------------------
    public Map<String, Object> obtenerInfoSesion(Long usuarioId) {
        if(!verificarSesionActiva(usuarioId)) {
            throw new RuntimeException("Sesión no activa o expirada");
        }
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        Map<String, Object> info = new HashMap<>();
        info.put("usuarioId", usuario.getId());
        info.put("nombreUsuario", usuario.getNombreUsuario());
        info.put("correo", usuario.getCorreo());
        info.put("sessionId", sesionesActivas.get(usuarioId));
        info.put("expiracion", expiracionSesiones.get(usuarioId));
        info.put("activo", usuario.isActivo());
        
        return info;
    }

    // ---------------------------- RENOVAR SESIÓN ------------------------------------------------
    public Map<String, Object> renovarSesion(Long usuarioId, String sessionId) {
        if(!verificarSesionActiva(usuarioId)) {
            throw new RuntimeException("Sesión no activa");
        }
        
        String sessionIdActivo = sesionesActivas.get(usuarioId);
        if(!sessionIdActivo.equals(sessionId)) {
            throw new RuntimeException("Token de sesión inválido");
        }
        
        // Renovar expiración
        expiracionSesiones.put(usuarioId, LocalDateTime.now().plusHours(2));
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("mensaje", "Sesión renovada");
        response.put("nuevaExpiracion", expiracionSesiones.get(usuarioId));
        
        return response;
    }

    // ---------------------------- LISTAR SESIONES ACTIVAS ---------------------------------------
    public List<Map<String, Object>> obtenerSesionesActivas() {
        List<Map<String, Object>> sesiones = new ArrayList<>();
        
        for(Map.Entry<Long, String> entry : sesionesActivas.entrySet()) {
            if(verificarSesionActiva(entry.getKey())) {
                Map<String, Object> sesionInfo = new HashMap<>();
                sesionInfo.put("usuarioId", entry.getKey());
                sesionInfo.put("sessionId", entry.getValue());
                sesionInfo.put("expiracion", expiracionSesiones.get(entry.getKey()));
                sesiones.add(sesionInfo);
            }
        }
        
        return sesiones;
    }

    // ---------------------------- LIMPIAR SESIONES EXPIRADAS ------------------------------------
    public void limpiarSesionesExpiradas() {
        List<Long> sesionesExpiradas = new ArrayList<>();
        
        for(Map.Entry<Long, LocalDateTime> entry : expiracionSesiones.entrySet()) {
            if(LocalDateTime.now().isAfter(entry.getValue())) {
                sesionesExpiradas.add(entry.getKey());
            }
        }
        
        for(Long usuarioId : sesionesExpiradas) {
            sesionesActivas.remove(usuarioId);
            expiracionSesiones.remove(usuarioId);
        }
    }

    // ---------------------------- MÉTODOS AUXILIARES --------------------------------------------
    private String generarSessionId() {
        return "SESS_" + System.currentTimeMillis() + "_" + 
               String.valueOf(Math.random()).substring(2, 15);
    }

    public boolean validarSession(Long usuarioId, String sessionId) {
        if(!verificarSesionActiva(usuarioId)) {
            return false;
        }
        String sessionIdActivo = sesionesActivas.get(usuarioId);
        return sessionIdActivo != null && sessionIdActivo.equals(sessionId);
    }
}