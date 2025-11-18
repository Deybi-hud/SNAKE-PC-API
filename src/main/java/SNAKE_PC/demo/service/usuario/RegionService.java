package SNAKE_PC.demo.service.usuario;

import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.repository.usuario.RegionRepository;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionService {

    

    @Autowired
    private RegionRepository regionRepository;


    
    public List<Region> findAllRegiones(){
        return regionRepository.findAll();
    }

    public Region save(Region region){
        if(region.getNombreRegion() == null || region.getNombreRegion().trim().isEmpty()){
            throw new RuntimeException("El nombre de la región es obligatorio");
        }
        if(regionRepository.existsByNombreRegion(region.getNombreRegion())){
            throw new RuntimeException("La región ya existe");
        }
        return regionRepository.save(region);
    }
}
