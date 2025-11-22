package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.repository.pedido.MetodoEnvioRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetodoEnvioService {
    
    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;


    @PostConstruct
    public void inicalizarMetodos(){
        crearEstadoSiNoExiste("ChileExpress");
        crearEstadoSiNoExiste("Starken");
        crearEstadoSiNoExiste("BlueExpress");
        crearEstadoSiNoExiste("SonicTheHedgehog");
    }



}
