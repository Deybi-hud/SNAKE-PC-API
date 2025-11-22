package SNAKE_PC.demo.service.pedido;

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

    @Autowired
    private PagoService pagoService;

    @Autowired 
    private EstadoPedidoService estadoPedidoService;

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

        Double totalPedido = pagoService.calcularTotalPedido(pedidoId);

        Pago pago = new Pago();
        pago.setMonto(totalPedido);
        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("PAGADO");
        pago.setMetodoPago(metodoPago);
        pago.setPedido(pedido);

        Pago pagoCreado = pagoRepository.save(pago);

        estadoPedidoService.actualizarEstadoPedido(pedidoId, "CONFIRMADO");

        return pagoCreado;
    }

     private Double calcularTotalPedido(Long pedidoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularTotalPedido'");
    }

     public List<Pago> obtenerPagosPorUsuario(String correoUsuario) {
        return pagoRepository.findByPedidoContactoUsuarioCorreo(correoUsuario);
    }


}
