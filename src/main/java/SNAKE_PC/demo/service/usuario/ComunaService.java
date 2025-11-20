package SNAKE_PC.demo.service.usuario;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;
    
    public List<Comuna> findComunasByRegion(Long regionId){
        return comunaRepository.findAll();
    }

    public Comuna save(String nombreComuna, Region region){
            if(nombreComuna == null || nombreComuna.trim().isEmpty()){
                throw new RuntimeException("Debe ingresar el nombre de la comuna");
            }
            if(region == null || region.getId() == null){
                throw new RuntimeException("Debe seleccionar una región");
            }

            boolean existeComuna = comunaRepository.existsByNombreComunaAndRegionId(
                nombreComuna.trim(), region.getId());
            if(existeComuna){
                throw new RuntimeException("La comuna" + nombreComuna + "ya existe en está región");
            }

            Comuna nuevaComuna = new Comuna();
            nuevaComuna.setNombreComuna(nombreComuna.trim());
            nuevaComuna.setRegion(region); 


        return comunaRepository.save(nuevaComuna);
    }

}
