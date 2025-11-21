package SNAKE_PC.demo.service.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class RolUsuarioService {
    

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    public Contacto actualizarRol(Long contactoId, Long nuevoRolId){
        
        Contacto contacto = contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        RolUsuario nuevoRol = rolRepository.findById(nuevoRolId)
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));
        
        contacto.setRolUsuario(nuevoRol);
        return contactoRepository.save(contacto);
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
