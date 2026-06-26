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
import pe.tiendavega.consumer.UsuarioConsumer;
import pe.tiendavega.model.dto.RolDTO;
import pe.tiendavega.model.dto.UsuarioDTO;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer rolId;
    private UsuarioDTO usuarioDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private UsuarioConsumer usuarioConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Grupos por Departamento ";
    }

    @Produces
    @Model
    public UsuarioDTO usuarioDTO() {
        this.usuarioDTO = new UsuarioDTO();
        System.out.println("ID usuarioDTO= " + id);
        if ( id != null && id > 0 ){
            this.usuarioDTO = usuarioConsumer.obtenerUsuario(loginBean.getTokenUsuario(), id);
            System.out.println("ID usuarioDTO= " + usuarioDTO.toString());
        }
        return this.usuarioDTO;
    }

    public List<UsuarioDTO> obtenerUsuarios(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener usuarios pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        return usuarioConsumer.obtenerUsuarios(token, List.of(0,1));
    }

    public String cambiarEstado(UsuarioDTO usuario){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = usuarioConsumer.eliminarUsuario(token, usuario.getId());
        if (exito) {
            System.out.println("Estado de usuario actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Usuario " + usuario.getApellidos() + "," + usuario.getNombres() + " actualizado el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado del usuario.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del usuario."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "usuario.jsf?faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar grupo pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        System.out.println("usuarioDTO= " + usuarioDTO);
        UsuarioDTO usuarioDB = null;

        if ( usuarioDTO.getId() != null && usuarioDTO.getId() > 0 ){
            System.out.println("MODIFICAR USUARIO : " + usuarioDTO.getId());
            usuarioDB = usuarioConsumer.actualizarUsuario(token, usuarioDTO.getId(), usuarioDTO);
        } else {
            System.out.println("GRABAR NUEVO USUARIO");
            usuarioDB = usuarioConsumer.crearUsuario(token, usuarioDTO);
        }

        if( usuarioDB != null ){
            this.usuarioDTO = usuarioDB;
            if (usuarioDTO.getId() != null && usuarioDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Usuario " + usuarioDTO.getApellidos() + "," + usuarioDTO.getNombres() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Usuario " + usuarioDTO.getApellidos() + "," + usuarioDTO.getNombres() + " creado con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar el usuario."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/usuario.jsf?faces-redirect=true";
    }

    public String cambiarClave(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar grupo pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        System.out.println("usuarioDTO= " + usuarioDTO);
        Boolean exito = false;

        if ( usuarioDTO.getId() != null && usuarioDTO.getId() > 0 ){
            System.out.println("GRABAR NUEVA CLAVE DE USUARIO : " + usuarioDTO.getId());
            exito = usuarioConsumer.nuevaClaveUsuario(token, usuarioDTO.getId(), usuarioDTO.getClave());
        }

        if( exito ){
            facesContext.addMessage(null, new FacesMessage("La contraseña del usuario " + usuarioDTO.getApellidos() + "," + usuarioDTO.getNombres() + " fue actualizada con exito!"));
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al actaulizar contraseña del usuario."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/usuario.jsf?faces-redirect=true";
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

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    @Inject
    private RolConsumer rolConsumer;

    public String nombreRol(Integer rolId){
        return rolConsumer.obtenerRol(loginBean.getTokenUsuario(), rolId).getDescripcion();
    }

    public List<RolDTO> getRoles() {
        return rolConsumer.obtenerRoles(loginBean.getTokenUsuario(), List.of(1));
    }

}
