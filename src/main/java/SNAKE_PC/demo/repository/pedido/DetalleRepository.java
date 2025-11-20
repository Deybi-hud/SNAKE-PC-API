package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.DetallePedido;
import java.util.List;

@Repository
public interface DetalleRepository extends JpaRepository<DetallePedido, Long> {
    
    // ✅ Detalles de un pedido específico
    List<DetallePedido> findByPedidoId(Long pedidoId);
    
    // ✅ Detalles con información completa del producto
    @Query("SELECT dp FROM DetallePedido dp JOIN FETCH dp.producto WHERE dp.pedido.id = :pedidoId")
    List<DetallePedido> findByPedidoIdWithProducto(@Param("pedidoId") Long pedidoId);
}