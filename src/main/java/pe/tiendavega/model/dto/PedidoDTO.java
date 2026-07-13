package pe.tiendavega.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    private Integer id;

    private Integer clienteID;

    private Integer tiendaID;

    private BigDecimal total;

    private EstadoPedido estado = EstadoPedido.PENDIENTE_PAGO;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaPago;

    private LocalDateTime fechaDespacho;

    private LocalDateTime fechaRecojo;

    private List<PedidoDetalleDTO> detalles = new ArrayList<>();

    private Integer tipoPago;

}
