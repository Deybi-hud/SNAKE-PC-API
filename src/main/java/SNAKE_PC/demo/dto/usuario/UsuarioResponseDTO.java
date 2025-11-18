package SNAKE_PC.demo.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    
    private Long id;

    @JsonProperty("nombreUsuario")
    private String nombreUsuario;

    @JsonProperty("correo")
    private String correo;

    @JsonProperty("activo")
    private boolean activo;
}
