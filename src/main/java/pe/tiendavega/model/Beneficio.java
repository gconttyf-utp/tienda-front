package pe.tiendavega.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Beneficio implements Serializable {

    private static final long serialVersionUID = 1561656255891459623L;

    private String titulo;
    private String descripcion;
    private String icono;

}
