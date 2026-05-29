package pe.tiendavega.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UbigeoDTO {

    private String codUbigeo;

    private String txtUbigeo;

    private String codUbigeoPadre;

}
