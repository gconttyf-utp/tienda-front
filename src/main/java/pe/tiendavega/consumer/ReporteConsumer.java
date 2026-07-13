package pe.tiendavega.consumer;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.tiendavega.model.dto.ReporteDTO;

@ApplicationScoped
public class ReporteConsumer {

    public List<ReporteDTO> reporteDiario(String tokenUsuario) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/reportes/diario");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ReporteDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ReporteDTO> reporteSemanal(String tokenUsuario) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/reportes/semanal");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ReporteDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ReporteDTO> reporteMensual(String tokenUsuario) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_USUARIO + "/reportes/mensual");

            Response response = target.request(MediaType.APPLICATION_JSON)
                                    .header("Authorization", "Bearer " + tokenUsuario)
                                    .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ReporteDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

}
