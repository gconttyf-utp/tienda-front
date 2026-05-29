package pe.tiendavega.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Integer id;

    private String nombres;

    private String apellidos;

    private String usuario;

    private String clave;

    private Integer estado;

    private Integer rolId;
    
}
