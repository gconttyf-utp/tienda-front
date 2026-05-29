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
public class AlmacenDTO {

    private Integer id;

    private Integer tiendaId;

    private Integer productoId;

    private LocalDateTime fechaIngreso;

    private Integer cantidad;

}
