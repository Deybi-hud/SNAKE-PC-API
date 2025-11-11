package SNAKE_PC.demo.service.usuario;

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
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service 
@Transactional
public class ContactoService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private RolRepository rolUsuarioRepository;

    @Autowired  
    private ComunaRepository comunaRepository;


    public  Contacto guardarContactoSinRegistro(Contacto contacto,Usuario usuario, RolUsuario rol,
            Direccion direccion, Comuna comuna, Long UsuarioId) {

        if(contactoRepository.existsByTelefono(contacto.getTelefono())){
            throw new RuntimeException("El contacto con teléfono " + contacto.getTelefono() + " ya existe.");
        }
        if(contacto.getTelefono() == null){
            throw new RuntimeException("El contacto debe tener un teléfono asociado.");
        }
        if(contacto.getNombre() == null || contacto.getApellido() == null){
            throw new RuntimeException("El contacto debe agregar un nombre y un apellido.");
        }
        if(contacto.getDireccion() == null){
            throw new RuntimeException("El contacto debe tener una dirección asociada.");
        }
        if(direccion.getCalle() == null|| direccion.getNumero() == null){
            throw new RuntimeException("La dirección debe tener calle y número.");
        }
        if(comuna == null){
            throw new RuntimeException("La comuna no puede ser nula.");
        }
        if(comuna.getId() == null){
            comuna = comunaRepository.save(comuna);
        }

        direccion.setComuna(comuna);
        Direccion nuevaDireccion = direccionRepository.save(direccion);


        RolUsuario rolInvitado = rolUsuarioRepository.findByNombreRol("Invitado")
                .orElseGet(()->{
                    RolUsuario nuevoRol = new RolUsuario();
                    nuevoRol.setNombreRol("Invitado");
                    return rolUsuarioRepository.save(nuevoRol);
                });

        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setNombre(contacto.getNombre());
        nuevoContacto.setApellido(contacto.getApellido());
        nuevoContacto.setTelefono(contacto.getTelefono());
        nuevoContacto.setDireccion(nuevaDireccion);
        nuevoContacto.setRolUsuario(rolInvitado);

        return contactoRepository.save(nuevoContacto);
    }


    public  Contacto registrarCliente(Contacto contacto,Usuario usuario, RolUsuario rolUsuario,
            Direccion direccion, Comuna comuna) {

            if(contactoRepository.existsByTelefono(contacto.getTelefono())){
                throw new RuntimeException("El contacto con teléfono " + contacto.getTelefono() + " ya existe.");
            }
            if(contacto.getTelefono() == null){
                throw new RuntimeException("Debe tener un teléfono asociado.");
            }
            if(contacto.getNombre() == null || contacto.getApellido() == null){
                throw new RuntimeException("Debe agregar un nombre y un apellido.");
            }
            if(contacto.getDireccion() == null){
                throw new RuntimeException("Debe tener una dirección asociada.");
            }
            if(direccion.getCalle() == null|| direccion.getNumero() == null){
                throw new RuntimeException("La dirección debe tener calle y número.");
            }
            if(comuna == null){
                throw new RuntimeException("La comuna no puede ser nula.");
            }
            if(comuna.getId() == null){
                comuna = comunaRepository.save(comuna);
            }

            direccion.setComuna(comuna);
            Direccion nuevaDireccion = direccionRepository.save(direccion);

            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);

            RolUsuario nuevoCliente = rolUsuarioRepository.findByNombreRol("Cliente")
                    .orElseGet(()->{
                        RolUsuario nuevoRol = new RolUsuario();
                        nuevoRol.setNombreRol("Cliente");
                        return rolUsuarioRepository.save(nuevoRol);
                    });
            
            Contacto nuevoContacto = new Contacto();
            nuevoContacto.setNombre(contacto.getNombre());
            nuevoContacto.setApellido(contacto.getApellido());
            nuevoContacto.setTelefono(contacto.getTelefono());
            nuevoContacto.setUsuario(nuevoUsuario);
            nuevoContacto.setDireccion(nuevaDireccion); 
            nuevoContacto.setRolUsuario(nuevoCliente);


        return contactoRepository.save(nuevoContacto);
    }

    public Contacto actualizarDireccion(Long contactoId, Direccion nuevaDireccion, Comuna comuna) {
        Contacto contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con ID: " + contactoId));
        if(nuevaDireccion.getCalle() == null || nuevaDireccion.getNumero() == null){
            throw new RuntimeException("La dirección debe tener calle y número.");
        }
        if(comuna == null){
            throw new RuntimeException("La comuna no puede ser nula.");
        }
        if(comuna.getId() == null){
            comuna = comunaRepository.save(comuna);
        }
        nuevaDireccion.setComuna(comuna);
        Direccion direccionGuardada = direccionRepository.save(nuevaDireccion);
        Direccion direccionAnterior =  contacto.getDireccion();
        contacto.setDireccion(direccionGuardada);
        Contacto contactoActualizado = contactoRepository.save(contacto);

        if(direccionAnterior != null){
            direccionRepository.delete(direccionAnterior);
        }
        return contactoActualizado;
    }
}
