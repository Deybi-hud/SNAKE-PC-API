package SNAKE_PC.demo.service.usuario;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;

@Service
@Transactional
public class UsuarioContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private DireccionRepository direccionRepository; 

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DireccionService direccionService;

    @Autowired 
    private RolRepository rolRepository;

    public Contacto RegistrarCliente(String nombre, String apellido, String telefono, String imagenUsuario, String nombreUsuario, String correo, String contrasena, 
                                    String confirmarContrasena, String calle, String numero, Long comunaId, Long idRol) throws IOException{
        try{      
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

       }catch(Exception e){
                throw new RuntimeException(e);
        }
    }

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

    public Contacto ActualizarContacto(Long contactoId, Contacto datosActualizados, String calle, String numero, Long comunaId){
            Contacto contactoexistente = contactoRepository.findById(contactoId)
                .orElseThrow(()-> new RuntimeException("Contacto no encontrado"));
            try{

                if(datosActualizados.getNombre() != null){
                    contactoexistente.setNombre(datosActualizados.getNombre());
                }    
                if(datosActualizados.getApellido() != null){
                    contactoexistente.setApellido(datosActualizados.getApellido());
                }
                if(datosActualizados.getTelefono() != null){
                    if(!datosActualizados.getTelefono().equals(contactoexistente.getTelefono()) &&
                        contactoRepository.existsByTelefono(datosActualizados.getTelefono())){
                        throw new RuntimeException("El teléfono ya está en uso");
                    }
                    contactoexistente.setTelefono(datosActualizados.getTelefono());
                }

                if(comunaId != null){
                    Comuna comuna = comunaRepository.findById(comunaId)
                        .orElseThrow(()-> new RuntimeException("Comuna no encontrada"));

                    Direccion direccion = contactoexistente.getDireccion();
                    if(direccion == null){
                        direccion = new Direccion();
                    }
                    if(calle != null){
                        direccion.setCalle(calle);
                    }
                    if(numero != null){
                        direccion.setNumero(numero);
                    }
                    direccion.setComuna(comuna);

                    direccionService.validarDireccion(direccion);
                    Direccion direccionGuardada = direccionRepository.save(direccion);
                    contactoexistente.setDireccion(direccionGuardada);
                }
            
                return contactoRepository.save(contactoexistente);

            }catch(Exception e){
                throw new RuntimeException(e);
            }

    }

    public Contacto obtenerDatosContacto(Long contactoId){
        return contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }


//--------------------------------- Para listar siendo admin ---------------------------------------------------
    public List<Contacto> findAll(){
        return contactoRepository.findAll();
    }
    
}
