package SNAKE_PC.demo.model.producto;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    private BigDecimal precio;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "peso", nullable = true)
    private String peso;

    @ManyToOne
    @JoinColumn(name = "id_productoCategoria", nullable = true)
    private ProductoCategoria productoCategoria;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = true)
    private Marca marca;

    @OneToOne
    @JoinColumn(name = "id_especificacion", nullable = true)
    private Especificacion especificacion;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Imagen> imagenes = new HashSet<>();

}
