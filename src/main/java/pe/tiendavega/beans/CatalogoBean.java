package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.dto.CategoriaDTO;
import pe.tiendavega.model.dto.DepartamentoDTO;
import pe.tiendavega.model.dto.GrupoDTO;
import pe.tiendavega.model.dto.MarcaDTO;
import pe.tiendavega.model.dto.ProductoDTO;

@Named
@ViewScoped
public class CatalogoBean implements Serializable{

    private static final long serialVersionUID = 456789L;

    @Inject
    private PublicoConsumer publicoConsumer;

    // Parámetros de URL
    private Integer departamentoSeleccionado;
    private Integer grupoSeleccionado;
    private Integer categoriaSeleccionada;
    private Integer marcaSeleccionada;
    private Double precioMin;
    private Double precioMax;
    private String nombreDepartamento;
    private String nombreGrupo;
    private String nombreCategoria;
    private Boolean soloOfertas;
    private String textoBusqueda;

    // Catálogo
    private List<ProductoDTO> productos;

    // Filtros disponibles
    private List<DepartamentoDTO> departamentos;
    private List<GrupoDTO> gruposDelDepartamento;
    private List<CategoriaDTO> categoriasDelGrupo;
    private List<MarcaDTO> marcasDisponibles;

    /*@PostConstruct
    public void init() {
        publicoConsumer = new PublicoConsumer();
        //cargarFiltros();
        //cargarProductos();
    }*/

    public void cargarFiltros() {
        System.out.println("Departamento: " + departamentoSeleccionado);
        System.out.println("Grupo: " + grupoSeleccionado);
        System.out.println("Categoria: " + categoriaSeleccionada);
        System.out.println("Marca: " + marcaSeleccionada);
        System.out.println("Precio Min: " + precioMin);
        System.out.println("Precio Max: " + precioMax);
        System.out.println("Nombre Departamento: " + nombreDepartamento);
        System.out.println("Nombre Grupo: " + nombreGrupo);
        System.out.println("Nombre Categoria: " + nombreCategoria);
        System.out.println("Solo Ofertas: " + soloOfertas);

        departamentos = publicoConsumer.obtenerDepartamentos(); 
        
        if (departamentoSeleccionado != null && departamentoSeleccionado > 0) {

            nombreDepartamento = departamentos.stream()
            .filter(d -> d.getId().equals(departamentoSeleccionado))
            .findFirst()
            .map(DepartamentoDTO::getNombre)
            .orElse(null);

            gruposDelDepartamento = publicoConsumer.obtenerGrupos(departamentoSeleccionado);
            if (grupoSeleccionado != null && grupoSeleccionado > 0) {

                nombreGrupo = gruposDelDepartamento.stream()
                .filter(g -> g.getId().equals(grupoSeleccionado))
                .findFirst()
                .map(GrupoDTO::getNombre)
                .orElse(null);

                categoriasDelGrupo = publicoConsumer.obtenerCategorias(grupoSeleccionado);
                if (categoriaSeleccionada != null && categoriaSeleccionada > 0) {
                    nombreCategoria = categoriasDelGrupo.stream()
                    .filter(c -> c.getId().equals(categoriaSeleccionada))
                    .findFirst()
                    .map(CategoriaDTO::getNombre)
                    .orElse(null);
                }
            }
        } else {
            soloOfertas = Boolean.TRUE;
        }

        marcasDisponibles = publicoConsumer.obtenerMarcas();

        cargarProductos();
    }

