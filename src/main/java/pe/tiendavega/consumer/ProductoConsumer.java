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
import pe.tiendavega.model.dto.ProductoDTO;

@ApplicationScoped
public class ProductoConsumer {

    public List<ProductoDTO> obtenerProductos(String tokenUsuario, Integer categoriaId, List<Integer> estados) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/productos/categoria");

            target = target.queryParam("codigo", categoriaId);

            if (estados != null && !estados.isEmpty()) {
                target = target.queryParam("estado", estados.toArray());
            }

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ProductoDTO obtenerProducto(String tokenUsuario, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/productos/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(ProductoDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public boolean eliminarProducto(String tokenUsuario, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/productos/{id}")
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

    public ProductoDTO crearProducto(String tokenUsuario, ProductoDTO productoDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/productos");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .post(Entity.entity(productoDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.CREATED.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ) {
                
                return response.readEntity(ProductoDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ProductoDTO actualizarProducto(String tokenUsuario, Integer id, ProductoDTO productoDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/productos/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .put(Entity.entity(productoDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() || 
                response.getStatus() == Response.Status.ACCEPTED.getStatusCode() ||
                response.getStatus() == Response.Status.NO_CONTENT.getStatusCode() ) {
                
                return response.readEntity(ProductoDTO.class);
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

}
