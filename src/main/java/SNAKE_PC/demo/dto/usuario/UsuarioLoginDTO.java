package SNAKE_PC.demo.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLoginDTO {
    
    @NotBlank(message = "El correo o nombre de usuario es obligatorio")
    private String correoONombreUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
