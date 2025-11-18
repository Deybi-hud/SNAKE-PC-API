package SNAKE_PC.demo.service.usuario;

import SNAKE_PC.demo.model.usuario.Comuna;
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
}
