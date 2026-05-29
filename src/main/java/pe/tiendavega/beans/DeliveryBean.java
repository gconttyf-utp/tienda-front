package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.dto.TiendaDTO;
import pe.tiendavega.model.dto.UbigeoDTO;

@Named
@SessionScoped
public class DeliveryBean implements Serializable{

    private static final long serialVersionUID = 2314514645321L;

    private PublicoConsumer publicoConsumer;

    private List<TiendaDTO> tiendas;

    private Integer tiendaId;

    @PostConstruct
    public void init() {
        publicoConsumer = new PublicoConsumer();
        tiendas = publicoConsumer.obtenerTiendas();
        if (!tiendas.isEmpty()) {
            tiendaId = tiendas.get(0).getId();
        }
        tiendas.forEach(tienda -> {
            UbigeoDTO ubigeo = publicoConsumer.obtenerUbigeo(tienda.getCodUbigeo());
            if (ubigeo != null) {
                tienda.setNombreUbigeo(ubigeo.getTxtUbigeo());
            }
        });
    }

    public List<TiendaDTO> getTiendas() {
        return tiendas;
    }

    public String getResumenUbicacion() {
        return publicoConsumer.obtenerTiendas().stream()
                .filter(t -> t.getId().equals(tiendaId))
                .findFirst()
                .map(TiendaDTO::getNombre)
                .orElse("Elige tu tienda");
    }

    public Integer getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Integer tiendaId) {
        this.tiendaId = tiendaId;
    }

    public String confirmarTienda(){
        System.out.println("SE CONFIRMO LA TIENDA :" + tiendaId);
        return "index.jsf";
    }

}
