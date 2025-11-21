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

    @Query("SELECT p FROM Pedido p WHERE p.contacto.usuario.id = :usuarioId")
    List<Pedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT p FROM Pedido p WHERE p.contacto.usuario.correo = :correo")
    List<Pedido> findByUsuarioCorreo(@Param("correo") String correo);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.contacto c JOIN FETCH c.usuario u JOIN FETCH p.estado ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllWithDetails();

    @Query("SELECT p FROM Pedido p WHERE p.estado.nombre = :estado")
    List<Pedido> findByEstado(@Param("estado") String estado);

    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido >= :fechaLimite ORDER BY p.fechaPedido DESC")
    List<Pedido> findPedidosRecientes(@Param("fechaLimite") LocalDate fechaLimite);
}