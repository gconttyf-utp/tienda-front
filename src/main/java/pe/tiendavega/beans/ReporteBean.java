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

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;

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
    
    private BarChartModel barModelDiario;
    private PieChartModel pieModelSemanal;
    private LineChartModel lineModelMensual;

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
                crearGraficoDiario();
            }
        }
    }

    private void crearGraficoDiario() {
        barModelDiario = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Importe Total Diario");

        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        if (reportes != null) {
            for (ReporteDTO rep : reportes) {
                values.add(rep.getTotal());
                labels.add(rep.getPeriodo());
            }
        }

        barDataSet.setData(values);

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(54, 162, 235, 0.2)");
        barDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(54, 162, 235)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);

        data.addChartDataSet(barDataSet);
        data.setLabels(labels);
        barModelDiario.setData(data);

        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Reporte Diario de Ventas");
        options.setTitle(title);

        barModelDiario.setOptions(options);
    }

    public void cargarReporteSemanal() {
        String token = loginBean.getTokenUsuario();
        if (token != null && !token.isEmpty()) {
            List<ReporteDTO> lista = reporteConsumer.reporteSemanal(token);
            if (lista != null) {
                this.reportes = lista;
                crearGraficoSemanal();
            }
        }
    }

    private void crearGraficoSemanal() {
        pieModelSemanal = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColors = new ArrayList<>();

        String[] colores = {
            "rgb(255, 99, 132)", "rgb(54, 162, 235)", "rgb(255, 205, 86)", 
            "rgb(75, 192, 192)", "rgb(153, 102, 255)", "rgb(255, 159, 64)"
        };
        int colorIndex = 0;

        if (reportes != null) {
            for (ReporteDTO rep : reportes) {
                values.add(rep.getTotal());
                labels.add(rep.getPeriodo());
                bgColors.add(colores[colorIndex % colores.length]);
                colorIndex++;
            }
        }

        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        pieModelSemanal.setData(data);
    }

    public void cargarReporteMensual() {
        String token = loginBean.getTokenUsuario();
        if (token != null && !token.isEmpty()) {
            List<ReporteDTO> lista = reporteConsumer.reporteMensual(token);
            if (lista != null) {
                this.reportes = lista;
                crearGraficoMensual();
            }
        }
    }

    private void crearGraficoMensual() {
        lineModelMensual = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        dataSet.setLabel("Importe Total Mensual");

        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        if (reportes != null) {
            for (ReporteDTO rep : reportes) {
                values.add(rep.getTotal());
                labels.add(rep.getPeriodo());
            }
        }

        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setBorderColor("rgb(75, 192, 192)");
        dataSet.setTension(0.1);

        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        lineModelMensual.setData(data);

        LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Reporte Mensual de Ventas");
        options.setTitle(title);

        lineModelMensual.setOptions(options);
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

    public BarChartModel getBarModelDiario() {
        return barModelDiario;
    }

    public void setBarModelDiario(BarChartModel barModelDiario) {
        this.barModelDiario = barModelDiario;
    }

    public PieChartModel getPieModelSemanal() {
        return pieModelSemanal;
    }

    public void setPieModelSemanal(PieChartModel pieModelSemanal) {
        this.pieModelSemanal = pieModelSemanal;
    }

    public LineChartModel getLineModelMensual() {
        return lineModelMensual;
    }

    public void setLineModelMensual(LineChartModel lineModelMensual) {
        this.lineModelMensual = lineModelMensual;
    }

}
