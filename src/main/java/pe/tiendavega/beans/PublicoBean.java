package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;

// @Named hace que esta clase sea visible desde el HTML usando "departamentoBean"
@Named
// @ViewScoped mantiene los datos vivos mientras el usuario no cambie de página
@ViewScoped
public class PublicoBean implements Serializable{

    private static final long serialVersionUID = 1564132156L;

    private PublicoConsumer publicoConsumer;

    private List<DepartamentoDTO> departamentos;

    // @PostConstruct es clave: Se ejecuta automáticamente UNA SOLA VEZ al abrir la página
    @PostConstruct
    public void init() {
        publicoConsumer = new PublicoConsumer();
        departamentos = publicoConsumer.obtenerDepartamentos();
    }

    // JSF necesita obligatoriamente el Getter para poder leer la lista
    public List<DepartamentoDTO> getDepartamentos() {
        return departamentos;
    }

    public List<GrupoDTO> getGrupos(Integer departamento) {
        return publicoConsumer.obtenerGrupos(departamento);
    }

}
