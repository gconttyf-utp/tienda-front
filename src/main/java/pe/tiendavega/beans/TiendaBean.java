package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.TiendaConsumer;
import pe.tiendavega.model.dto.TiendaDTO;

@Named
@ViewScoped
public class TiendaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private TiendaDTO tiendaDTO;
    private List<Integer> estados;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private TiendaConsumer tiendaConsumer;

    @Inject
    private FacesContext facesContext;

    public List<TiendaDTO> obtenerTiendas(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener tiendas pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        System.out.println("Enviando petición con token: " + token);
        return tiendaConsumer.obtenerTiendas(token, List.of(0,1));
    }

    public String cambiarEstado(TiendaDTO tienda){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = tiendaConsumer.eliminarTienda(token, tienda.getId());
        if (exito) {
            System.out.println("Estado de tienda actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Tienda " + tienda.getNombre() + " actualizada el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado de la tienda.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado de la tienda."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "tiendas.jsf?faces-redirect=true";
    }

    @Produces
    @Model
    public TiendaDTO tiendaDTO() {
        this.tiendaDTO = new TiendaDTO();
        if ( id != null && id > 0 ){
            this.tiendaDTO = tiendaConsumer.obtenerTienda(loginBean.getTokenUsuario(), id);
        }
        return this.tiendaDTO;
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();

        tiendaDTO.setImagenUrl("");

        TiendaDTO tiendaDB = null;

        if ( tiendaDTO.getId() != null && tiendaDTO.getId() > 0 ){
            System.out.println("MODIFICAR TIENDA : " + tiendaDTO.getId());
            tiendaDB = tiendaConsumer.actualizarTienda(token, tiendaDTO.getId(), tiendaDTO);
        } else {
            System.out.println("GRABAR NUEVA TIENDA");
            tiendaDB = tiendaConsumer.crearTienda(token, tiendaDTO);
        }

        if( tiendaDB != null ){
            this.tiendaDTO = tiendaDB;
            if (tiendaDTO.getId() != null && tiendaDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Tienda " + tiendaDTO.getNombre() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Tienda " + tiendaDTO.getNombre() + " creada con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar la tienda."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/tiendas.jsf?faces-redirect=true";
    }

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Tiendas";
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
