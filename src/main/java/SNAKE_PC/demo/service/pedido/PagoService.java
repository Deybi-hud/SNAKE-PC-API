package SNAKE_PC.demo.service.pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.MetodoPago;
import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.model.pedido.Pedido;
import SNAKE_PC.demo.repository.pedido.MetodoPagoRepository;
import SNAKE_PC.demo.repository.pedido.PagoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {
    
    @Autowired
    private PedidoService pedidoService;

    @Autowired 
    private MetodoPagoRepository metodoPagoRepository;

    @Autowired
    private PagoRepository pagoRepository;

 

    public Pago crearPago(Long pedidoId, String correoUsuario, Long metodoPagoId) {

        if (metodoPagoId == null || metodoPagoId <= 0) {
            throw new RuntimeException("Debe proporcionar un método de pago válido");
        }

        Pedido pedido = pedidoService.obtenerPedidoPorId(pedidoId, correoUsuario);

        if (!pedido.getEstado().getNombre().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden pagar pedidos en estado PENDIENTE. Estado actual: " + pedido.getEstado().getNombre());
        }

        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new RuntimeException("Método de pago no válido"));

        BigDecimal totalPedido = pedidoService.calcularTotalPedido(pedidoId);

        Pago pago = new Pago();
        pago.setMonto(totalPedido);
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("CONFIRMADO");
        pago.setMetodoPago(metodoPago);
        pago.setPedido(pedido);

        Pago pagoCreado = pagoRepository.save(pago);
        
        pedidoService.actualizarEstadoPedido(pedidoId, "PAGADO");

        return pagoCreado;
    }


    public List<Pago> obtenerPagosPorUsuario(String correoUsuario) {
        return pagoRepository.findByPedidoUsuarioCorreo(correoUsuario);
    }


}
