package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.MetodoEnvio;
import SNAKE_PC.demo.repository.pedido.MetodoEnvioRepository;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MetodoEnvioService {

    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;

    public MetodoEnvio crearMetodoEnvio(MetodoEnvio metodoEnvio) {
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
        nuevo.setCostoEnvio(metodoEnvio.getCostoEnvio());
        nuevo.setActivo(true);

        return metodoEnvioRepository.save(nuevo);
    }

    public MetodoEnvio seleccionarMetodoEnvio(Integer metodoEnvioId) {
        if (metodoEnvioId == null || metodoEnvioId <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un método de envío");
        }
        return obtenerPorId(metodoEnvioId);
    }

    public List<MetodoEnvio> obtenerTodosLosMetodos() {
        return metodoEnvioRepository.findAll();
    }

    public List<MetodoEnvio> obtenerMetodosActivos() {
        return metodoEnvioRepository.findByActivoTrue();
    }

    public MetodoEnvio obtenerPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del método debe ser válido");
        }
        return metodoEnvioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de envío no encontrado"));
    }

    public MetodoEnvio obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.strip().isEmpty()) {
            throw new IllegalArgumentException("El nombre del método no puede estar vacío");
        }
        return metodoEnvioRepository.findByNombreMetodoIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Método de envío no encontrado: " + nombre));
    }

}
