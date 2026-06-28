package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.AlmacenConsumer;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.consumer.TiendaConsumer;
import pe.tiendavega.model.dto.AlmacenDTO;
import pe.tiendavega.model.dto.ProductoDTO;
import pe.tiendavega.model.dto.TiendaDTO;

@Named
@ViewScoped
public class AbastecimientoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer tiendaId;
    private String codUbigeo;
    private AlmacenDTO almacenDTO;
    private String titulo;
    private String tituloAbastecimiento;

    @Inject
    private LoginBean loginBean;

    @Inject
    private AlmacenConsumer almacenConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Almacenes no Activadas ";
        this.tituloAbastecimiento = "Listado Abastecimiento de Almacenes";
    }

    /*@Produces
    @Model
    public AlmacenDTO almacenDTO() {
        this.almacenDTO = new AlmacenDTO();
        almacenDTO.setTiendaId(tiendaId);
        return this.almacenDTO;
    }*/

    public List<AlmacenDTO> obtenerAbastecimiento(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener almacenes pero no hay token en la sesión.");
            return java.util.Collections.emptyList();
        }
        return almacenConsumer.obtenerAlmacenesAcumuladas(token, this.tiendaId);
    }

    public List<TiendaDTO> obtenerAlmacenesActivadas(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener almacenes pero no hay token en la sesión.");
            return java.util.Collections.emptyList();
        }
        return almacenConsumer.obtenerTiendasAlmacenes(token, 1);
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar grupo pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        System.out.println("almacenDTO= " + almacenDTO);
        AlmacenDTO almacenDB = null;

        if ( almacenDTO.getId() != null && almacenDTO.getId() > 0 ){
            System.out.println("MODIFICAR ALMACEN : " + almacenDTO.getId());
            almacenDB = almacenConsumer.actualizarAlmacen(token, almacenDTO.getId(), almacenDTO);
        } else {
            System.out.println("GRABAR NUEVO ALMACEN");
            almacenDB = almacenConsumer.crearAlmacen(token, almacenDTO);
        }

        if( almacenDB != null ){
            this.almacenDTO = almacenDB;
            if (almacenDTO.getId() != null && almacenDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Almacen " + nombreTienda( almacenDTO.getTiendaId() ) + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Almacen " + nombreTienda( almacenDTO.getTiendaId() ) + " creado con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar el grupo."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/almacen.jsffaces-redirect=true";
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTituloAbastecimiento() {
        return tituloAbastecimiento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Integer tiendaId) {
        this.tiendaId = tiendaId;
    }

    public String getCodUbigeo() {
        return codUbigeo;
    }

    public void setCodUbigeo(String codUbigeo) {
        this.codUbigeo = codUbigeo;
    }

    @Inject
    private PublicoConsumer publicoConsumer;

    public String nombreDepartamento(String codUbigeo){
        String dato = codUbigeo.substring(0, 2) + "0000";
        return publicoConsumer.obtenerUbigeo(dato).getTxtUbigeo();
    }

    public String nombreProvincia(String codUbigeo){
        String dato = codUbigeo.substring(0, 4) + "00";
        return publicoConsumer.obtenerUbigeo(dato).getTxtUbigeo();
    }

    public String nombreDistrito(String codUbigeo){
        return publicoConsumer.obtenerUbigeo(codUbigeo).getTxtUbigeo();
    }

    public List<ProductoDTO> productos(Integer categoria) {
        if (categoria == null) {
            return java.util.Collections.emptyList();
        }
        return publicoConsumer.obtenerProductosPorCategoria(categoria);
    }

    // Método que se ejecuta al cambiar el Grupo
    public void alCambiarCategoria() {
        // 1. Limpiamos solo la variable hija directa
        //this.productoId = null;
        this.almacenDTO.setProductoId(null);
        
        // 2. Opcional: Limpiar la tabla
        // this.listaProductos = null;
        
        System.out.println("Categoria cambiado. Se limpió producto.");
    }

    @Inject
    private TiendaConsumer tiendaConsumer;

    public String nombreTienda(Integer id){
        if (id == null) {
            return "---";
        }
        return tiendaConsumer.obtenerTienda(loginBean.getTokenUsuario(), id).getNombre();
    }

    public String nombreDireccion(Integer id){
        if (id == null) {
            return "---";
        }
        return tiendaConsumer.obtenerTienda(loginBean.getTokenUsuario(), id).getDireccion();
    }

    public String codigoUbigeoTienda(Integer id){
        if (id == null) {
            return "---";
        }
        return tiendaConsumer.obtenerTienda(loginBean.getTokenUsuario(), id).getCodUbigeo();
    }

}
