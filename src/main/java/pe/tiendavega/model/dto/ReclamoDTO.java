package pe.tiendavega.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReclamoDTO {

    private Integer id;

    private Integer clienteID;

    private Integer pedidoID;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaAtencion;

    private String comentario;

    private String comentarioRespuesta;

    private EstadoReclamo estado = EstadoReclamo.PENDIENTE;

}
