package SNAKE_PC.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // ✅ Busca directamente en la entidad Usuario
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        // Obtener authorities desde los contactos del usuario
        return org.springframework.security.core.userdetails.User.builder()
            .username(usuario.getCorreo())
            .password(usuario.getContrasena())
            .disabled(!usuario.isActivo())
            .authorities(obtenerAuthorities(usuario))
            .build();
    }

    private String[] obtenerAuthorities(Usuario usuario) {
        if (usuario.getContactos() != null && !usuario.getContactos().isEmpty()) {
            String rol = usuario.getContactos().get(0).getRolUsuario().getNombreRol();
            return new String[]{"ROLE_" + rol};
        }
        return new String[]{"ROLE_USUARIO"};
    }
}