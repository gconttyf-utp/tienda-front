package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PedidoConsumer;
import pe.tiendavega.consumer.ProductoConsumer;
import pe.tiendavega.consumer.TiendaConsumer;
import pe.tiendavega.model.dto.PedidoDTO;

@Named("pedidoUsuarioBean")
@ViewScoped
public class PedidoUsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PedidoConsumer pedidoConsumer;

    @Inject
    private LoginBean loginBean;

    private List<PedidoDTO> listadoSeguimientoPendienteDespacho;
    private List<PedidoDTO> listadoSeguimientoPendienteEntrega;

    private String titulo;

    private Integer pedidoId;

    private PedidoDTO pedidoDTO;

    @Inject
    private FacesContext facesContext; 

    @PostConstruct
    public void init() {
        this.titulo = "Listado de Pedidos";
        listadoSeguimientoPendienteDespacho = new ArrayList<>();
        listadoSeguimientoPendienteEntrega = new ArrayList<>();
        String token = loginBean.getTokenUsuario();

        if (token != null && !token.isEmpty()) {
            try {
                // El consumidor actualmente retorna un solo PedidoDTO
                List<PedidoDTO> despacho = pedidoConsumer.pedidosPendienteDespacho(token);
                if (despacho != null) {
                    listadoSeguimientoPendienteDespacho = despacho;
                }

                List<PedidoDTO> entrega = pedidoConsumer.pedidosPendienteEntrega(token);
                if (entrega != null) {
                    listadoSeguimientoPendienteEntrega = entrega;
                }
            } catch (Exception e) {
                System.out.println("Error al obtener pedidos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public List<PedidoDTO> getListadoSeguimientoPendienteDespacho() {
        return listadoSeguimientoPendienteDespacho;
    }

    public void setListadoSeguimientoPendienteDespacho(List<PedidoDTO> listadoSeguimientoPendienteDespacho) {
        this.listadoSeguimientoPendienteDespacho = listadoSeguimientoPendienteDespacho;
    }

    public List<PedidoDTO> getListadoSeguimientoPendienteEntrega() {
        return listadoSeguimientoPendienteEntrega;
    }

    public void setListadoSeguimientoPendienteEntrega(List<PedidoDTO> listadoSeguimientoPendienteEntrega) {
        this.listadoSeguimientoPendienteEntrega = listadoSeguimientoPendienteEntrega;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public void toggleDetalle(Integer id) {
        if (this.pedidoId != null && this.pedidoId.equals(id)) {
            this.pedidoId = null;
        } else {
            this.pedidoId = id;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Inject
    private TiendaConsumer tiendaConsumer;

    @Inject
    private ProductoConsumer productoConsumer;

    public String obtenerNombreTienda(int id) {
        return tiendaConsumer.obtenerTienda(loginBean.getTokenUsuario(), id).getNombre();
    }

    public String obtenerNombreProducto(Integer idProducto){
        return productoConsumer.obtenerProducto(loginBean.getTokenUsuario(), idProducto).getDescripcion();
    }

    public String enviarADespacho(Integer id) {
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando enviar pedido a despacho pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        try {
            PedidoDTO pedidoDTO = pedidoConsumer.actualizarDespacho(token, id);
            if (pedidoDTO != null) {
                System.out.println("Pedido " + pedidoDTO.getId() + " enviado a despacho exitosamente.");
                facesContext.addMessage(null, new FacesMessage("Pedido " + pedidoDTO.getId() + " enviado a despacho exitosamente!"));
            } else {
                System.out.println("Error al intentar enviar pedido a despacho.");
                facesContext.addMessage(null, new FacesMessage("Error al intentar enviar pedido a despacho."));
            }

            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            return "seguimientoadmin.jsf?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("Error al enviar pedido a despacho: " + e.getMessage());
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage("Error al enviar pedido a despacho."));
            return "seguimientoadmin.jsf?faces-redirect=true";
        }
    }

    public String enviarAEntregado(Integer id) {
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando enviar pedido a entregado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        try {
            PedidoDTO pedidoDTO = pedidoConsumer.actualizarEntrega(token, id);
            if (pedidoDTO != null) {
                System.out.println("Pedido " + pedidoDTO.getId() + " enviado a entregado exitosamente.");
                facesContext.addMessage(null, new FacesMessage("Pedido " + pedidoDTO.getId() + " enviado a entregado exitosamente!"));
            } else {
                System.out.println("Error al intentar enviar pedido a entregado.");
                facesContext.addMessage(null, new FacesMessage("Error al intentar enviar pedido a despacho."));
            }

            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            return "despachoadmin.jsf?faces-redirect=true";
        } catch (Exception e) {
            System.out.println("Error al enviar pedido a despacho: " + e.getMessage());
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage("Error al enviar pedido a despacho."));
            return "despachoadmin.jsf?faces-redirect=true";
        }
    }
    
}
