package SNAKE_PC.demo.model.pedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedido_metodo_envio")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoMetodoEnvio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_metodo_envio", nullable = false)
    private MetodoEnvio metodoEnvio;

    @OneToOne(mappedBy = "pedidoMetodoEnvio", cascade = CascadeType.ALL, optional = false)
    private Pedido pedido;
}
