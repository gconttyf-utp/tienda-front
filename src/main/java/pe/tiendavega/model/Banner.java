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
public class Banner implements Serializable {

    private static final long serialVersionUID = 1561656111L;

    private String titulo;
    private String subtitulo;
    private String badge;
    private String cta;
    private String url;
    private String colorFondo;
    private String colorTexto;

}
