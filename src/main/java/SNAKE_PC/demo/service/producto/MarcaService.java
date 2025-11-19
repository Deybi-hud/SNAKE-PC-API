package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Marca;
import SNAKE_PC.demo.repository.producto.MarcaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    public List<Marca> buscarTodos() {
        return marcaRepository.findAll();
    }

    public Marca buscarPorId(Long id) {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) {
            throw new IllegalArgumentException("Marca no encontrada.");
        }
        return marca;
    }

    public Marca buscarPorNombre(String nombre) {
        Marca marca = marcaRepository.findByNombre(nombre).orElse(null);
        if (marca == null) {
            throw new IllegalArgumentException("Marca no encontrada.");
        }
        return marca;
    }

    public Marca guardarMarca(Marca marca) {
        if (marca.getNombre() == null || marca.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la marca no puede estar vacío.");
        }
        return marcaRepository.save(marca);
    }

    public Marca actualizarMarca(Long id, Marca marca) {
        Marca marcaExistente = marcaRepository.findById(id).orElse(null);
        if (marcaExistente == null) {
            throw new IllegalArgumentException("Marca no encontrada.");
        }
        if (marca.getNombre() != null && !marca.getNombre().isBlank()) {
            marcaExistente.setNombre(marca.getNombre());
        }
        return marcaRepository.save(marcaExistente);
    }

    public void eliminarMarca(Long id) {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca == null) {
            throw new IllegalArgumentException("Marca no encontrada.");
        }
        marcaRepository.deleteById(id);
    }
}
