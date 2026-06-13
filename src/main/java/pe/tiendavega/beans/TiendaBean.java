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
import jakarta.servlet.http.Part;
import java.io.File;

@Named
@ViewScoped
public class TiendaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private TiendaDTO tiendaDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private TiendaConsumer tiendaConsumer;

    @Inject
    private FacesContext facesContext;

    // CDI busca el @Produces que tenga este nombre exacto
    @Inject
    @Named("rutaImagenes")
    private String directorioDestino;

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
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! No hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }

        try{
            // 1. Obtener la ruta estática externa configurada en el sistema
            //String rutaBase = System.getProperty("app.ruta.imagenes", "D:/PROYECTOS/utp/tienda-front/src/main/webapp/resources/imagenes") + "/tiendas";
            String rutaBase = directorioDestino + "/tiendas";
            File directorio = new File(rutaBase);
            if (!directorio.exists()) {
                directorio.mkdirs(); // Crea las carpetas físicas si no existen en Windows o Linux
            }
            System.out.println("La ruta absoluta del directorio es: " + directorio.getAbsolutePath());

            TiendaDTO tiendaDB = null;

            // CASO A: EL REGISTRO YA EXISTE (MODIFICAR)
            if (tiendaDTO.getId() != null && tiendaDTO.getId() > 0) {
                
                // Si el usuario seleccionó una nueva imagen, la procesamos
                if (archivoImagen != null && archivoImagen.getSize() > 0) {
                    String extension = obtenerExtension(archivoImagen.getSubmittedFileName());
                    String nuevoNombreImagen = "tienda_" + tiendaDTO.getId() + extension;
                    
                    // Guardado físico en el disco duro
                    File destino = new File(directorio, nuevoNombreImagen);
                    archivoImagen.write(destino.getAbsolutePath());
                    
                    // Asignamos el nombre final al DTO antes de enviarlo al backend
                    tiendaDTO.setImagenUrl(nuevoNombreImagen);
                    System.out.println("El nombre final de la imagen es: " + nuevoNombreImagen);
                    System.out.println("El destino final de la imagen es: " + destino.getAbsolutePath());
                    System.out.println("El archivo final de la imagen es: " + archivoImagen.getName());
                    System.out.println("El nombre final en tiendaDTO.setImagenUrl() ahora es: " + tiendaDTO.getImagenUrl());
                }
                
                System.out.println("MODIFICAR TIENDA: " + tiendaDTO.getId());
                tiendaDB = tiendaConsumer.actualizarTienda(token, tiendaDTO.getId(), tiendaDTO);

            // CASO B: EL REGISTRO ES NUEVO (CREAR)
            } else {
                System.out.println("GRABAR NUEVA TIENDA");
            
                // Si viene con imagen, le ponemos un nombre temporal inicial
                String extensionTemporal = "";
                if (archivoImagen != null && archivoImagen.getSize() > 0) {
                    extensionTemporal = obtenerExtension(archivoImagen.getSubmittedFileName());
                    tiendaDTO.setImagenUrl("temp" + extensionTemporal);
                }

                // Guardamos en la base de datos para OBTENER EL ID autogenerado
                tiendaDB = tiendaConsumer.crearTienda(token, tiendaDTO);

                // Si la creación fue exitosa y venía un archivo, procedemos al renombrado real
                if (tiendaDB != null && tiendaDB.getId() != null && archivoImagen != null && archivoImagen.getSize() > 0) {
                    String nuevoNombreImagen = "tienda_" + tiendaDB.getId() + extensionTemporal;
                    
                    // Guardado físico con el ID real
                    File destino = new File(directorio, nuevoNombreImagen);
                    archivoImagen.write(destino.getAbsolutePath());
                    
                    // Actualizamos el objeto en la base de datos con su nombre de imagen definitivo
                    tiendaDB.setImagenUrl(nuevoNombreImagen);
                    tiendaDB = tiendaConsumer.actualizarTienda(token, tiendaDB.getId(), tiendaDB);
                }
            }

            if (tiendaDB != null) {
                this.tiendaDTO = tiendaDB;
                facesContext.addMessage(null, new FacesMessage("Tienda guardada con éxito."));
            } else {
                facesContext.addMessage(null, new FacesMessage("Error al guardar la tienda."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            facesContext.addMessage(null, new FacesMessage("Error crítico al procesar el archivo: " + e.getMessage()));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/tiendas.jsf?faces-redirect=true";
    }

    // Método utilitario para extraer la extensión (.jpg, .png, .webp, etc.)
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return ".webp"; // Por defecto si no se detecta
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf("."));
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

    private Part archivoImagen; // Aquí JSF inyectará el archivo binario seleccionado

    // Getters y Setters obligatorios
    public Part getArchivoImagen() {
        return archivoImagen;
    }

    public void setArchivoImagen(Part archivoImagen) {
        this.archivoImagen = archivoImagen;
    }

}
