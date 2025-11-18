package SNAKE_PC.demo.dto.producto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDTO {
    
    private Long id;

    @JsonProperty("nombreProducto")
    private String nombreProducto;

    @JsonProperty("stock")
    private Integer stock;

    @JsonProperty("precio")
    private Double precio;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("idProductoCategoria")
    private Long idProductoCategoria;

    @JsonProperty("idMarca")
    private Long idMarca;

    @JsonProperty("idDimension")
    private Long idDimension;

    @JsonProperty("idEspecificacion")
    private Long idEspecificacion;
}
