package SNAKE_PC.demo.service.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class RolUsuarioService {
    

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

   public Usuario actualizarRol(Long usuarioId, String nombreRol) {

    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
    RolUsuario nuevoRol = rolRepository.findByNombreRol(nombreRol.toUpperCase())
        .orElseThrow(() -> new RuntimeException("Rol no encontrado: '" + nombreRol + "'"));

    usuario.setRolUsuario(nuevoRol);
    
    return usuarioRepository.save(usuario);
}

    public RolUsuario save(RolUsuario rolUsuario) {
        if (rolUsuario.getNombreRol() == null || rolUsuario.getNombreRol().trim().isEmpty()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }
        if (rolRepository.existsByNombreRol(rolUsuario.getNombreRol())) {
            throw new RuntimeException("El rol '" + rolUsuario.getNombreRol() + "' ya existe");
        }
        
        return rolRepository.save(rolUsuario);
    }

    public List<RolUsuario> findAll() {
        return rolRepository.findAll();
    }
    
    public RolUsuario findById(Long id) {
        return rolRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }
    
    public RolUsuario findByNombre(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));
    }
    
    public boolean existeRol(String nombreRol) {
        return rolRepository.existsByNombreRol(nombreRol);
    }
}
