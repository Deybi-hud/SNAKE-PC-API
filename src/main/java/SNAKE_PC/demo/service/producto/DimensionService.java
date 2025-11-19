package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Dimension;
import SNAKE_PC.demo.repository.producto.DimensionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class DimensionService {

    @Autowired
    private DimensionRepository dimensionRepository;

    public List<Dimension> buscarTodos() {
        return dimensionRepository.findAll();
    }

    public Dimension buscarPorId(Long id) {
        Dimension dimension = dimensionRepository.findById(id).orElse(null);
        if (dimension == null) {
            throw new IllegalArgumentException("Dimensión no encontrada.");
        }
        return dimension;
    }

    public Dimension guardarDimension(Dimension dimension) {
        if (dimension.getLargo() == null || dimension.getLargo().isBlank()) {
            throw new RuntimeException("El largo no puede estar vacío.");
        }
        if (dimension.getAncho() == null || dimension.getAncho().isBlank()) {
            throw new RuntimeException("El ancho no puede estar vacío.");
        }
        if (dimension.getAlto() == null || dimension.getAlto().isBlank()) {
            throw new RuntimeException("El alto no puede estar vacío.");
        }
        return dimensionRepository.save(dimension);
    }

    public Dimension actualizarDimension(Long id, Dimension dimension) {
        Dimension dimensionExistente = dimensionRepository.findById(id).orElse(null);
        if (dimensionExistente == null) {
            throw new IllegalArgumentException("Dimensión no encontrada.");
        }
        if (dimension.getLargo() != null && !dimension.getLargo().isBlank()) {
            dimensionExistente.setLargo(dimension.getLargo());
        }
        if (dimension.getAncho() != null && !dimension.getAncho().isBlank()) {
            dimensionExistente.setAncho(dimension.getAncho());
        }
        if (dimension.getAlto() != null && !dimension.getAlto().isBlank()) {
            dimensionExistente.setAlto(dimension.getAlto());
        }
        return dimensionRepository.save(dimensionExistente);
    }

    public void eliminarDimension(Long id) {
        Dimension dimension = dimensionRepository.findById(id).orElse(null);
        if (dimension == null) {
            throw new IllegalArgumentException("Dimensión no encontrada.");
        }
        dimensionRepository.deleteById(id);
    }
}
