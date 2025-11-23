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

    public Especificacion guardarEspecificacion(Especificacion especificacion) {
        validarEspecificacion(especificacion);
        return especificacionRepository.save(especificacion);
        
    }

    public void validarEspecificacion(Especificacion especificacion){

        if (especificacion.getFrecuencia() == null || especificacion.getFrecuencia().trim().isBlank()) {
            throw new RuntimeException("La frecuencia no puede estar vacía.");
        }
        if (especificacion.getCapacidadAlmacenamiento() == null || especificacion.getCapacidadAlmacenamiento().trim().isBlank()) {
            throw new RuntimeException("La capacidad de almacenamiento es obligatoria.");
        }
        if (especificacion.getConsumo() ==  null || especificacion.getConsumo().trim().isBlank()) {
            throw new RuntimeException("El consumo es obligatorio.");
        }

        String frec = especificacion.getFrecuencia().trim().toUpperCase();
        String cap  = especificacion.getCapacidadAlmacenamiento().trim().toUpperCase();
        String con  = especificacion.getConsumo().trim().toUpperCase();

        boolean existente = especificacionRepository
            .existsByFrecuenciaAndCapacidadAlmacenamientoAndConsumo(frec, cap, con);
        if(existente){
            throw new RuntimeException("Ya existe especificación");
        }
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