    private void cargarProductos() {
        System.out.println("Cargar Productos");
        // Filtros por defecto si no vienen en la URL (ej: ofertas)
        boolean tieneFiltroDepartamento = departamentoSeleccionado != null && departamentoSeleccionado > 0;
        boolean tieneFiltroGrupo = grupoSeleccionado != null && grupoSeleccionado > 0;
        boolean tieneFiltroCategoria = categoriaSeleccionada != null && categoriaSeleccionada > 0;
        boolean tieneFiltroMarca = marcaSeleccionada != null && marcaSeleccionada > 0;
        //boolean tieneFiltroPrecio = precioMin != null || precioMax != null;
        
        System.out.println("Departamento: " + departamentoSeleccionado);
        System.out.println("Grupo: " + grupoSeleccionado);
        System.out.println("Categoria: " + categoriaSeleccionada);
        System.out.println("Marca: " + marcaSeleccionada);

        if (tieneFiltroDepartamento && tieneFiltroGrupo && tieneFiltroCategoria && tieneFiltroMarca) {
            productos = publicoConsumer.obtenerProductos( departamentoSeleccionado, grupoSeleccionado, categoriaSeleccionada, marcaSeleccionada );
        } else if (tieneFiltroDepartamento && tieneFiltroGrupo && tieneFiltroCategoria && !tieneFiltroMarca) {
            productos = publicoConsumer.obtenerProductos( departamentoSeleccionado, grupoSeleccionado, categoriaSeleccionada, 0 );
        } else if (tieneFiltroDepartamento && tieneFiltroGrupo && !tieneFiltroCategoria && !tieneFiltroMarca) {
            productos = publicoConsumer.obtenerProductos( departamentoSeleccionado, grupoSeleccionado, 0, 0 );
        } else if (tieneFiltroDepartamento && !tieneFiltroGrupo && !tieneFiltroCategoria && !tieneFiltroMarca) {
            productos = publicoConsumer.obtenerProductos( departamentoSeleccionado, 0, 0, 0 );
        } else {
            // Si no hay filtros activos, mostramos ofertas
            productos = publicoConsumer.obtenerOfertas();
        }
    }

    public void aplicarFiltros() {
        cargarProductos();
    }

    public void limpiarFiltros() {
        departamentoSeleccionado = null;
        grupoSeleccionado = null;
        categoriaSeleccionada = null;
        marcaSeleccionada = null;
        precioMin = null;
        precioMax = null;
        cargarProductos();
    }

    public List<DepartamentoDTO> getDepartamentos() {
        return departamentos;
    }

    public List<GrupoDTO> getGruposDelDepartamento() {
        return gruposDelDepartamento != null ? gruposDelDepartamento : new ArrayList<>();
    }

    public List<CategoriaDTO> getCategoriasDelGrupo() {
        return categoriasDelGrupo != null ? categoriasDelGrupo : new ArrayList<>();
    }

    public List<MarcaDTO> getMarcasDisponibles() {
        return marcasDisponibles != null ? marcasDisponibles : new ArrayList<>();
    }

    public List<ProductoDTO> getProductos() {
        return productos != null ? productos : new ArrayList<>();
    }

    public Integer getDepartamentoSeleccionado() {
        return departamentoSeleccionado;
    }

    public void setDepartamentoSeleccionado(Integer departamentoSeleccionado) {
        this.departamentoSeleccionado = departamentoSeleccionado;
    }

    public Integer getGrupoSeleccionado() {
        return grupoSeleccionado;
    }

    public void setGrupoSeleccionado(Integer grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public Integer getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    public void setCategoriaSeleccionada(Integer categoriaSeleccionada) {
        this.categoriaSeleccionada = categoriaSeleccionada;
    }

    public Integer getMarcaSeleccionada() {
        return marcaSeleccionada;
    }

    public void setMarcaSeleccionada(Integer marcaSeleccionada) {
        this.marcaSeleccionada = marcaSeleccionada;
    }

    public Double getPrecioMin() {
        return precioMin;
    }

    public void setPrecioMin(Double precioMin) {
        this.precioMin = precioMin;
    }

    public Double getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(Double precioMax) {
        this.precioMax = precioMax;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public Boolean getSoloOfertas() {
        return soloOfertas;
    }

    public void setSoloOfertas(Boolean soloOfertas) {
        this.soloOfertas = soloOfertas;
    }

    public String getTextoBusqueda() {
        return textoBusqueda;
    }

    public void setTextoBusqueda(String textoBusqueda) {
        this.textoBusqueda = textoBusqueda;
    }

}
