package SNAKE_PC.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SNAKE-PC API")
                .version("1.0.0")
                .description("API para prueba de funcionalidades del proyecto SNAKE-PC")
                .contact(new Contact().name("SNAKE-PC"))
                .license(new License().name("Apache 2.0")));
    }

}
