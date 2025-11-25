package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.Pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByFechaPedido(LocalDate fechaPedido);

    List<Pedido> findByFechaPedidoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Pedido> findByFechaPedidoBetweenOrderByFechaPedidoDesc(LocalDate fechaInicio, LocalDate fechaFin);

    @Query("SELECT p FROM Pedido p WHERE p.usuario.id = :usuarioId")
    List<Pedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario u JOIN FETCH p.estado ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllWithDetails();

    @Query("SELECT p FROM Pedido p WHERE p.estado.nombre = :estado")
    List<Pedido> findByEstado(@Param("estado") String estado);

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido >= :fechaLimite ORDER BY p.fechaPedido DESC")
    List<Pedido> findPedidosRecientes(@Param("fechaLimite") LocalDate fechaLimite);

    @Query(value = """
        SELECT 
        COUNT(p.id) as total_pedidos,
        COALESCE(SUM(d.precio_unitario * d.cantidad), 0) + 
        COALESCE(SUM(me.costo_envio), 0) as total_ventas
        FROM pedido p
        JOIN detalle_pedido d ON d.id_pedido = p.id
        LEFT JOIN metodo_envio me ON d.id_metodo_envio = me.id
        WHERE DATE(p.fecha_pedido) = CURRENT_DATE
    """, nativeQuery = true)
    Object[] estadisticasDelDiaNative();

    @Query(value = """
        SELECT 
        COUNT(p.id) as total_pedidos,
        COALESCE(SUM(d.precio_unitario * d.cantidad), 0) + 
        COALESCE(SUM(me.costo_envio), 0) as total_ventas
        FROM pedido p
        JOIN detalle_pedido d ON d.id_pedido = p.id
        LEFT JOIN metodo_envio me ON d.id_metodo_envio = me.id
        WHERE p.fecha_pedido >= date_trunc('month', current_date)
        AND p.fecha_pedido < date_trunc('month', current_date) + interval '1 month'
        """, nativeQuery = true)
    Object[] estadisticasDelMesNative();


}