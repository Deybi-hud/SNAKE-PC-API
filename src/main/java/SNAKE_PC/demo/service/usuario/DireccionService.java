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


    public void validarDireccion(Direccion direccion){
        if(direccion.getCalle() == null || direccion.getCalle().trim().isBlank()){
            throw new RuntimeException("Debe ingresar el nombre de la calle para el envio");
        }
        if(direccion.getNumero() == null ||direccion.getNumero().trim().isBlank()){
            throw new RuntimeException("Debe ingresar al menos 1 dígitos para la direccion");
        }
    }

    public Direccion crearDireccion(Direccion direccion, Long comunaId){
        validarDireccion(direccion);

        Comuna comuna = comunaRepository.findById(comunaId)
            .orElseThrow(()-> new RuntimeException("Comuna no encontrada"));
        
        direccion.setCalle(direccion.getCalle().trim());
        direccion.setNumero(direccion.getNumero().trim());
        direccion.setComuna(comuna);

        return direccionRepository.save(direccion);
    }


}
