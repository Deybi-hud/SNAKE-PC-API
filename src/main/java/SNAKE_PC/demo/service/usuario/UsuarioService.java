package SNAKE_PC.demo.service.usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import SNAKE_PC.demo.repository.usuario.RegionRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private RolRepository rolUsuaRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ContactoRepository contactoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//--------------------------- Métodos para registrar  ----------------------------------------
    public Usuario registrarUsuario(Usuario usuario, String confirmarContrasena) {
        if(usuarioRepository.existsByUsername(usuario.getNombreUsuario())){
            throw new RuntimeException("El usuario con ID " + usuario.getId() + " ya existe.");
        }
        if(usuarioRepository.existsByEmail(usuario.getCorreo())){
            throw new RuntimeException("El correo " + usuario.getCorreo() + " ya está en uso.");
        }
        if(!usuario.getContrasena().equals(confirmarContrasena)){
            throw new RuntimeException("Las contraseñas no coinciden.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(usuario.getNombreUsuario());
        nuevoUsuario.setCorreo(usuario.getCorreo());
        nuevoUsuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        return usuarioRepository.save(nuevoUsuario);
    }

    public Contacto registrarCliente(Contacto contacto,Usuario usuario, RolUsuario rolUsuario, String confirmarContrasena,
        Direccion direccion, String nombreComuna, String nombreRegion) {

            if(contactoRepository.existsByTelefono(contacto.getTelefono())){
                throw new RuntimeException("El contacto con teléfono " + contacto.getTelefono() + " ya existe.");
            }
            if(contacto.getTelefono() == null){
                throw new RuntimeException("Debe tener un teléfono asociado.");
            }
            if(contacto.getNombre() == null || contacto.getApellido() == null){
                throw new RuntimeException("Debe agregar un nombre y un apellido");
            }
            if(direccion.getCalle() == null|| direccion.getNumero() == null){
                throw new RuntimeException("La dirección debe tener calle y número.");
            }
            if(nombreRegion == null || nombreComuna == null){
                throw new RuntimeException("Comuna y region requeridas");
            }

            Region region = regionRepository.findByNombreRegion(nombreRegion)
                .orElseGet(()->{
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

            Usuario nuevoUsuario = registrarUsuario(usuario, confirmarContrasena);

            RolUsuario nuevoCliente = rolUsuaRepository.findByNombreRol("Cliente")
                    .orElseGet(()->{
                        RolUsuario nuevoRol = new RolUsuario();
                        nuevoRol.setNombreRol("Cliente");
                        return rolUsuaRepository.save(nuevoRol);
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


//--------------------------- Métodos para editar usuario ------------------------------------------------

    public Usuario actualizarContrasena(Long id, String contrasenaActual, String nuevaContrasena) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }   
        if(nuevaContrasena.length() < 8){
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres.");
        }
        if(!nuevaContrasena.matches(".*[A-Z].*")){
            throw new RuntimeException("La contraseña debe contener al menos una letra mayúscula.");
        }
        if(!nuevaContrasena.matches(".*[a-z].*")){
            throw new RuntimeException("La contraseña debe contener al menos una letra minúscula.");
        }
        if(!nuevaContrasena.matches(".*\\d.*")){
            throw new RuntimeException("La contraseña debe contener al menos un número.");
        }
        if(!nuevaContrasena.matches(".*[!@#$%^&*()].*")){
            throw new RuntimeException("La contraseña debe contener al menos un carácter especial.");
        }
        if(nuevaContrasena.contains(" ")){
            throw new RuntimeException("La contraseña no puede contener espacios.");
        }
        if(nuevaContrasena.equalsIgnoreCase(usuario.getNombreUsuario())){
            throw new RuntimeException("La contraseña no puede ser igual al nombre de usuario.");
        }
        if(nuevaContrasena.equalsIgnoreCase(usuario.getCorreo())){
            throw new RuntimeException("La contraseña no puede ser igual al correo.");
        }
        if (passwordEncoder.matches(nuevaContrasena, usuario.getContrasena())) {
            throw new RuntimeException("La nueva contraseña no puede ser igual a la anterior");
        }
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        return usuarioRepository.save(usuario);
    }


    public Usuario actualizarUsuario(Usuario usuarioActualizado) {
        Usuario usuario = usuarioRepository.findById(usuarioActualizado.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioActualizado.getId()));

         if (!usuario.getNombreUsuario().equals(usuarioActualizado.getNombreUsuario()) && usuarioRepository.existsByUsername(usuarioActualizado.getNombreUsuario())) {
        throw new RuntimeException("El nombre de usuario ya está en uso");
    }
        if (!usuario.getCorreo().equals(usuarioActualizado.getCorreo()) && usuarioRepository.existsByEmail(usuarioActualizado.getCorreo())) {
        throw new RuntimeException("El correo ya está en uso");
        }

        usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuario.setCorreo(usuarioActualizado.getCorreo());
        return usuarioRepository.save(usuario);
    }
    

     
    
   
    //--------------------------- Metodos de búsquedad -----------------------------------------------------

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario ) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario obtenerDatosDeUsuario(Long usuarioId) {
        return usuarioRepository.findByIdWithRelations(usuarioId)
            .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
    }


//------------------------------------- Método para eliminar información de contacto ---------------------------

    public Map<String, Object> eliminarInformacionContacto(Long contactoId){
        Contacto contacto = contactoRepository.findById(contactoId)
            .orElseThrow(()-> new RuntimeException("Contacto no encontrado"));

            Usuario usuario = contacto.getUsuario();
            Direccion direccion = contacto.getDireccion();

        Map<String, Object> direccionInfo = new HashMap<>();
        if(direccion != null){
            direccionInfo.put("calle", direccion.getCalle());
            direccionInfo.put("numero",direccion.getNumero());
            if(direccion.getComuna() != null){
                direccionInfo.put("comunaObjeto", direccion.getComuna());
                direccionInfo.put("comunaNombre", direccion.getComuna().getNombreComuna());
                if(direccion.getComuna().getRegion() != null){
                    direccionInfo.put("regionObjeto", direccion.getComuna().getRegion());
                    direccionInfo.put("nombreRegion",direccion.getComuna().getRegion().getNombreRegion());
                }
            }
        }

        Map<String, Object> eliminar = new HashMap<>();

        if(direccion != null){
            direccion.setCalle(null);
            direccion.setNumero(null);
            direccion.setComuna(null);
            direccionRepository.save(direccion);
            eliminar.put("direccion","Direccion eliminada correctamente");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success",true);     
        response.put("mensaje","Informacion de contacto eliminada correctamente");   
        response.put("contactoId",contactoId);   
        response.put("usuarioId",usuario != null ? usuario.getId() : null);   
        response.put("informacionEliminada",direccionInfo);   
        response.put("eliminar",eliminar);   

        return response;
        
    }



}
