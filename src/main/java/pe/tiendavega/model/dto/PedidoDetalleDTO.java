package pe.tiendavega.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDetalleDTO {

    private Integer id;

    private Integer pedidoID;

    private Integer productoID;

    private Integer cantidad;

    private BigDecimal subtotal;
    
}
