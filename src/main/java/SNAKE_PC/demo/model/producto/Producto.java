package SNAKE_PC.demo.model.producto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombreProducto;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @ManyToOne
    @JoinColumn(name = "id_productoCategoria", nullable = false)
    private ProductoCategoria productoCategoria;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_dimension")
    private Dimension dimension;

    @ManyToOne
    @JoinColumn(name = "id_especificacion")
    private Especificacion especificacion;

    @ManyToOne
    @JoinColumn(name = "id_refrigeracion")
    private Refrigeracion refrigeracion;

    @OneToMany(mappedBy = "producto")
    private List<ColorProducto> colores;
}
