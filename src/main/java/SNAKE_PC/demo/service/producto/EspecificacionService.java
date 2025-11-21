package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Especificacion;
import SNAKE_PC.demo.repository.producto.EspecificacionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EspecificacionService {

    @Autowired
    private EspecificacionRepository especificacionRepository;

    public List<Especificacion> buscarTodos() {
        return especificacionRepository.findAll();
    }

    public Especificacion buscarPorId(Long id) {
        Especificacion especificacion = especificacionRepository.findById(id).orElse(null);
        if (especificacion == null) {
            throw new IllegalArgumentException("Especificación no encontrada.");
        }
        return especificacion;
    }

    public Especificacion guardarEspecificacion(String frecuencia, String Capacidad, String consumo) {
        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            throw new RuntimeException("La frecuencia no puede estar vacía.");
        }
        if (Capacidad == null || Capacidad.trim().isEmpty()) {
            throw new RuntimeException("La capacidad de almacenamiento es obligatoria.");
        }
        if (consumo == null || consumo.trim().isEmpty()) {
            throw new RuntimeException("El consumo es obligatorio.");
        }
        Especificacion nuevaEspecificacion = new Especificacion();
        nuevaEspecificacion.setFrecuencia(frecuencia);
        nuevaEspecificacion.setCapacidadAlmacenamiento(Capacidad);
        nuevaEspecificacion.setConsumo(consumo);
        return especificacionRepository.save(nuevaEspecificacion);
    }

    public Especificacion actualizarEspecificacion(Long id, Especificacion especificacion) {
        Especificacion especificacionExistente = especificacionRepository.findById(id).orElse(null);
        if (especificacionExistente == null) {
            throw new IllegalArgumentException("Especificación no encontrada.");
        }
        if (especificacion.getFrecuencia() != null && !especificacion.getFrecuencia().isBlank()) {
            especificacionExistente.setFrecuencia(especificacion.getFrecuencia());
        }
        if (especificacion.getCapacidadAlmacenamiento() != null 
            && !especificacion.getCapacidadAlmacenamiento().isBlank()) {
            especificacionExistente.setCapacidadAlmacenamiento(especificacion.getCapacidadAlmacenamiento());
        }
        if (especificacion.getConsumo() != null && !especificacion.getConsumo().isBlank()) {
            especificacionExistente.setConsumo(especificacion.getConsumo());
        }
        return especificacionRepository.save(especificacionExistente);
    }

    public void eliminarEspecificacion(Long id) {
        Especificacion especificacion = especificacionRepository.findById(id).orElse(null);
        if (especificacion == null) {
            throw new IllegalArgumentException("Especificación no encontrada.");
        }
        especificacionRepository.deleteById(id);
    }
}
