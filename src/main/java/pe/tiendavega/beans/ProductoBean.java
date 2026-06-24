package pe.tiendavega.beans;

import java.io.File;
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
import jakarta.servlet.http.Part;
import pe.tiendavega.consumer.CategoriaConsumer;
import pe.tiendavega.consumer.DepartamentoConsumer;
import pe.tiendavega.consumer.GrupoConsumer;
import pe.tiendavega.consumer.ProductoConsumer;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.dto.CategoriaDTO;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;
import pe.tiendavega.model.dto.ProductoDTO;

@Named
@ViewScoped
public class ProductoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer departamentoId;
    private Integer grupoId;
    private Integer categoriaId;
    private ProductoDTO productoDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private ProductoConsumer productoConsumer;

    @Inject
    private FacesContext facesContext; 

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Productos";
    }

    @Produces
    @Model
    public ProductoDTO productoDTO() {
        this.productoDTO = new ProductoDTO();
        
        if ( id != null && id > 0 ){
            this.productoDTO = productoConsumer.obtenerProducto(loginBean.getTokenUsuario(), id);
        }
        return this.productoDTO;
    }

    public List<ProductoDTO> obtenerProductos(){
        if (categoriaId == null) {
            return java.util.Collections.emptyList();
        }
        
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener productos pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        return productoConsumer.obtenerProductos(token, categoriaId, List.of(0,1));
    }

    public String cambiarEstado(ProductoDTO producto){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = productoConsumer.eliminarProducto(token, producto.getId());
        if (exito) {
            System.out.println("Estado de producto actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Producto " + producto.getDescripcion() + " actualizado el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado de producto.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del producto."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "producto.jsf?depa=" + departamentoId + "&grupo=" + grupoId + "&cate=" + categoriaId + "&faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar producto pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        try{
            // 1. Obtener la ruta estática externa configurada en el sistema
            String rutaBase = directorioDestino + "/productos";
            File directorio = new File(rutaBase);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crea las carpetas físicas si no existen en Windows o Linux
            }
            System.out.println("La ruta absoluta del directorio es: " + directorio.getAbsolutePath());

            ProductoDTO productoDB = null;

            // CASO A: EL REGISTRO YA EXISTE (MODIFICAR)
            if ( productoDTO.getId() != null && productoDTO.getId() > 0 ){

                // Si el usuario seleccionó una nueva imagen, la procesamos
                if (archivoImagen != null && archivoImagen.getSize() > 0) {
                    String extension = obtenerExtension(archivoImagen.getSubmittedFileName());
                    String nuevoNombreImagen = "producto_" + productoDTO.getId() + extension;

                    // Guardado físico en el disco duro
                    File destino = new File(directorio, nuevoNombreImagen);
                    archivoImagen.write(destino.getAbsolutePath());
                    
                    // Asignamos el nombre final al DTO antes de enviarlo al backend
                    productoDTO.setRutaImg(nuevoNombreImagen);
                    System.out.println("El nombre final de la imagen es: " + nuevoNombreImagen);
                    System.out.println("El destino final de la imagen es: " + destino.getAbsolutePath());
                    System.out.println("El archivo final de la imagen es: " + archivoImagen.getName());
                    System.out.println("El nombre final en productoDTO.setRutaImg() ahora es: " + productoDTO.getRutaImg());
                }
                
                System.out.println("MODIFICAR PRODUCTO : " + productoDTO.getId());
                productoDB = productoConsumer.actualizarProducto(token, productoDTO.getId(), productoDTO);
            }
            // CASO B: EL REGISTRO ES NUEVO (CREAR) 
            else {
                System.out.println("GRABAR NUEVO PRODUCTO");                

                // Si viene con imagen, le ponemos un nombre temporal inicial
                String extensionTemporal = "";
                if (archivoImagen != null && archivoImagen.getSize() > 0) {
                    extensionTemporal = obtenerExtension(archivoImagen.getSubmittedFileName());
                    productoDTO.setRutaImg("temp" + extensionTemporal);
                }

                // Guardamos en la base de datos para OBTENER EL ID autogenerado
                productoDB = productoConsumer.crearProducto(token, productoDTO);

                // Si la creación fue exitosa y venía un archivo, procedemos al renombrado real
                if (productoDB != null && productoDB.getId() != null && archivoImagen != null && archivoImagen.getSize() > 0) {
                    String nuevoNombreImagen = "producto_" + productoDB.getId() + extensionTemporal;
                    
                    // Guardado físico con el ID real
                    File destino = new File(directorio, nuevoNombreImagen);
                    archivoImagen.write(destino.getAbsolutePath());
                    
                    // Actualizamos el objeto en la base de datos con su nombre de imagen definitivo
                    productoDTO.setRutaImg(nuevoNombreImagen);
                    productoDB = productoConsumer.actualizarProducto(token, productoDB.getId(), productoDTO);
                }
            }

            if( productoDB != null ){
                this.productoDTO = productoDB;
                if (productoDTO.getId() != null && productoDTO.getId() > 0 ) {
                    facesContext.addMessage(null, new FacesMessage("Producto " + productoDTO.getDescripcion() + " actualizado con exito!"));
                } else {
                    facesContext.addMessage(null, new FacesMessage("Producto " + productoDTO.getDescripcion() + " creado con exito!"));
                }
            } else {
                facesContext.addMessage(null, new FacesMessage("Error al guardar el producto."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage("Error crítico al procesar el archivo: " + e.getMessage()));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/producto.jsf?depa=" + departamentoId + "&grupo=" + grupoId + "&cate=" + productoDTO.getCategoriaID() + "&faces-redirect=true";
    }

    // Método que se ejecuta al cambiar el Departamento
    public void alCambiarDepartamento() {
        // 1. Limpiamos las variables hijas
        this.grupoId = null;
        this.categoriaId = null;
        
        // 2. Opcional: Si manejas la lista de la tabla en una variable global, límpiala aquí
        // this.listaProductos = null; o this.listaProductos.clear();
        
        System.out.println("Departamento cambiado. Se limpiaron grupo y categoría.");
    }

    // Método que se ejecuta al cambiar el Grupo
    public void alCambiarGrupo() {
        // 1. Limpiamos solo la variable hija directa
        this.categoriaId = null;
        
        // 2. Opcional: Limpiar la tabla
        // this.listaProductos = null;
        
        System.out.println("Grupo cambiado. Se limpió categoría.");
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

    public List<CategoriaDTO> categorias(Integer grupo) {
        if (grupo == null) {
            return java.util.Collections.emptyList();
        }
        return publicoConsumer.obtenerCategorias(grupo);
    }

    @Inject
    private DepartamentoConsumer departamentoConsumer;

    public String nombreDepartamento(Integer id){
        return departamentoConsumer.obtenerDepartamento(loginBean.getTokenUsuario(), id).getNombre();
    }

    @Inject
    private GrupoConsumer grupoConsumer;

    public String nombreGrupo(Integer id){
        return grupoConsumer.obtenerGrupo(loginBean.getTokenUsuario(), id).getNombre();
    }

    @Inject
    private CategoriaConsumer categoriaConsumer;

    public String nombreCategoria(Integer id){
        return categoriaConsumer.obtenerCategoria(loginBean.getTokenUsuario(), id).getNombre();
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

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    // CDI busca el @Produces que tenga este nombre exacto
    @Inject
    @Named("rutaImagenes")
    private String directorioDestino;

    private Part archivoImagen; // Aquí JSF inyectará el archivo binario seleccionado

    // Getters y Setters obligatorios
    public Part getArchivoImagen() {
        return archivoImagen;
    }

    public void setArchivoImagen(Part archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

    // Método utilitario para extraer la extensión (.jpg, .png, .webp, etc.)
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return ".webp"; // Por defecto si no se detecta
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
    }

}
