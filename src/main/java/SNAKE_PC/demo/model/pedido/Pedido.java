package SNAKE_PC.demo.model.pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import SNAKE_PC.demo.model.usuario.Contacto;
import jakarta.persistence.CascadeType;
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
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_pedido", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPedido;

    @Column(name = "numero_pedido", unique = true)
    private String numeroPedido;

    @Column(name = "total", nullable =  false)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_contacto", nullable = false)
    private Contacto contacto;

    @ManyToOne
    @JoinColumn(name = "id_estado_pedido", nullable = false)
    private EstadoPedido estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<DetallePedido> detalles = new ArrayList<>();
}
