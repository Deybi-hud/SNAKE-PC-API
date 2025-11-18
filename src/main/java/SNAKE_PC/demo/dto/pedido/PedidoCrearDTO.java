package SNAKE_PC.demo.dto.pedido;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
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
public class PedidoCrearDTO {
    
    @NotNull(message = "La fecha del pedido es obligatoria")
    @FutureOrPresent(message = "La fecha del pedido no puede ser en el pasado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPedido;

    @NotBlank(message = "El número de pedido es obligatorio")
    @Pattern(regexp = "^PED-[0-9]{8,12}$", message = "El número de pedido debe estar en formato PED-XXXXXXXX")
    private String numeroPedido;

    @NotNull(message = "El ID del contacto es obligatorio")
    @Positive(message = "El ID del contacto debe ser positivo")
    private Long idContacto;

    @NotNull(message = "El ID del estado es obligatorio")
    @Positive(message = "El ID del estado debe ser positivo")
    private Long idEstadoPedido;
}
