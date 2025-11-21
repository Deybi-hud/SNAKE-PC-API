package SNAKE_PC.demo.service.producto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.producto.Categoria;
import SNAKE_PC.demo.repository.producto.CategoriaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> buscarTodos() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        return categoria;
    }

    public Categoria buscarPorNombre(String nombre) {
        Categoria categoria = categoriaRepository.findByNombreCategoria(nombre).orElse(null);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        return categoria;
    }

    public Categoria guardarCategoria(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            throw new RuntimeException("El nombre de la categoría no puede estar vacío.");
        }
        return categoriaRepository.findByNombreCategoria(nombreCategoria)
                .orElseGet(()->{
                    Categoria nuevaCategoria = new Categoria();
                    nuevaCategoria.setNombreCategoria(nombreCategoria);
                    return categoriaRepository.save(nuevaCategoria);
        });  
    }

    public Categoria actualizarCategoria(Long id, Categoria categoria) {
        Categoria categoriaExistente = categoriaRepository.findById(id).orElse(null);
        if (categoriaExistente == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        if (categoria.getNombreCategoria() != null && !categoria.getNombreCategoria().isBlank()) {
            categoriaExistente.setNombreCategoria(categoria.getNombreCategoria());
        }
        return categoriaRepository.save(categoriaExistente);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if (categoria == null) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }
        categoriaRepository.deleteById(id);
    }
}
