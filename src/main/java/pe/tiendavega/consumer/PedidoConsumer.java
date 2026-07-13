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
import pe.tiendavega.model.dto.PedidoDTO;

@ApplicationScoped
public class PedidoConsumer {

    public PedidoDTO crearPedido(String tokenCliente, PedidoDTO pedidoDTO) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/pedidos");

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + tokenCliente)
                    .post(Entity.entity(pedidoDTO, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode() ||
                    response.getStatus() == Response.Status.CREATED.getStatusCode() ||
                    response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {

                return response.readEntity(PedidoDTO.class);

            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<PedidoDTO> pedidoClientePendientePago(String tokenCliente) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/pedidos/clientependientepago");

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + tokenCliente)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode() ||
                    response.getStatus() == Response.Status.CREATED.getStatusCode() ||
                    response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {

                return response.readEntity(new GenericType<List<PedidoDTO>>() {});

            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<PedidoDTO> pedidosCliente(String tokenCliente) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/pedidos/cliente");

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + tokenCliente)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode() ||
                    response.getStatus() == Response.Status.CREATED.getStatusCode() ||
                    response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {

                return response.readEntity(new GenericType<List<PedidoDTO>>() {});

            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public PedidoDTO obtenerPedido(String tokenCliente, Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_CLIENTE + "/pedidos/{id}")
                .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + tokenCliente)
                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode() ||
                    response.getStatus() == Response.Status.CREATED.getStatusCode() ||
                    response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {

                return response.readEntity(PedidoDTO.class);

            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public PedidoDTO actualizarPago(String tokenCliente, Integer pedidoId, Integer pagoId) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_CLIENTE + "/pedidos/actualizarpago/{id}")
                .resolveTemplate("id", pedidoId);

            target = target.queryParam("pago", pagoId);

            System.out.println("target= " + target.getUri().toString());

            Response response = target.request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + tokenCliente)
                    .put(null);

            if (response.getStatus() == Response.Status.OK.getStatusCode() ||
                    response.getStatus() == Response.Status.CREATED.getStatusCode() ||
                    response.getStatus() == Response.Status.ACCEPTED.getStatusCode()) {

                return response.readEntity(PedidoDTO.class);

            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

}
