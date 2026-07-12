package pe.tiendavega.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.LoginConsumer;
import pe.tiendavega.model.dto.LoginDTO;
import pe.tiendavega.model.dto.LoginJWT;

import java.io.IOException;
import java.io.Serializable;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginConsumer loginConsumer;

    private String correo;
    private String clave;
    private String mensaje;
    private String tokenUsuario;
    private String tokenCliente;

    public String autenticarInterno() {
        System.out.println("Intentando loguear: " + correo);

        LoginDTO loginDTO = new LoginDTO(correo, clave, "acceso_web");
        LoginJWT loginJWT = loginConsumer.loginUsuario(loginDTO);

        if (loginJWT != null) {
            this.tokenUsuario = loginJWT.accessToken();
            System.out.println("tokenUsuario: " + tokenUsuario);
            this.mensaje = "Login exitoso";
            return "/interno/dashboard.jsf?faces-redirect=true";
        } else {
            this.mensaje = "Usuario o clave incorrectos";
            return "/loginadmin.jsf?faces-redirect=true";
        }
    }

    public String autenticarCliente() {
        System.out.println("Intentando loguear: " + correo);

        LoginDTO loginDTO = new LoginDTO(correo, clave, "acceso_web");
        LoginJWT loginJWT = loginConsumer.loginCliente(loginDTO);

        if (loginJWT != null) {
            this.tokenCliente = loginJWT.accessToken();
            System.out.println("tokenCliente: " + tokenCliente);
            this.mensaje = "Login exitoso";
            //return "/publico/dashboard.jsf?faces-redirect=true";
            return "/index.jsf?faces-redirect=true";
        } else {
            this.mensaje = "Usuario o clave incorrectos";
            return "/login.jsf?faces-redirect=true";
        }
    }

    public String cerrarSesionUsuario() {
        System.out.println("Cerrando la Sesion del Usuario");
        // 1. Destruimos la sesión HTTP del servidor
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        tokenUsuario = null;

        // 2. Redirigimos a la página de login (o a la página pública principal)
        return "/loginadmin.jsf?faces-redirect=true";
    }

    public String cerrarSesionCliente() {
        System.out.println("Cerrando la Sesion del Cliente");
        // 1. Destruimos la sesión HTTP del servidor
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        // 2. Redirigimos a la página de login (o a la página pública principal)
        return "/login.jsf?faces-redirect=true";
    }

    public void verificarSesionUsuario() {
        try {
            // Si no hay token, significa que no está logueado o la sesión expiró/se cerró
            if (this.tokenUsuario == null || this.tokenUsuario.isEmpty()) {
                FacesContext context = FacesContext.getCurrentInstance();
                // Redirigimos al login de admin
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath() + "/loginadmin.jsf");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTokenUsuario() {
        return tokenUsuario;
    }

    public String getTokenCliente() {
        return tokenCliente;
    }

}
