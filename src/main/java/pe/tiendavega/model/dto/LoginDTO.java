package pe.tiendavega.model.dto;

import jakarta.ws.rs.FormParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    @FormParam("usuario")
    private String usuario;

    @FormParam("clave")
    private String clave;

    @FormParam("grant_type")
    private String grant_type;

}
