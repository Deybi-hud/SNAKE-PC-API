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

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

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
                imagen.setUrl(url.trim());
                imagen.setProducto(producto);
                return imagen;
            })
            .toList();

        List<Imagen> guardadas = imagenRepository.saveAllAndFlush(imagenes);

        log.info("Guardadas {} imágenes para producto ID: {} - {}", 
            guardadas.size(), producto.getId(), producto.getNombreProducto());

        return guardadas;
    }

    public void eliminarImagenesDeProducto(Long productoId) {
        long borradas = imagenRepository.deleteByProductoId(productoId);
        log.info("Eliminadas {} imágenes del producto ID: {}", borradas, productoId);
    }

    public List<Imagen> obtenerImagenesDeProducto(Long productoId) {
        return imagenRepository.findByProductoId(productoId);
    }
}