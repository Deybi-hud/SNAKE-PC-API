package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

@Service
public class RolUsuarioService {
    

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Contacto actualizarRol(Long contactoId, Long nuevoRolId){
        Contacto contacto = contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        RolUsuario nuevoRol = rolRepository.findById(nuevoRolId)
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));
        
        contacto.setRolUsuario(nuevoRol);
        return contactoRepository.save(contacto);
    }
}
