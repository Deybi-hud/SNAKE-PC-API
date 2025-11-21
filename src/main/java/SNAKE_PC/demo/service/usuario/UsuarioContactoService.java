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

    public Contacto RegistrarCliente(String nombre, String apellido, String telefono, String imagenUsuario, String nombreUsuario, String correo, String contrasena, 
                                    String confirmarContrasena, String calle, String numero, Long comunaId, Long idRol) throws IOException{
    
        validarDatosContacto(nombre, apellido, telefono);
        RolUsuario rolExistente = rolRepository.findById(idRol)
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));

        Usuario nuevoUsuario = usuarioService.crearUsuario(nombreUsuario,imagenUsuario,correo, contrasena,confirmarContrasena);
        Direccion nuevadireccion = direccionService.crearDireccion(calle,numero,comunaId);

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setNombre(nombre);
        nuevoContacto.setApellido(apellido);
        nuevoContacto.setTelefono(telefono);
        nuevoContacto.setUsuario(nuevoUsuario);
        nuevoContacto.setDireccion(nuevadireccion);
        nuevoContacto.setRolUsuario(rolExistente);
        
        return contactoRepository.save(nuevoContacto);

    }

    public Contacto ActualizarContacto(Long contactoId, String nombre, String apellido, String telefono, String calle, String numero, Long comunaId){
        Contacto contactoexistente = contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Contacto no encontrado"));

        validarDatosContacto(nombre, apellido, telefono);
        
        direccionService.validarDireccion(calle, numero);
        Direccion nuevaDireccion = direccionService.crearDireccion(calle, numero, comunaId);

        contactoexistente.setDireccion(nuevaDireccion);
        contactoexistente.setNombre(nombre);
        contactoexistente.setApellido(apellido);
        contactoexistente.setTelefono(telefono);
        
        return contactoRepository.save(contactoexistente);
    }

    public Contacto obtenerDatosContacto(Long contactoId){
        return contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }



//--------------------------------- Validaciones ---------------------------------------------------------------
public void validarDatosContacto(String nombre, String apellido, String telefono){
        if (nombre == null || nombre.trim().isEmpty()){ 
                throw new RuntimeException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.trim().isEmpty()){
            throw new RuntimeException("El apellido es obligatorio");
        }
        if(telefono == null || telefono.trim().isEmpty()){
            throw new RuntimeException("El teléfono es obligatorio");
        }
        if(!telefono.matches("\\d+")){
            throw new RuntimeException("El teléfono solo puede contener números");
        }
        if(telefono.length() != 9){
            throw new RuntimeException("El teléfono debe tener exactamente 9 dígitos");
        }
        if(contactoRepository.existsByTelefono(telefono)){
            throw new RuntimeException("El teléfono ya está registrado");
        }
    }
//--------------------------------- Para listar siendo admin ---------------------------------------------------
    public List<Contacto> findAll(){
        return contactoRepository.findAll();
    }
    
}
