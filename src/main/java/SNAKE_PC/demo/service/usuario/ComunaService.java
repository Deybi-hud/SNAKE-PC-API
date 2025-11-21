package SNAKE_PC.demo.service.usuario;

import SNAKE_PC.demo.model.usuario.Comuna;
import SNAKE_PC.demo.model.usuario.Region;
import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;
    
    @Autowired
    private RegionService regionService;

    public List<Comuna> findComunasByRegion(Long regionId) {
        return comunaRepository.findByRegionIdOrderByNombreComunaAsc(regionId);
    }
    
    public List<Comuna> findAll() {
        return comunaRepository.findAllWithRegion();
    }
    
    public Comuna findById(Long comunaId) {
        return comunaRepository.findByIdWithRegion(comunaId)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));
    }
    
    public Comuna findByNombre(String nombreComuna) {
        return comunaRepository.findByNombreComuna(nombreComuna)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada: " + nombreComuna));
    }
    
    public Comuna save(Comuna comuna, Long regionId) {
        if (comuna.getNombreComuna() == null || comuna.getNombreComuna().isBlank()) {
            throw new RuntimeException("Debe ingresar el nombre de la comuna");
        }
        comuna.setNombreComuna(comuna.getNombreComuna().trim());
        Region region = regionService.findById(regionId);
        boolean existeComuna = comunaRepository.existsByNombreComunaAndRegionId(
            comuna.getNombreComuna(), regionId);
        if (existeComuna) {
            throw new RuntimeException("La comuna '" + comuna.getNombreComuna() + "' ya existe en esta región");
        }
        
       comuna.setRegion(region);

        return comunaRepository.save(comuna);
    }
    
    public boolean existeComuna(String nombreComuna, Long regionId) {
        return comunaRepository.existsByNombreComunaAndRegionId(nombreComuna, regionId);
    }
}