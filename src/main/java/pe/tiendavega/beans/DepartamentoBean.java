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
import pe.tiendavega.model.dto.DepartamentoDTO;

@Named
@ViewScoped
public class DepartamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private DepartamentoDTO departamentoDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private DepartamentoConsumer departamentoConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Departamentos";
    }

    @Produces
    @Model
    public DepartamentoDTO departamentoDTO() {
        this.departamentoDTO = new DepartamentoDTO();
        if ( id != null && id > 0 ){
            this.departamentoDTO = departamentoConsumer.obtenerDepartamento(loginBean.getTokenUsuario(), id);
        }
        return this.departamentoDTO;
    }

    public List<DepartamentoDTO> obtenerDepartamentos(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener departamentos pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        System.out.println("Enviando petición con token: " + token);
        return departamentoConsumer.obtenerDepartamentos(token, List.of(0,1));
    }

    public String cambiarEstado(DepartamentoDTO departamento){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = departamentoConsumer.eliminarDepartamento(token, departamento.getId());
        if (exito) {
            System.out.println("Estado de departamento actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Departamento " + departamento.getNombre() + " actualizada el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado de la departamento.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del departamento."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "departamento.jsf?faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar departamento pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        DepartamentoDTO departamentoDB = null;

        if ( departamentoDTO.getId() != null && departamentoDTO.getId() > 0 ){
            System.out.println("MODIFICAR DEPARTAMENTO : " + departamentoDTO.getId());
            departamentoDB = departamentoConsumer.actualizarDepartamento(token, departamentoDTO.getId(), departamentoDTO);
        } else {
            System.out.println("GRABAR NUEVO DEPARTAMENTO");
            departamentoDB = departamentoConsumer.crearDepartamento(token, departamentoDTO);
        }

        if( departamentoDB != null ){
            this.departamentoDTO = departamentoDB;
            if (departamentoDTO.getId() != null && departamentoDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Departamento " + departamentoDTO.getNombre() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Departamento " + departamentoDTO.getNombre() + " creada con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar el departamento."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/departamento.jsf?faces-redirect=true";
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
