package pe.tiendavega.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.tiendavega.model.dto.ClienteDTO;

@ApplicationScoped
public class ClienteConsumer {

    public List<ClienteDTO> obtenerClientes(String tokenUsuario, List<Integer> estados) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/clientes");

            if (estados != null && !estados.isEmpty()) {
                target = target.queryParam("estado", estados.toArray());
            }

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ClienteDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ClienteDTO obtenerCliente(String tokenUsuario, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/clientes/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(ClienteDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public boolean eliminarCliente(String tokenUsuario, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/clientes/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ) {
                return true;
            } else {
                System.out.println("Error en la petición DELETE: " + response.getStatus());
                return false;
            }

        }
    }

    public ClienteDTO crearCliente(String tokenUsuario, ClienteDTO clienteDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/clientes");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .post(Entity.entity(clienteDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.CREATED.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ) {
                
                return response.readEntity(ClienteDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ClienteDTO actualizarCliente(String tokenUsuario, Integer id, ClienteDTO clienteDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/clientes/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .put(Entity.entity(clienteDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ||
                response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ) {
                
                return response.readEntity(ClienteDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public Boolean nuevaClaveUsuario(String tokenUsuario, Integer id, String clave) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/clientes/newclave/{id}")
                                    .resolveTemplate("id", id);

            Map<String, String> newClave = new HashMap<>();
            newClave.put("newClave", clave);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .put(Entity.entity(newClave, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ||
                response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ) {
                
                return true;
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return false;
            }

        }
    }

}
