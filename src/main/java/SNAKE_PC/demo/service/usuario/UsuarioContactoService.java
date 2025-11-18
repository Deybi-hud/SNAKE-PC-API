package SNAKE_PC.demo.service.usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UsuarioContactoService {

    private final UsuarioRepository usuarioRepository;
    
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

    UsuarioContactoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Contacto RegistrarCliente(Contacto contacto, String nombreUsuario, String correo, String contrasena, String confrimarContrasena, String calle, String numero, Long comunaId, Long idRol){

        if(!contrasena.equals(confrimarContrasena)){
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        Usuario nuevoUsuario = usuarioService.save(usuario, confrimarContrasena);

        Comuna comuna = comunaRepository.findById(comunaId)
            .orElseThrow(()-> new RuntimeException("Comuna no valida"));

        Direccion direccion = new Direccion();
        direccion.setCalle(calle);
        direccion.setNumero(numero);
        direccion.setComuna(comuna);
        direccionService.validarDireccion(direccion);
        Direccion direccionGuardada = direccionRepository.save(direccion);

        contacto.setUsuario(nuevoUsuario);
        contacto.setDireccion(direccionGuardada);
        
        RolUsuario rolExistente = rolRepository.findById(idRol)
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));
        contacto.setRolUsuario(rolExistente);

        if (contacto.getNombre() == null || contacto.getNombre().trim().isEmpty()|| 
            contacto.getApellido() == null || contacto.getApellido().trim().isEmpty()){
            throw new RuntimeException("Debe indicar su nombre y apellido");
        }
        if(contacto.getTelefono() == null){
            throw new RuntimeException("Debe ingresar un telefono");
        }
        if(contacto.getTelefono().length() != 9){
            throw new RuntimeException("Debe ingresar solo 9 digitos");
        }
        if(contactoRepository.existsByTelefono(contacto.getTelefono())){
            throw new RuntimeException("El telefono ya está registrado");
        }
        if(contacto.getDireccion().getComuna() == null){
            throw new RuntimeException("Debe seleccionar una comuna"); 
        }
        return contactoRepository.save(contacto);
    }

    public Contacto ActualizarContacto(Long contactoId, Contacto datosActualizados, String calle, String numero, Long comunaId){
            Contacto contactoexistente = contactoRepository.findById(contactoId)
                .orElseThrow(()-> new RuntimeException("Contacto no encontrado"));

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

    }

    public Contacto obtenerDatosContacto(Long contactoId){
        return contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }

    public List<Contacto> findAll(){
        return contactoRepository.findAll();
    }
    
}
