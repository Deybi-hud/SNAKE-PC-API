package SNAKE_PC.demo.service.producto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import SNAKE_PC.demo.model.producto.Imagen;
import SNAKE_PC.demo.model.producto.Producto;
import SNAKE_PC.demo.repository.producto.ImagenRepository;

import java.util.List;

@Service
@RequiredArgsConstructor  // ← ESTO INYECTA AUTOMÁTICO
@Slf4j
public class ImagenService {

    private final ImagenRepository imagenRepository;  // ← SIN @Autowired, CON LOMBOK

    @Transactional
    public List<Imagen> guardarImagenesParaProducto(List<String> urls, Producto producto) {
        if (urls == null || urls.isEmpty()) {
            log.info("No hay imágenes para guardar - Producto ID: {}", producto.getId());
            return List.of();
        }

        List<Imagen> imagenes = urls.stream()
            .filter(url -> url != null && !url.trim().isEmpty())
            .map(url -> {
                Imagen imagen = new Imagen();
                imagen.setImagenUrl(url.trim());
                imagen.setProducto(producto);
                return imagen;
            })
            .toList();

        List<Imagen> guardadas = imagenRepository.saveAllAndFlush(imagenes);
        log.info("Guardadas {} imágenes para producto ID: {} - {}", 
            guardadas.size(), producto.getId(), producto.getNombreProducto());

        return guardadas;
    }

    @Transactional
    public void eliminarImagenesDeProducto(Long productoId) {
        long borradas = imagenRepository.countByProductoId(productoId);
        imagenRepository.deleteByProductoId(productoId);
        log.info("Eliminadas {} imágenes del producto ID: {}", borradas, productoId);
    }

    public List<Imagen> obtenerImagenesDeProducto(Long productoId) {
        return imagenRepository.findByProductoId(productoId);
    }

    @Transactional
    public void eliminarImagenPorId(Long imagenId) {
        Imagen imagen = imagenRepository.findById(imagenId)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        imagenRepository.delete(imagen);
        log.info("Imagen eliminada → ID: {}", imagenId);
    }
}