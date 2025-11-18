package SNAKE_PC.demo.service.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.repository.usuario.ComunaRepository;
import SNAKE_PC.demo.repository.usuario.ContactoRepository;
import SNAKE_PC.demo.repository.usuario.DireccionRepository;
import SNAKE_PC.demo.repository.usuario.RegionRepository;
import SNAKE_PC.demo.repository.usuario.RolRepository;
import SNAKE_PC.demo.repository.usuario.UsuarioRepository;

@Service
public class RegistroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContactoRepository contactoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ComunaRepository    comunaRepository;

    @Autowired
    private RegionRepository    regionRepository;

    @Autowired
    private RolRepository   rolRepository;
    
}
