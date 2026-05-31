package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.Banner;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;
import pe.tiendavega.model.dto.ProductoDTO;

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

    public List<ProductoDTO> ofertas() {
        return publicoConsumer.obtenerOfertas();
    }

    public List<ProductoDTO> ofertasDepartamento(Integer codigo) {
        return publicoConsumer.obtenerOfertasDepartamento(codigo);
    }

    public List<ProductoDTO> ofertasGrupo(Integer codigo) {
        return publicoConsumer.obtenerOfertasGrupo(codigo);
    }

    public List<Banner> obtenerBanners() {
        List<Banner> banners = new ArrayList<>();

        banners.add(new Banner(
            "Ahorra hasta 40% en Abarrotes",
            "Ofertas al por mayor en tus marcas favoritas. Recojo gratis en nuestras tiendas.",
            "Semana OFF",
            "Ver ofertas",
            "catalogo.xhtml?ofertas=1",
            "linear-gradient(135deg, #0D3B50 0%, #1DA1A1 100%)",
            "#FFFFFF"
        ));

        return banners;
    }

}
