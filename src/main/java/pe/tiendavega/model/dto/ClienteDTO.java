package pe.tiendavega.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private Integer id;

    private String correo;

    private String clave;

    private String nombres;

    private String apellidos;

    private int estado;

}
