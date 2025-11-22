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

    public Contacto RegistrarCliente(Contacto contacto, Usuario usuario,String confirmarContrasena, Direccion direccion, Long idComuna) throws IOException{
    
        validarDatosContacto(contacto);
        RolUsuario rolCliente= rolRepository.findByNombreRol("CLIENTE")
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));
        Usuario usuarioNuevo = usuarioService.crearUsuario(usuario, confirmarContrasena);
        usuarioNuevo.setRolUsuario(rolCliente);

        Direccion direccionNuevo = direccionService.crearDireccion(direccion,idComuna);
        contacto.setUsuario(usuarioNuevo);
        contacto.setDireccion(direccionNuevo);
        usuarioNuevo.setContacto(contacto);
        
        return contactoRepository.save(contacto);
    }

    public Contacto ActualizarContacto(Contacto contactoActualizado, Usuario usuarioConCorreoNuevo, Direccion direccionActualizada, Long idComuna, String correoUsuarioLogueado){
        Contacto contactoexistente = contactoRepository.findByIdAndUsuarioCorreo(contactoActualizado.getId(), correoUsuarioLogueado)
            .orElseThrow(()-> new RuntimeException("Contacto no encontrado o no tienes permisos"));

        validarDatosContactoParaActualizacion(contactoActualizado, contactoexistente.getId());

        contactoexistente.setNombre(contactoActualizado.getNombre().trim());
        contactoexistente.setApellido(contactoActualizado.getApellido().trim());
        contactoexistente.setTelefono(contactoActualizado.getTelefono());

        Usuario usuarioReal = contactoexistente.getUsuario();
        String correoActual = usuarioReal.getCorreo().trim();
        String correoNuevo = usuarioConCorreoNuevo.getCorreo().trim();
        if(!correoActual.equalsIgnoreCase(correoNuevo)){
            usuarioService.validarCorreoParaActualizacion(correoNuevo, usuarioReal.getId());
            usuarioService.actualizarCorreo(usuarioReal.getId(), correoNuevo);
        }
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

    private void validarDatosContactoParaActualizacion(Contacto contactoActualizado, Long contactoIdActual) {
        if (contactoActualizado.getNombre() == null || contactoActualizado.getNombre().trim().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (contactoActualizado.getApellido() == null || contactoActualizado.getApellido().trim().isBlank()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        if (contactoActualizado.getTelefono() == null || contactoActualizado.getTelefono().trim().isEmpty()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }
        if (!contactoActualizado.getTelefono().matches("\\d+")) {
            throw new RuntimeException("El teléfono solo puede contener números");
        }
        if (contactoActualizado.getTelefono().length() != 9) {
            throw new RuntimeException("El teléfono debe tener exactamente 9 dígitos");
        }
        if (contactoRepository.existsByTelefonoAndIdNot(contactoActualizado.getTelefono(), contactoIdActual)) {
            throw new RuntimeException("El teléfono ya está registrado por otro usuario");
        }
    }


//--------------------------------- Para listar siendo admin ---------------------------------------------------
    public List<Contacto> findAll(){
        return contactoRepository.findAll();
    }
    
}
