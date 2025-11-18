package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Contacto;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

@Service
public class UsuarioContactoService {
    
    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DireccionRepository direccionRepository; 

    @Autowired
    private ComunaRepository comunaRepository;

    public Contacto save(Contacto contacto){
        if (contacto.getNombre() == null || contacto.getNombre().trim().isEmpty()|| 
            contacto.getApellido() == null || contacto.getApellido().trim().isEmpty()){
            throw new RuntimeException("Debe indicar su nombre y apellido");
        }
        if(contacto.getTelefono() == null){
            throw new RuntimeException("Debe ingresar un telefono");
        }

        return contactoRepository.save(contacto);
    }



}
