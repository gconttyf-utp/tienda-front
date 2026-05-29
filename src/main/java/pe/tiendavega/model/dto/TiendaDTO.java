package pe.tiendavega.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TiendaDTO {

    private Integer id;

    private String nombre;

    private String direccion;

    private Integer estado;

    private String codUbigeo;

    private String horario;

    private String telefono;

    private String imagenUrl;

    private Double latitud;

    private Double longitud;

    private String nombreUbigeo;
    
}
