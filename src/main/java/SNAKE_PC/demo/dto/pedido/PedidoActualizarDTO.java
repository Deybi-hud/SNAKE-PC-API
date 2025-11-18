package SNAKE_PC.demo.dto.pedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoActualizarDTO {
    
    @NotNull(message = "El estado es obligatorio")
    @Positive(message = "El ID del estado debe ser positivo")
    private Long idEstadoPedido;

    @Pattern(regexp = "^PED-[0-9]{8,12}$", message = "El número de pedido debe estar en formato PED-XXXXXXXX")
    private String numeroPedido;
}
