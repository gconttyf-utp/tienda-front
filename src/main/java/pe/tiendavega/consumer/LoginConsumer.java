package pe.tiendavega.consumer;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.tiendavega.model.dto.LoginDTO;
import pe.tiendavega.model.dto.LoginJWT;

@RequestScoped
public class LoginConsumer {

    public LoginJWT loginUsuario(LoginDTO loginDTO){
        // 1. Usamos try-with-resources para que el Client se cierre automáticamente
        try ( Client client = ClientBuilder.newClient() ) {

            // 2. Apuntamos a la URL exacta
            WebTarget target = client.target(RutasConsumer.ENDPOINT_AUTH_USUARIO);

            // 3. Mapeamos los datos del DTO a un Formato "x-www-form-urlencoded"
            // La clase Form pertenece a jakarta.ws.rs.core.Form
            Form formParams = new Form()
                    .param("usuario", loginDTO.getUsuario())
                    .param("clave", loginDTO.getClave())
                    .param("grant_type", "acceso_web");
            
            // 4. Construimos la petición esperando JSON y ejecutamos el POST enviando el Formulario
            Response response = target.request(MediaType.APPLICATION_JSON)
                                      .post(Entity.form(formParams));
            
            // 5. Verificamos el código HTTP (Postman muestra 202 Accepted, pero cubrimos 200 por si acaso)
            if (response.getStatus() == Response.Status.ACCEPTED.getStatusCode() || 
                response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                // Mapeo directo a tu Record LoginJWT (no necesitas GenericType para objetos únicos)
                return response.readEntity(LoginJWT.class);                
                
            } else {
                System.out.println("Error en la autenticación. Código HTTP: " + response.getStatus());
                // Opcional: Podrías leer el mensaje de error del backend aquí usando response.readEntity(String.class)
                return null;
            }

        }// Aquí se ejecuta client.close() de forma automática
        
    }

    public LoginJWT loginCliente(LoginDTO loginDTO){
        // 1. Usamos try-with-resources para que el Client se cierre automáticamente
        try ( Client client = ClientBuilder.newClient() ) {

            // 2. Apuntamos a la URL exacta
            WebTarget target = client.target(RutasConsumer.ENDPOINT_AUTH_CLIENTE);

            // 3. Mapeamos los datos del DTO a un Formato "x-www-form-urlencoded"
            // La clase Form pertenece a jakarta.ws.rs.core.Form
            Form formParams = new Form()
                    .param("usuario", loginDTO.getUsuario())
                    .param("clave", loginDTO.getClave())
                    .param("grant_type", "acceso_web");
            
            // 4. Construimos la petición esperando JSON y ejecutamos el POST enviando el Formulario
            Response response = target.request(MediaType.APPLICATION_JSON)
                                      .post(Entity.form(formParams));
            
            // 5. Verificamos el código HTTP (Postman muestra 202 Accepted, pero cubrimos 200 por si acaso)
            if (response.getStatus() == Response.Status.ACCEPTED.getStatusCode() || 
                response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                // Mapeo directo a tu Record LoginJWT (no necesitas GenericType para objetos únicos)
                return response.readEntity(LoginJWT.class);                
                
            } else {
                System.out.println("Error en la autenticación. Código HTTP: " + response.getStatus());
                // Opcional: Podrías leer el mensaje de error del backend aquí usando response.readEntity(String.class)
                return null;
            }

        }// Aquí se ejecuta client.close() de forma automática
        
    }

}
