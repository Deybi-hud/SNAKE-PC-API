package SNAKE_PC.demo.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDTO {
    
    @NotBlank(message = "La calle es obligatoria")
    @Size(min = 3, max = 100, message = "La calle debe tener entre 3 y 100 caracteres")
    private String calle;

    @NotNull(message = "El número de calle es obligatorio")
    @Positive(message = "El número de calle debe ser positivo")
    private Integer numero;

    @NotBlank(message = "El departamento/apartamento es obligatorio")
    @Size(min = 1, max = 50, message = "El departamento debe tener entre 1 y 50 caracteres")
    private String departamento;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "El código postal debe contener entre 5 y 10 números")
    private String codigoPostal;

    @NotNull(message = "El ID de la comuna es obligatorio")
    @Positive(message = "El ID de la comuna debe ser positivo")
    private Long idComuna;
}
