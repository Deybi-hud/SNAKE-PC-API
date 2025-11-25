package SNAKE_PC.demo.service.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SNAKE_PC.demo.model.pedido.MetodoPago;
import SNAKE_PC.demo.repository.pedido.MetodoPagoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MetodoPagoService {   
    
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;
    
    public MetodoPago crearMetodoPago(MetodoPago metodoPago){
        if(metodoPago.getTipoPago() == null || metodoPago.getTipoPago().trim().isEmpty()){
            throw new RuntimeException("Debe ingresar un tipo de pago válido");
        }
        String tipoPago = metodoPago.getTipoPago().trim();
        boolean yaExiste = metodoPagoRepository.existsByTipoPago(tipoPago);
        if(yaExiste){
            throw new RuntimeException("El método de pago '" + tipoPago + "' ya existe");
        }
        MetodoPago nuevoMetodoPago = new MetodoPago();
        nuevoMetodoPago.setTipoPago(tipoPago);
        return metodoPagoRepository.save(nuevoMetodoPago);
    } 

}
