package pe.tiendavega.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Integer id;

    private String descripcion;

    private String sku;

    private BigDecimal precioLista;

    private BigDecimal precioOnline;

    private Integer stock;

    private Integer categoriaID;

    private Integer marcaID;

    private String rutaImg;

    private Integer estado;

    private String slug;

    private String unidad;

    private String colorPlaceholder;

    private String descripcionLarga;

    private Integer oferta;

    private Integer destacado;

    private LocalDateTime createdAt;

    private Integer descuento;

    private Boolean stockBajo;

    private Boolean agotado;

    public Integer getMarcaID() {
        if (marcaID == null || marcaID == 0) {
            return 1;
        }
        return marcaID;
    }

}
