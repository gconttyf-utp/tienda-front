package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PedidoConsumer;
import pe.tiendavega.consumer.ProductoConsumer;
import pe.tiendavega.consumer.ReclamoConsumer;
import pe.tiendavega.model.dto.PedidoDTO;
import pe.tiendavega.model.dto.ReclamoDTO;

@Named("reclamoUsuarioBean")
@ViewScoped
public class ReclamoUsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ReclamoConsumer reclamoConsumer;

    @Inject
    private LoginBean loginBean;

    private List<ReclamoDTO> reclamos;

    private Integer reclamoId;

    private Integer pedidoId;

    private ReclamoDTO reclamoDTO = new ReclamoDTO();

    private PedidoDTO pedidoDTO = new PedidoDTO();

    private String titulo;

    @Inject
    private FacesContext facesContext; 

    @PostConstruct
    public void init() {
        this.titulo = "Listado de Reclamos";
        reclamos = new ArrayList<>();
        String token = loginBean.getTokenUsuario();
        if (token != null && !token.isEmpty()) {
            try {
                List<ReclamoDTO> lista = reclamoConsumer.listarReclamos(token);
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Inject
    private PedidoConsumer pedidoConsumer;

    public void obtenerPedido(){
        System.out.println("reclamoId= " + reclamoId);
        if(reclamoId != null) {
            try {
                reclamoDTO = reclamoConsumer.obtenerReclamo(loginBean.getTokenUsuario(), reclamoId);
                System.out.println("reclamoDTO= " + reclamoDTO);
                
                pedidoDTO = pedidoConsumer.obtenerPedido(loginBean.getTokenUsuario(), reclamoDTO.getPedidoID());
                System.out.println("pedidoDTO= " + pedidoDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject
    private ProductoConsumer productoConsumer;
    
    public String obtenerNombreProducto(Integer idProducto){
        return productoConsumer.obtenerProducto(loginBean.getTokenUsuario(), idProducto).getDescripcion();
    }

    public String reclamoAdminFormulario(Integer reclamoId){
        this.reclamoId = reclamoId;
        return "reclamoadminform?faces-redirect=true&reclamoId=" + reclamoId;
    }

    public String actualizarReclamo() {
        if (reclamoDTO != null && reclamoDTO.getId() != null) {
            try {
                reclamoConsumer.actualizarReclamo(loginBean.getTokenUsuario(), reclamoDTO.getId(), reclamoDTO);
                facesContext.addMessage(null, new jakarta.faces.application.FacesMessage("Reclamo actualizado exitosamente."));
            } catch (Exception e) {
                e.printStackTrace();
                facesContext.addMessage(null, new jakarta.faces.application.FacesMessage("Error al actualizar el reclamo."));
                return null;
            }
        }
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "reclamoadmin.jsf?faces-redirect=true";
    }
}