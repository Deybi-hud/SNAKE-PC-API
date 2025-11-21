package SNAKE_PC.demo.service.pedido;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.MetodoPago;
import SNAKE_PC.demo.model.pedido.Pago;
import SNAKE_PC.demo.model.pedido.Pedido;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoService {
    
    @Autowired
    private PedidoService pedidoService;

    public Pago crearPago(Long pedidoId, String correoUsuario, Long metodoPagoId) {

        Pedido pedido = pedidoService.obtenerPedidoPorId(pedidoId,correoUsuario);
        if (!pedido.getContacto().getUsuario().getCorreo().equals(correoUsuario)) {
            throw new RuntimeException("No tiene permisos para pagar este pedido");
        }
        if (!pedido.getEstado().getNombre().equals("PENDIENTE")) {
            throw new RuntimeException("Solo se pueden pagar pedidos en estado PENDIENTE");
        }

        MetodoPago metodoPago = metodoPagoRepository.findById(metodoPagoId)
                .orElseThrow(() -> new RuntimeException("Método de pago no válido"));

        Double totalPedido = calcularTotalPedido(pedidoId);

        Pago pago = new Pago();
        pago.setMonto(totalPedido);
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("PAGADO");
        pago.setMetodoPago(metodoPago);
        pago.setPedido(pedido);

        Pago pagoCreado = pagoRepository.save(pago);

        actualizarEstadoPedido(pedidoId, "CONFIRMADO");

        return pagoCreado;
    }
}
