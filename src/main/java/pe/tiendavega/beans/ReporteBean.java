package pe.tiendavega.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.ReporteConsumer;
import pe.tiendavega.model.dto.ReporteDTO;

@Named("reporteBean")
@ViewScoped
public class ReporteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ReporteConsumer reporteConsumer;

    @Inject
    private LoginBean loginBean;

    private String fechaFiltro;

    private List<ReporteDTO> reportes;

    @PostConstruct
    public void init() {
        reportes = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fechaFiltro = sdf.format(new Date());
    }

    public void cargarReporteDiario() {
        String token = loginBean.getTokenUsuario();
        if (token != null && !token.isEmpty()) {
            List<ReporteDTO> lista = reporteConsumer.reporteDiario(token);
            if (lista != null) {
                this.reportes = lista;
            }
        }
    }

    public void cargarReporteSemanal() {
        String token = loginBean.getTokenUsuario();
        if (token != null && !token.isEmpty()) {
            List<ReporteDTO> lista = reporteConsumer.reporteSemanal(token);
            if (lista != null) {
                this.reportes = lista;
            }
        }
    }

    public void cargarReporteMensual() {
        String token = loginBean.getTokenUsuario();
        if (token != null && !token.isEmpty()) {
            List<ReporteDTO> lista = reporteConsumer.reporteMensual(token);
            if (lista != null) {
                this.reportes = lista;
            }
        }
    }

    public String getFechaFiltro() {
        return fechaFiltro;
    }

    public void setFechaFiltro(String fechaFiltro) {
        this.fechaFiltro = fechaFiltro;
    }

    public List<ReporteDTO> getReportes() {
        return reportes;
    }

    public void setReportes(List<ReporteDTO> reportes) {
        this.reportes = reportes;
    }

}
