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
import pe.tiendavega.consumer.RolConsumer;
import pe.tiendavega.model.dto.RolDTO;

@Named
@ViewScoped
public class RolBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private RolDTO rolDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private RolConsumer rolConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Roles";
    }

    @Produces
    @Model
    public RolDTO rolDTO() {
        this.rolDTO = new RolDTO();
        if ( id != null && id > 0 ){
            this.rolDTO = rolConsumer.obtenerRol(loginBean.getTokenUsuario(), id);
        }
        return this.rolDTO;
    }

    public List<RolDTO> obtenerRoles(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener roles pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        System.out.println("Enviando petición con token: " + token);
        return rolConsumer.obtenerRoles(token, List.of(0,1));
    }

    public String cambiarEstado(RolDTO rol){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = rolConsumer.eliminarRol(token, rol.getId());
        if (exito) {
            System.out.println("Estado de rol actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Rol " + rol.getDescripcion() + " actualizado el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado del rol.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del rol."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "rol.jsf?faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar departamento pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        RolDTO rolDB = null;

        if ( rolDTO.getId() != null && rolDTO.getId() > 0 ){
            System.out.println("MODIFICAR ROL : " + rolDTO.getId());
            rolDB = rolConsumer.actualizarRol(token, rolDTO.getId(), rolDTO);
        } else {
            System.out.println("GRABAR NUEVO ROL");
            //rolDB = rolConsumer.crearRol(token, rolDTO);
        }

        if( rolDB != null ){
            this.rolDTO = rolDB;
            if (rolDTO.getId() != null && rolDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Rol " + rolDTO.getDescripcion() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Rol " + rolDTO.getDescripcion() + " creada con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar el rol."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/rol.jsf?faces-redirect=true";
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
