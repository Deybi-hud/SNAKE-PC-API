package SNAKE_PC.demo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dyf3i5iqa",
                "api_key", "543736654125764",
                "api_secret", "gHuPr6yCh5s_rcP5cIN9OSUETSk"
        ));
    }
}

