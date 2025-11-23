package SNAKE_PC.demo.service;

import java.io.IOException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImagenService {

    @Autowired
    private Cloudinary cloudinary;

    @SuppressWarnings("unchecked")
    public String subirImagen(MultipartFile archivo) throws IOException {

        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                archivo.getBytes(),
                ObjectUtils.asMap(
                        "folder", "snakepc/usuarios"
                )
        );

        return uploadResult.get("secure_url").toString();  // ← URL segura https
    }
}
