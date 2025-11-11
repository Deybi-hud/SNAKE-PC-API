package SNAKE_PC.demo.service.usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.usuario.RolUsuario;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class RolService {
    
    @Autowired 
    private RolRepository rolRepository;


    public RolUsuario crearRolUsuario(RolUsuario rolUsuario) {
        if(rolRepository.existsById(rolUsuario.getId())){
            throw new RuntimeException("El rol con id " + rolUsuario.getId() + " ya existe.");
        }
        if(rolRepository.findByNombreRol(rolUsuario.getNombreRol()).isPresent()){
            throw new RuntimeException("El rol con nombre " + rolUsuario.getNombreRol() + " ya existe.");
        }
        return rolRepository.save(rolUsuario);
    }

    public Optional<RolUsuario> obtenerRolPorNombre(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol);
    }
}
