package SNAKE_PC.demo.service.usuario;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.model.usuario.Usuario;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

@Service
@Transactional
public class UsuarioContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DireccionService direccionService;

    public Contacto crearContacto(Contacto contacto,
        Direccion direccion, Long idComuna, Long idUsuario) throws IOException {
        Usuario existente = usuarioRepository.findById(idUsuario)
            .orElseThrow(()-> new RuntimeException("No se encontro un usuario"));
        validarDatosContacto(contacto);

        Direccion direccionNuevo = direccionService.crearDireccion(direccion, idComuna);
        contacto.setUsuario(existente);
        contacto.setDireccion(direccionNuevo);
        return contactoRepository.save(contacto);
    }

    public Contacto ActualizarContacto(Contacto contactoActualizado,
        Direccion direccionActualizada, String nuevoCorreo, Long idComuna, Long idUsuarioLogueado) {
            
        Contacto contactoexistente = contactoRepository.findByUsuarioId(idUsuarioLogueado)
            .orElseThrow(()-> new RuntimeException("Sin datos de contacto"));

        validarDatosContactoParaActualizacion(contactoActualizado, contactoexistente.getId());
        contactoexistente.setNombre(contactoActualizado.getNombre().trim());
        contactoexistente.setApellido(contactoActualizado.getApellido().trim());
        contactoexistente.setTelefono(contactoActualizado.getTelefono());

        Usuario usuarioExistente = contactoexistente.getUsuario();
        String correoActual = nuevoCorreo != null ? nuevoCorreo.trim() : usuarioExistente.getCorreo();

        if (!usuarioExistente.getCorreo().equalsIgnoreCase(correoActual)) {
            usuarioService.validarCorreoParaActualizacion(nuevoCorreo, usuarioExistente.getId());
            usuarioService.actualizarCorreo(usuarioExistente.getId(), nuevoCorreo);
        }
        Direccion nuevaDireccion = direccionService.crearDireccion(direccionActualizada, idComuna);
        contactoexistente.setDireccion(nuevaDireccion);
        return contactoRepository.save(contactoexistente);
    }

    public Contacto obtenerDatosContacto(Long contactoId) {
        return contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado"));
    }

    // --------------------------------- Validaciones  ---------------------------------------------------------------
    public void validarDatosContacto(Contacto contacto) {
        if (contacto.getNombre() == null || contacto.getNombre().trim().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (contacto.getApellido() == null || contacto.getApellido().trim().isBlank()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        if (contactoRepository.existsByTelefono(contacto.getTelefono())) {
            throw new RuntimeException("El teléfono ya existe");
        }
        if (contacto.getTelefono() == null || contacto.getTelefono().trim().isEmpty()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }
        if (!contacto.getTelefono().matches("\\d+")) {
            throw new RuntimeException("El teléfono solo puede contener números");
        }
        if (contacto.getTelefono().length() != 9) {
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

    // --------------------------------- Para listar siendo admin ----------------------------------------------
    public List<Contacto> findAll() {
        return contactoRepository.findAll();
    }

}
