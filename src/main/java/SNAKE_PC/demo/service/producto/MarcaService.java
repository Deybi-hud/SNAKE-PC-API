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

    public Marca buscarPorNombre(String marcaNombre) {
        if(marcaNombre == null || marcaNombre.trim().isEmpty()) {
            throw new RuntimeException("Debe ingresar el nombre de la marca");
        }
        String nombre = marcaNombre.trim().toUpperCase();
        return marcaRepository.findByMarcaNombre(nombre)
            .orElseThrow(()->new RuntimeException("No se encontro la marca."));
    }

    public Marca guardarMarca(Marca marca) {
        if (marca.getMarcaNombre() == null || marca.getMarcaNombre().trim().isBlank()) {
            throw new RuntimeException("El nombre de la marca no puede estar vacío.");
        }

        String normalizado = marca.getMarcaNombre().trim();
        return marcaRepository.findByMarcaNombre(normalizado)
            .orElseGet(()->{
                Marca nuevaMarca = new Marca();
                nuevaMarca.setMarcaNombre(marca.getMarcaNombre());
                return marcaRepository.save(nuevaMarca);
            });
    }


    public Marca actualizarMarca(Long id, Marca marca) {
        Marca marcaExistente = marcaRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Marca no encontrada"));
        if (marca.getMarcaNombre() != null && !marca.getMarcaNombre().isBlank()) {
            marcaExistente.setMarcaNombre(marca.getMarcaNombre());
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
