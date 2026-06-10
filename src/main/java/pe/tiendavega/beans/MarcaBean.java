package pe.tiendavega.beans;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.MarcaConsumer;
import pe.tiendavega.model.dto.MarcaDTO;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

@Named
@ViewScoped
public class MarcaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private MarcaDTO marcaDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private MarcaConsumer marcaConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Marcas";
    }

    @Produces
    @Model
    public MarcaDTO marcaDTO() {
        this.marcaDTO = new MarcaDTO();
        if ( id != null && id > 0 ){
            this.marcaDTO = marcaConsumer.obtenerMarca(loginBean.getTokenUsuario(), id);
        }
        return this.marcaDTO;
    }

    public List<MarcaDTO> obtenerMarcas(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener marcas pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        System.out.println("Enviando petición con token: " + token);
        return marcaConsumer.obtenerMarcas(token, List.of(0,1));
    }

    public String cambiarEstado(MarcaDTO marca){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = marcaConsumer.eliminarMarca(token, marca.getId());
        if (exito) {
            System.out.println("Estado de marca actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Marca " + marca.getNombre() + " actualizada el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado de la marca.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado de la marca."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "marca.jsf?faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar marca pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        MarcaDTO marcaDB = null;

        if ( marcaDTO.getId() != null && marcaDTO.getId() > 0 ){
            System.out.println("MODIFICAR MARCA : " + marcaDTO.getId());
            marcaDB = marcaConsumer.actualizarMarca(token, marcaDTO.getId(), marcaDTO);
        } else {
            System.out.println("GRABAR NUEVA MARCA");
            marcaDB = marcaConsumer.crearMarca(token, marcaDTO);
        }

        if( marcaDB != null ){
            this.marcaDTO = marcaDB;
            if (marcaDTO.getId() != null && marcaDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Marca " + marcaDTO.getNombre() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Marca " + marcaDTO.getNombre() + " creada con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar la marca."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/marca.jsf?faces-redirect=true";
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
