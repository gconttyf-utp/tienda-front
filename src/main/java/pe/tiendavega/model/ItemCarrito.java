package pe.tiendavega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.tiendavega.model.dto.ProductoDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1544633111L;

    private ProductoDTO producto;

    private int cantidad;

    public BigDecimal getSubtotal(){
        if (producto == null || producto.getPrecioOnline() == null)
            return BigDecimal.ZERO;
        
        return producto.getPrecioOnline().multiply(BigDecimal.valueOf(cantidad));
    }

}
