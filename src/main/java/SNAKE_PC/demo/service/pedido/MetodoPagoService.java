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
            throw new RuntimeException("Debe ingresar un metodo valído");
        }
        String tipoPagoNormalizado = metodoPago.getTipoPago().trim().toLowerCase();
        return metodoPagoRepository.findByTipoPago(tipoPagoNormalizado)
            .orElseGet(()->{
                MetodoPago nuevoMetodoPago = new MetodoPago();
                nuevoMetodoPago.setTipoPago(metodoPago.getTipoPago());
                return metodoPagoRepository.save(nuevoMetodoPago);
            });
    } 

    public MetodoPago guardarMetodoSeleccionado(MetodoPago metodoPago){
        return metodoPagoRepository.findByTipoPago(metodoPago.getTipoPago())
            .orElseThrow(()->new RuntimeException("Metodo de pago invalido"));
    }
           


    public MetodoPago buscarTipoPago(MetodoPago metodoPago){
        if(metodoPagoRepository.existsByTipoPago(metodoPago.getTipoPago())){
            throw new RuntimeException("El metodo no existe");
        }
        return metodoPago;
    }


    

}
