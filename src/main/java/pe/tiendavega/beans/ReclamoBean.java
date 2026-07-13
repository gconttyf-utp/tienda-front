package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PedidoConsumer;
import pe.tiendavega.consumer.ProductoConsumer;
import pe.tiendavega.consumer.ReclamoConsumer;
import pe.tiendavega.model.dto.PedidoDTO;
import pe.tiendavega.model.dto.ReclamoDTO;

@Named("reclamoBean")
@ViewScoped
public class ReclamoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ReclamoConsumer reclamoConsumer;

    @Inject
    private LoginBean loginBean;

    private List<ReclamoDTO> reclamos;

    private Integer reclamoId;

    private Integer pedidoId;

    private ReclamoDTO reclamoDTO;

    private PedidoDTO pedidoDTO = new PedidoDTO();

    @PostConstruct
    public void init() {
        reclamos = new ArrayList<>();
        String token = loginBean.getTokenCliente();
        if (token != null && !token.isEmpty()) {
            try {
                List<ReclamoDTO> lista = reclamoConsumer.listarReclamosCliente(token);
                if (lista != null) {
                    reclamos = lista;
                }
            } catch (Exception e) {
                System.out.println("Error al obtener reclamos del cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public List<ReclamoDTO> getReclamos() {
        return reclamos;
    }

    public void setReclamos(List<ReclamoDTO> reclamos) {
        this.reclamos = reclamos;
    }

    public Integer getReclamoId() {
        return reclamoId;
    }

    public void setReclamoId(Integer reclamoId) {
        this.reclamoId = reclamoId;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public ReclamoDTO getReclamoDTO() {
        return reclamoDTO;
    }

    public void setReclamoDTO(ReclamoDTO reclamoDTO) {
        this.reclamoDTO = reclamoDTO;
    }

    public PedidoDTO getPedidoDTO() {
        return pedidoDTO;
    }

    public void setPedidoDTO(PedidoDTO pedidoDTO) {
        this.pedidoDTO = pedidoDTO;
    }

    @Inject
    private PedidoConsumer pedidoConsumer;

    public void obtenerPedido(){
        if(pedidoId != null) {
            try {
                reclamoDTO = new ReclamoDTO();
                reclamoDTO.setPedidoID( pedidoId );
                
                pedidoDTO = pedidoConsumer.obtenerPedido(loginBean.getTokenCliente(), pedidoId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String crearReclamo() {
        if (reclamoDTO != null) {
            try {
                reclamoConsumer.crearReclamo(loginBean.getTokenCliente(), reclamoDTO);
            } catch (Exception e) {                
                e.printStackTrace();
                return "reclamosform.jsf?pedidoId="+pedidoId+"&faces-redirect=true";
            }
        }
        return "reclamos.jsf?faces-redirect=true";
    }

    @Inject
    private ProductoConsumer productoConsumer;
    
    public String obtenerNombreProducto(Integer idProducto){
        return productoConsumer.obtenerProducto(loginBean.getTokenCliente(), idProducto).getDescripcion();
    }
}
