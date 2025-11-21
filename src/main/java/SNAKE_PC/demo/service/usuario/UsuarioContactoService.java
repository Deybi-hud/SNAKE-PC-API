package SNAKE_PC.demo.service.usuario;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;

@Service
@Transactional
public class UsuarioContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DireccionService direccionService;

    @Autowired 
    private RolRepository rolRepository;

    public Contacto RegistrarCliente(Contacto contacto, Usuario usuario,String confirmarContrasena, Long idRol,Direccion direccion, Long idComuna) throws IOException{
    
        validarDatosContacto(contacto);
        RolUsuario rolExistente = rolRepository.findById(idRol)
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));

        Usuario usuarioNuevo = usuarioService.crearUsuario(usuario, confirmarContrasena);
        Direccion direccionNuevo = direccionService.crearDireccion(direccion,idComuna);
        contacto.setUsuario(usuarioNuevo);
        contacto.setDireccion(direccionNuevo);
        usuarioNuevo.setContacto(contacto);
        
        return contactoRepository.save(contacto);

    }

    public Contacto ActualizarContacto(Contacto contactoActualizado, Usuario usuario, Direccion direccionActualizada, Long idComuna){
        Contacto contactoexistente = contactoRepository.findById(contactoActualizado.getId())
            .orElseThrow(()-> new RuntimeException("Contacto no encontrado"));

        validarDatosContacto(contactoActualizado);
        usuarioService.actualizarCorreo(usuario.getId(),usuario.getCorreo());
        direccionService.validarDireccion(direccionActualizada);
        Direccion nuevaDireccion = direccionService.crearDireccion(direccionActualizada, idComuna);
        contactoexistente.setDireccion(nuevaDireccion);

        return contactoRepository.save(contactoexistente);
    }

    public Contacto obtenerDatosContacto(Long contactoId){
        return contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Contacto no encontrado"));
    }



//--------------------------------- Validaciones ---------------------------------------------------------------
public void validarDatosContacto(Contacto contacto){
        if (contacto.getNombre() == null || contacto.getNombre().trim().isBlank()){ 
                throw new RuntimeException("El nombre es obligatorio");
        }
        if (contacto.getApellido() == null || contacto.getApellido().trim().isBlank()){
            throw new RuntimeException("El apellido es obligatorio");
        }
        if(contactoRepository.existsByTelefono(contacto.getTelefono())){
            throw new RuntimeException("El teléfono ya existe");
        }
        if(contacto.getTelefono() == null || contacto.getTelefono().trim().isEmpty()){
            throw new RuntimeException("El teléfono es obligatorio");
        }
        if(!contacto.getTelefono().matches("\\d+")){
            throw new RuntimeException("El teléfono solo puede contener números");
        }
        if(contacto.getTelefono().length() != 9){
            throw new RuntimeException("El teléfono debe tener exactamente 9 dígitos");
        }
    }
//--------------------------------- Para listar siendo admin ---------------------------------------------------
    public List<Contacto> findAll(){
        return contactoRepository.findAll();
    }
    
}
