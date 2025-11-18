package SNAKE_PC.demo.dto.producto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoActualizarDTO {
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombreProducto;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotBlank(message = "El SKU es obligatorio")
    @Pattern(regexp = "^[A-Z0-9-]*$", message = "El SKU solo puede contener letras mayúsculas, números y guiones")
    @Size(min = 3, max = 50, message = "El SKU debe tener entre 3 y 50 caracteres")
    private String sku;

    private Long idProductoCategoria;

    private Long idMarca;

    private Long idDimension;

    private Long idEspecificacion;
}
