package pe.tiendavega.consumer;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.tiendavega.model.dto.CategoriaDTO;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;
import pe.tiendavega.model.dto.MarcaDTO;
import pe.tiendavega.model.dto.ProductoDTO;
import pe.tiendavega.model.dto.TiendaDTO;
import pe.tiendavega.model.dto.UbigeoDTO;

@ApplicationScoped
public class PublicoConsumer {

    public List<DepartamentoDTO> obtenerDepartamentos() {
        // 1. Usamos try-with-resources para que el Client se cierre automáticamente
        try (Client client = ClientBuilder.newClient()) {

            // 2. Apuntamos a la URL exacta
            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/departamentos");

            // 3 y 4. Construimos la petición esperando JSON y ejecutamos el GET
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            // Verificamos que el código HTTP sea 200 (OK)
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                // Magia de JAX-RS: Convierte el JSON recibido directamente a tu List<DepartamentoDTO>
                // Usamos GenericType porque es una lista (para objetos simples se usa .class directo)
                return response.readEntity(new GenericType<List<DepartamentoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }// Aquí se ejecuta client.close() de forma automática
    }

    public List<GrupoDTO> obtenerGrupos(Integer departamento) {
        // 1. Usamos try-with-resources para que el Client se cierre automáticamente
        try (Client client = ClientBuilder.newClient()) {

            // 2. Apuntamos a la URL exacta
            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/grupos")
                                    .queryParam("depa", departamento);

            // 3 y 4. Construimos la petición esperando JSON y ejecutamos el GET
            Response response = target.request(MediaType.APPLICATION_JSON).get();

            // Verificamos que el código HTTP sea 200 (OK)
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                // Magia de JAX-RS: Convierte el JSON recibido directamente a tu List<DepartamentoDTO>
                // Usamos GenericType porque es una lista (para objetos simples se usa .class directo)
                return response.readEntity(new GenericType<List<GrupoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }// Aquí se ejecuta client.close() de forma automática
    }

    public List<CategoriaDTO> obtenerCategorias(Integer grupo) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/categorias")
                                    .queryParam("grupo", grupo);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<CategoriaDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<MarcaDTO> obtenerMarcas() {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/marcas");

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<MarcaDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ProductoDTO> obtenerProductos(Integer departamento, Integer grupo, Integer categoria, Integer marca) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/productos")
                                    .queryParam("depa", departamento)
                                    .queryParam("grupo", grupo)
                                    .queryParam("categoria", categoria)
                                    .queryParam("marca", marca);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ProductoDTO> obtenerProductosPorCategoria(Integer categoria) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/productos/categoria")
                                    .queryParam("codigo", categoria);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ProductoDTO> obtenerProductosPorMarca(Integer marca) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/productos/marca")
                                    .queryParam("codigo", marca);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public ProductoDTO obtenerProducto(Integer id) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/producto/{id}")
                                    .resolveTemplate("id", id);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<ProductoDTO>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<TiendaDTO> obtenerTiendas() {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/tiendas");

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<TiendaDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<TiendaDTO> obtenerTiendasPorUbigeo(Integer ubigeo) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/tiendasubigeo")
                                    .queryParam("ubigeo", ubigeo);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<TiendaDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public UbigeoDTO obtenerUbigeo(String codUbigeo) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_UBIGEO + "/ubigeo/{codUbigeo}")
                                    .resolveTemplate("codUbigeo", codUbigeo);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<UbigeoDTO>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<UbigeoDTO> obtenerUbigeoDepartamento() {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_UBIGEO + "/ubigeo/departamentos");

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<UbigeoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ProductoDTO> obtenerOfertas() {
        System.out.println("Obtener ofertas sin filtros");
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/ofertas");

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ProductoDTO> obtenerOfertasDepartamento(Integer codigo) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/ofertadepartamento")
                                    .queryParam("codigo", codigo);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }

    public List<ProductoDTO> obtenerOfertasGrupo(Integer codigo) {
        try (Client client = ClientBuilder.newClient()) {

            WebTarget target = client.target(RutasConsumer.ENDPOINT_PUBLICO + "/ofertagrupos")
                                    .queryParam("codigo", codigo)
                                    .queryParam("oferta", 1)
                                    .queryParam("oferta", 0);

            Response response = target.request(MediaType.APPLICATION_JSON).get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                
                return response.readEntity(new GenericType<List<ProductoDTO>>() {});
                
            } else {
                System.out.println("Error en la petición: " + response.getStatus());
                return null;
            }

        }
    }
    
}
