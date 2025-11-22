package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;
import SNAKE_PC.demo.repository.pedido.MetodoEnvioRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetodoEnvioService {
    
    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;


    public MetodoEnvio crearMetodoEnvio(MetodoEnvio metodoEnvio){
        String nombre = metodoEnvio.getNombreMetodo().strip();

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del método no puede estar vacío");
        }
        boolean yaExiste = metodoEnvioRepository.existsByNombreMetodoIgnoreCase(nombre);
        if (yaExiste) {
            throw new RuntimeException("El método de envío '" + nombre + "' ya existe");
        }
        MetodoEnvio nuevo = new MetodoEnvio();
        nuevo.setNombreMetodo(nombre);
        nuevo.setActivo(true);

        return metodoEnvioRepository.save(nuevo);
    }


   public MetodoEnvio seleccionarMetodoEnvio(Long metodoEnvioId) {
        if(metodoEnvioId == null || metodoEnvioId <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un método de envío");
        }
        return metodoEnvioRepository.findById(metodoEnvioId)
                .orElseThrow(() -> new RuntimeException("El método de envío seleccionado no existe"));
    }


}
