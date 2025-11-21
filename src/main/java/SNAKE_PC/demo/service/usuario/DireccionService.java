package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired 
    private ComunaRepository comunaRepository;


    public void validarDireccion(String calle, String numero){
        if(numero == null || numero.trim().isEmpty()){
            throw new RuntimeException("Debe ingresar al menos 1 dígitos para la direccion");
        }
        if(calle == null || calle.trim().isEmpty()){
            throw new RuntimeException("Debe ingresar el nombre de la calle para el envio");
        }
    }

    public Direccion crearDireccion(String calle, String numero, Long comunaId){
        validarDireccion(calle,numero);
        
        Comuna comuna = comunaRepository.findById(comunaId)
            .orElseThrow(()-> new RuntimeException("Comuna no encontrada"));
            
        Direccion nuevaDireccion = new Direccion();
        nuevaDireccion.setCalle(calle);
        nuevaDireccion.setNumero(numero);
        nuevaDireccion.setComuna(comuna);

        return direccionRepository.save(nuevaDireccion);
    }


}
