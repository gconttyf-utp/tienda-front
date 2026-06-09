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
import pe.tiendavega.consumer.DepartamentoConsumer;
import pe.tiendavega.consumer.GrupoConsumer;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;

@Named
@ViewScoped
public class GrupoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer departamentoId;
    private GrupoDTO grupoDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private GrupoConsumer grupoConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Grupos por Departamento ";
    }

    @Produces
    @Model
    public GrupoDTO grupoDTO() {
        this.grupoDTO = new GrupoDTO();
        System.out.println("ID grupoDTO= " + id);
        if ( id != null && id > 0 ){
            this.grupoDTO = grupoConsumer.obtenerGrupo(loginBean.getTokenUsuario(), id);
        }
        return this.grupoDTO;
    }

    public List<GrupoDTO> obtenerGrupos(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener grupos pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        return grupoConsumer.obtenerGrupos(token, departamentoId, List.of(0,1));
    }

    public String cambiarEstado(GrupoDTO grupo){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = grupoConsumer.eliminarGrupo(token, grupo.getId());
        if (exito) {
            System.out.println("Estado de grupo actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Grupo " + grupo.getNombre() + " actualizado el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado del grupo.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del grupo."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "grupo.jsf?depa=" + departamentoId + "&faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar grupo pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        System.out.println("grupoDTO= " + grupoDTO);
        GrupoDTO grupoDB = null;

        if ( grupoDTO.getId() != null && grupoDTO.getId() > 0 ){
            System.out.println("MODIFICAR GRUPO : " + grupoDTO.getId());
            grupoDB = grupoConsumer.actualizarGrupo(token, grupoDTO.getId(), grupoDTO);
        } else {
            System.out.println("GRABAR NUEVO GRUPO");
            grupoDB = grupoConsumer.crearGrupo(token, grupoDTO);
        }

        if( grupoDB != null ){
            this.grupoDTO = grupoDB;
            if (grupoDTO.getId() != null && grupoDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Grupo " + grupoDTO.getNombre() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Grupo " + grupoDTO.getNombre() + " creado con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar el grupo."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/grupo.jsf?depa=" + grupoDTO.getDepartamentoId() + "&faces-redirect=true";
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

    @Inject
    private PublicoConsumer publicoConsumer;

    public List<DepartamentoDTO> getDepartamentos() {
        return publicoConsumer.obtenerDepartamentos();
    }

    @Inject
    private DepartamentoConsumer departamentoConsumer;

    public String nombreDepartamento(Integer id){
        return departamentoConsumer.obtenerDepartamento(loginBean.getTokenUsuario(), id).getNombre();
    }

    public Integer getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Integer departamentoId) {
        this.departamentoId = departamentoId;
    }

}
