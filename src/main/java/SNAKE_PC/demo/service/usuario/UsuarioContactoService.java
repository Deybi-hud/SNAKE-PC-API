package SNAKE_PC.demo.service.usuario;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

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
public class UsuarioContactoService {


    @Autowired
    private UsuarioRepository usuarioRepository;
    
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


    public Contacto RegistrarCliente(Contacto contacto, MultipartFile imagen, String nombreUsuario, String correo, String contrasena, 
                                    String confrimarContrasena, String calle, String numero, Long comunaId, Long idRol) throws IOException{

        // ✅ VALIDACIONES PREVIAS (antes de crear usuario)
        
        // Validar Contacto
        if (contacto.getNombre() == null || contacto.getNombre().trim().isEmpty()){ 
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (contacto.getApellido() == null || contacto.getApellido().trim().isEmpty()){
            throw new RuntimeException("El apellido es obligatorio");
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
        if(contactoRepository.existsByTelefono(contacto.getTelefono())){
            throw new RuntimeException("El teléfono ya está registrado");
        }

        // Validar Usuario
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()){
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        
        // Validar Dirección
        if (calle == null || calle.trim().isEmpty()){
            throw new RuntimeException("La calle es obligatoria");
        }
        if (numero == null || numero.trim().isEmpty()){
            throw new RuntimeException("El número es obligatorio");
        }
        
        // Validar Comuna y Rol antes de crear entidades
        Comuna comuna = comunaRepository.findById(comunaId)
            .orElseThrow(()-> new RuntimeException("Comuna no válida"));
        
        RolUsuario rolExistente = rolRepository.findById(idRol)
            .orElseThrow(()-> new RuntimeException("Rol no encontrado"));

        // ✅ CREAR USUARIO
        Usuario usuario = new Usuario();
        if (imagen != null && !imagen.isEmpty()) {
            byte[] imagenBytes = imagen.getBytes();
            String base64 = Base64.getEncoder().encodeToString(imagenBytes);
            usuario.setImagenUsuario(base64);
        }
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setContrasena(contrasena);
        Usuario nuevoUsuario = usuarioService.save(usuario, confrimarContrasena);

        // ✅ CREAR DIRECCIÓN
        Direccion direccion = new Direccion();
        direccion.setCalle(calle);
        direccion.setNumero(numero);
        direccion.setComuna(comuna);
        direccionService.validarDireccion(direccion);
        Direccion direccionGuardada = direccionRepository.save(direccion);

        // ✅ CREAR CONTACTO
        contacto.setUsuario(nuevoUsuario);
        contacto.setDireccion(direccionGuardada);
        contacto.setRolUsuario(rolExistente);
        
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


//--------------------------------- Para listar siendo admin ---------------------------------------------------
    public List<Contacto> findAll(){
        return contactoRepository.findAll();
    }
    
}
