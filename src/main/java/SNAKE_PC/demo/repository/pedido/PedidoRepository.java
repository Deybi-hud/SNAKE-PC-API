package SNAKE_PC.demo.repository.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import SNAKE_PC.demo.model.pedido.Pedido;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    // ✅ 1. PEDIDOS DE UN USUARIO ESPECÍFICO (por ID de usuario)
    @Query("SELECT p FROM Pedido p WHERE p.contacto.usuario.id = :usuarioId")
    List<Pedido> findByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    // ✅ 2. PEDIDOS DE UN USUARIO ESPECÍFICO (por correo)
    @Query("SELECT p FROM Pedido p WHERE p.contacto.usuario.correo = :correo")
    List<Pedido> findByUsuarioCorreo(@Param("correo") String correo);
    
    // ✅ 3. TODOS LOS PEDIDOS CON INFORMACIÓN COMPLETA (para Admin)
    @Query("SELECT p FROM Pedido p JOIN FETCH p.contacto c JOIN FETCH c.usuario u JOIN FETCH p.estado ORDER BY p.fechaPedido DESC")
    List<Pedido> findAllWithDetails();
    
    // ✅ 4. PEDIDOS POR ESTADO (para Admin)
    @Query("SELECT p FROM Pedido p WHERE p.estado.nombre = :estado")
    List<Pedido> findByEstado(@Param("estado") String estado);
    
    // ✅ 5. BUSCAR PEDIDO POR NÚMERO (para usuario y admin)
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    
    // ✅ 6. PEDIDOS RECIENTES (últimos 30 días)
    @Query("SELECT p FROM Pedido p WHERE p.fechaPedido >= CURRENT_DATE - 30 ORDER BY p.fechaPedido DESC")
    List<Pedido> findPedidosRecientes();
}