package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import SNAKE_PC.demo.repository.usuario.RegionRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import jakarta.transaction.Transactional;

@Service 
@Transactional
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private RolRepository rolUsuarioRepository;

    @Autowired  
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;


    public Contacto guardarContactoSinRegistro(Contacto contacto,
        Direccion direccion, String nombreRegion, String nombreComuna) {

        if(contactoRepository.existsByTelefono(contacto.getTelefono())){
            throw new RuntimeException("El contacto con teléfono " + contacto.getTelefono() + " ya existe.");
        }
        if(contacto.getTelefono() == null){
            throw new RuntimeException("El contacto debe tener un teléfono asociado.");
        }
        if(contacto.getNombre() == null || contacto.getApellido() == null){
            throw new RuntimeException("El contacto debe agregar un nombre y un apellido.");
        }
        if(direccion.getCalle() == null|| direccion.getNumero() == null){
            throw new RuntimeException("La dirección debe tener calle y número.");
        }
        if(nombreComuna== null || nombreRegion == null){
            throw new RuntimeException("Comuna y región requeridos");
        }
       
        Region region = regionRepository.findByNombreRegion(nombreRegion)
            .orElseGet(()-> {
                Region nuevaRegion = new Region();
                nuevaRegion.setNombreRegion(nombreRegion);
                return regionRepository.save(nuevaRegion);
            });
        
        Comuna comuna = comunaRepository.findByNombreComunaAndRegion(nombreComuna, region)
            .orElseGet(()->{
                Comuna nuevaComuna = new Comuna();
                nuevaComuna.setNombreComuna(nombreComuna);
                nuevaComuna.setRegion(region);
                return comunaRepository.save(nuevaComuna);
            });

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
