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
import pe.tiendavega.consumer.CategoriaConsumer;
import pe.tiendavega.consumer.DepartamentoConsumer;
import pe.tiendavega.consumer.GrupoConsumer;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.dto.CategoriaDTO;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;

@Named
@ViewScoped
public class CategoriaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer departamentoId;
    private Integer grupoId;
    private CategoriaDTO categoriaDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private CategoriaConsumer categoriaConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Categorías";
    }

    @Produces
    @Model
    public CategoriaDTO categoriaDTO() {
        this.categoriaDTO = new CategoriaDTO();
        
        if ( id != null && id > 0 ){
            this.categoriaDTO = categoriaConsumer.obtenerCategoria(loginBean.getTokenUsuario(), id);
        }
        return this.categoriaDTO;
    }

    public List<CategoriaDTO> obtenerCategorias(){
        if (grupoId == null) {
            return java.util.Collections.emptyList();
        }
        
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener categorias pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        return categoriaConsumer.obtenerCategorias(token, grupoId, List.of(0,1));
    }

    public String cambiarEstado(CategoriaDTO categoria){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = categoriaConsumer.eliminarCategoria(token, categoria.getId());
        if (exito) {
            System.out.println("Estado de categoria actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Categoria " + categoria.getNombre() + " actualizado el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado de categoria.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del grupo."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "categoria.jsf?depa=" + departamentoId + "&grupo=" + grupoId + "&faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar categoria pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        CategoriaDTO categoriaDB = null;

        if ( categoriaDTO.getId() != null && categoriaDTO.getId() > 0 ){
            System.out.println("MODIFICAR CATEGORIA : " + categoriaDTO.getId());
            categoriaDB = categoriaConsumer.actualizarCategoria(token, categoriaDTO.getId(), categoriaDTO);
        } else {
            System.out.println("GRABAR NUEVA CATEGORIA");
            categoriaDB = categoriaConsumer.crearCategoria(token, categoriaDTO);
        }

        if( categoriaDB != null ){
            this.categoriaDTO = categoriaDB;
            if (categoriaDTO.getId() != null && categoriaDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Categoria " + categoriaDTO.getNombre() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Categoria " + categoriaDTO.getNombre() + " creado con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar la categoria."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/categoria.jsf?depa=" + departamentoId + "&grupo=" + grupoId + "&faces-redirect=true";
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

    public List<GrupoDTO> grupos(Integer departamento) {
        if (departamento == null) {
            return java.util.Collections.emptyList();
        }
        return publicoConsumer.obtenerGrupos(departamento);
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

    public Integer getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Integer grupoId) {
        this.grupoId = grupoId;
    }
    
    @Inject
    private GrupoConsumer grupoConsumer;

    public String nombreGrupo(Integer id){
        return grupoConsumer.obtenerGrupo(loginBean.getTokenUsuario(), id).getNombre();
    }

}
