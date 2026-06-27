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
import pe.tiendavega.model.dto.AlmacenDTO;
import pe.tiendavega.model.dto.TiendaDTO;

@ApplicationScoped
public class AlmacenConsumer {

    public List<TiendaDTO> obtenerTiendasSinAlmacenes(String tokenUsuario, Integer activadas) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/almacenes/tiendas");

            target = target.queryParam("activadas", activadas);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<TiendaDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public AlmacenDTO crearAlmacen(String tokenUsuario, AlmacenDTO almacenDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/almacenes");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .post(Entity.entity(almacenDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.CREATED.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ) {
                
                return response.readEntity(AlmacenDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public AlmacenDTO actualizarAlmacen(String tokenUsuario, Integer id, AlmacenDTO almacenDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/almacenes/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .put(Entity.entity(almacenDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ||
                response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ) {
                
                return response.readEntity(AlmacenDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

}
