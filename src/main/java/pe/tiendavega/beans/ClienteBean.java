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
import pe.tiendavega.consumer.ClienteConsumer;
import pe.tiendavega.model.dto.ClienteDTO;

@Named
@ViewScoped
public class ClienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private ClienteDTO clienteDTO;
    private String titulo;

    @Inject
    private LoginBean loginBean;

    @Inject
    private ClienteConsumer clienteConsumer;

    @Inject
    private FacesContext facesContext;

    @PostConstruct
    public void init(){
        this.titulo = "Listado de Clientes ";
    }

    @Produces
    @Model
    public ClienteDTO clienteDTO() {
        this.clienteDTO = new ClienteDTO();
        System.out.println("ID clienteDTO= " + id);
        if ( id != null && id > 0 ){
            this.clienteDTO = clienteConsumer.obtenerCliente(loginBean.getTokenUsuario(), id);
            System.out.println("ID clienteDTO= " + clienteDTO.toString());
        }
        return this.clienteDTO;
    }

    public List<ClienteDTO> obtenerClientes(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando obtener clientes pero no hay token en la sesión.");
            // Idealmente aquí deberías redireccionar al login
            return java.util.Collections.emptyList();
        }
        return clienteConsumer.obtenerClientes(token, List.of(0,1));
    }

    public String cambiarEstado(ClienteDTO cliente){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando cambiar estado pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        
        boolean exito = clienteConsumer.eliminarCliente(token, cliente.getId());
        if (exito) {
            System.out.println("Estado de usuario actualizado exitosamente.");
            facesContext.addMessage(null, new FacesMessage("Usuario " + cliente.getApellidos() + "," + cliente.getNombres() + " actualizado el estado con exito!"));
        } else {
            System.out.println("Error al intentar cambiar el estado del usuario.");
            facesContext.addMessage(null, new FacesMessage("Error al intentar cambiar el estado del usuario."));
        }
        
        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "cliente.jsf?faces-redirect=true";
    }

    public String procesar(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar grupo pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        System.out.println("clienteDTO= " + clienteDTO);
        ClienteDTO clienteDB = null;

        if ( clienteDTO.getId() != null && clienteDTO.getId() > 0 ){
            System.out.println("MODIFICAR CLIENTE : " + clienteDTO.getId());
            clienteDB = clienteConsumer.actualizarCliente(token, clienteDTO.getId(), clienteDTO);
        } else {
            System.out.println("GRABAR NUEVO CLIENTE");
            clienteDB = clienteConsumer.crearCliente(token, clienteDTO);
        }

        if( clienteDB != null ){
            this.clienteDTO = clienteDB;
            if (clienteDTO.getId() != null && clienteDTO.getId() > 0 ) {
                facesContext.addMessage(null, new FacesMessage("Usuario " + clienteDTO.getApellidos() + "," + clienteDTO.getNombres() + " actualizado con exito!"));
            } else {
                facesContext.addMessage(null, new FacesMessage("Usuario " + clienteDTO.getApellidos() + "," + clienteDTO.getNombres() + " creado con exito!"));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al guardar el usuario."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/cliente.jsf?faces-redirect=true";
    }

    public String cambiarClave(){
        String token = loginBean.getTokenUsuario();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear o actualizar grupo pero no hay token en la sesión.");
            return "/loginadmin.jsf?faces-redirect=true";
        }
        System.out.println("clienteDTO= " + clienteDTO);
        Boolean exito = false;

        if ( clienteDTO.getId() != null && clienteDTO.getId() > 0 ){
            System.out.println("GRABAR NUEVA CLAVE DE USUARIO : " + clienteDTO.getId());
            exito = clienteConsumer.nuevaClaveUsuario(token, clienteDTO.getId(), clienteDTO.getClave());
        }

        if( exito ){
            facesContext.addMessage(null, new FacesMessage("La contraseña del usuario " + clienteDTO.getApellidos() + "," + clienteDTO.getNombres() + " fue actualizada con exito!"));
        } else {
            facesContext.addMessage(null, new FacesMessage("Error al actaulizar contraseña del usuario."));
        }

        facesContext.getExternalContext().getFlash().setKeepMessages(true);
        return "/interno/cliente.jsf?faces-redirect=true";
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
