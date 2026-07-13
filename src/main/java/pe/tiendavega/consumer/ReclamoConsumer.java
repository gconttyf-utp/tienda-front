package pe.tiendavega.consumer;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.tiendavega.model.dto.ReclamoDTO;

@ApplicationScoped
public class ReclamoConsumer {

    public ReclamoDTO crearReclamo(String tokenCliente, ReclamoDTO reclamoDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_CLIENTE + "/reclamos");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenCliente)
                                    .post(Entity.entity(reclamoDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.CREATED.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ) {
                
                return response.readEntity(ReclamoDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ReclamoDTO actualizarReclamo(String tokenCliente, Integer id, ReclamoDTO reclamoDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/reclamos/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenCliente)
                                    .put(Entity.entity(reclamoDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ||
                response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ) {
                
                return response.readEntity(ReclamoDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ReclamoDTO obtenerReclamo(String tokenCliente, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_CLIENTE + "/reclamos/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenCliente)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(ReclamoDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ReclamoDTO> listarReclamosPorCliente(String tokenCliente, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_CLIENTE + "/reclamos/cliente/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenCliente)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ReclamoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ReclamoDTO> listarReclamosCliente(String tokenCliente) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_CLIENTE + "/reclamos/cliente");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenCliente)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ReclamoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ReclamoDTO> listarReclamos(String tokenUsuario) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/reclamos");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ReclamoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

}
