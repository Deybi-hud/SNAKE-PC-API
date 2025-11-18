package SNAKE_PC.demo.dto.pedido;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {
    
    private Long id;

    @JsonProperty("fechaPedido")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPedido;

    @JsonProperty("numeroPedido")
    private String numeroPedido;

    @JsonProperty("idContacto")
    private Long idContacto;

    @JsonProperty("idEstadoPedido")
    private Long idEstadoPedido;
}
