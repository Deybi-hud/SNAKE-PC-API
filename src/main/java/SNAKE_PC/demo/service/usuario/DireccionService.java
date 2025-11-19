package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.Direccion;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import SNAKE_PC.demo.repository.usuario.RegionRepository;

@Service
public class DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired 
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    public void validarDireccion(Direccion direccion){
        if(direccion.getNumero() == null || direccion.getNumero().trim().isEmpty()){
            throw new RuntimeException("Debe ingresar al menos 1 dígitos para la direccion");
        }
        if(direccion.getCalle() == null || direccion.getCalle().trim().isEmpty()){
            throw new RuntimeException("Debe ingresar el nombre de la calle para el envio");
        }
    }

}
