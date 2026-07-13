package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PedidoConsumer;
import pe.tiendavega.consumer.TiendaConsumer;
import pe.tiendavega.model.dto.PedidoDTO;

@Named("pedidoBean")
@ViewScoped
public class PedidoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PedidoConsumer pedidoConsumer;

    @Inject
    private LoginBean loginBean;

    private List<PedidoDTO> pendientesPago;
    private List<PedidoDTO> listadoSeguimiento;

    private Integer pedidoId;

    private PedidoDTO pedidoDTO;

    @PostConstruct
    public void init() {
        pendientesPago = new ArrayList<>();
        listadoSeguimiento = new ArrayList<>();
        String token = loginBean.getTokenCliente();
        if (token != null && !token.isEmpty()) {
            try {
                // El consumidor actualmente retorna un solo PedidoDTO
                List<PedidoDTO> pedido = pedidoConsumer.pedidoClientePendientePago(token);
                if (pedido != null) {
                    pendientesPago = pedido;
                }
                
                List<PedidoDTO> seguimiento = pedidoConsumer.pedidosCliente(token);
                if (seguimiento != null) {
                    listadoSeguimiento = seguimiento;
                }
            } catch (Exception e) {
                System.out.println("Error al obtener pedidos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Produces
    @Model
    public PedidoDTO pedidoActual() {
        this.pedidoDTO = new PedidoDTO();
        
        if ( pedidoId != null && pedidoId > 0 ){
            this.pedidoDTO = pedidoConsumer.obtenerPedido(loginBean.getTokenCliente(), pedidoId);
        }
        return this.pedidoDTO;
    }

    public List<PedidoDTO> getListadoSeguimiento() {
        return listadoSeguimiento;
    }

    public void setListadoSeguimiento(List<PedidoDTO> listadoSeguimiento) {
        this.listadoSeguimiento = listadoSeguimiento;
    }

    public List<PedidoDTO> getPendientesPago() {
        return pendientesPago;
    }

    public void setPendientesPago(List<PedidoDTO> pendientesPago) {
        this.pendientesPago = pendientesPago;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    @Inject
    private TiendaConsumer tiendaConsumer;

    public String obtenerNombreTienda(int id) {
        return tiendaConsumer.obtenerTienda(loginBean.getTokenCliente(), id).getNombre();
    }

    public String procesarPago(){
        if (this.pedidoDTO != null) {
            System.out.println("Procesando pago...");
            System.out.println("ID Pedido: " + this.pedidoDTO.getId());
            System.out.println("Tipo de Pago: " + this.pedidoDTO.getTipoPago());
            PedidoDTO pedidoActualizado = pedidoConsumer.actualizarPago(loginBean.getTokenCliente(), this.pedidoDTO.getId(), this.pedidoDTO.getTipoPago());
            if (pedidoActualizado != null) {
                this.pedidoDTO = new PedidoDTO();
                init();
                return "seguimiento.jsf?faces-redirect=true";
            }
        }
        return "checkout.jsf?idPedido=" + this.pedidoDTO.getId() + "&faces-redirect=true";
    }
    
}
