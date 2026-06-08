package pe.tiendavega.beans;

import jakarta.enterprise.inject.Model;

@Model
public class LoginBean {

    private String correo;
    private String clave;
    private String mensaje;

    public String autenticarInterno(){
        System.out.println("Intentando loguear: " + correo);

        return "/interno/dashboard.jsf?faces-redirect=true";
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

}
