package pe.tiendavega.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.PedidoConsumer;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.ItemCarrito;
import pe.tiendavega.model.dto.PedidoDTO;
import pe.tiendavega.model.dto.PedidoDetalleDTO;
import pe.tiendavega.model.dto.ProductoDTO;

@Named("carritoBean")
@SessionScoped
public class CarritoBean implements Serializable{

    private static final long serialVersionUID = 2314514747321L;

    private final List<ItemCarrito> items = new ArrayList<>();

    @Inject
    private PublicoConsumer publicoConsumer;

    @Inject
    private PedidoConsumer pedidoConsumer;

    @Inject
    private LoginBean loginBean;

    @Inject
    private DeliveryBean deliveryBean;

    public void agregar(Integer idProducto, Integer cantidad) {
        System.out.println("Ingresando a CarritoBean Agregar");
        System.out.println("Producto ID: " + idProducto);
        System.out.println("Cantidad: " + cantidad);
        ProductoDTO producto = publicoConsumer.obtenerProducto(idProducto);

        if (producto == null || cantidad == null || cantidad == 0) {
            return;
        }

        ItemCarrito itemExistente = items.stream()
            .filter(item -> item.getProducto().getId().equals(idProducto))
            .findFirst()
            .orElse(null);

        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
            System.out.println("Cantidad actualizada para el producto: " + idProducto + " - " + producto.getDescripcion() + " - Nueva cantidad: " + itemExistente.getCantidad());
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(producto, cantidad);
            items.add(nuevoItem);
            System.out.println("Nuevo producto agregado al carrito: " + idProducto + " - " + producto.getDescripcion() + " - Nueva cantidad: " + nuevoItem.getCantidad());
        }
    }

    public void eliminar(Integer idProducto) {
        items.removeIf(item -> item.getProducto().getId().equals(idProducto));
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return items.size();
    }

    public String actualizarCantidad(Integer idProducto, Integer nuevaCantidad) {
        System.out.println("Ingresando a CarritoBean actualizarCantidad");
        System.out.println("Producto ID: " + idProducto);
        System.out.println("Nueva cantidad: " + nuevaCantidad);

        if (nuevaCantidad == null || nuevaCantidad <= 0) {
            // Si es cero o nulo, eliminamos el producto
            eliminar(idProducto);
            System.out.println("Cantidad 0 o nula, eliminando producto: " + idProducto);
        } else {
            ItemCarrito item = items.stream()
                    .filter(i -> i.getProducto().getId().equals(idProducto))
                    .findFirst()
                    .orElse(null);

            if (item != null) {
                item.setCantidad(nuevaCantidad);
                System.out.println("Cantidad actualizada para el producto: " + idProducto + " - Nueva cantidad: " + nuevaCantidad);
            } else {
                // Si no se encuentra el item, lo agregamos
                agregar(idProducto, nuevaCantidad);
                System.out.println("Producto no encontrado, agregando producto: " + idProducto + " - Cantidad: " + nuevaCantidad);
            }
        }

        // Regresamos a la misma página (carrito.xhtml)
        return null;
    }

    public String agregarPedido(){
        String token = loginBean.getTokenCliente();
        if (token == null || token.isEmpty()) {
            System.out.println("¡ADVERTENCIA! Intentando crear pedido pero no hay token en la sesión.");
            return "/login.jsf?faces-redirect=true";
        }
        
        System.out.println("Ingresando a CarritoBean agregarPedido");
        System.out.println("Cantidad total: " + getTotal());
        System.out.println("Total de items: " + getTotalItems());
        items.forEach(item -> {
            System.out.println("Producto: " + item);
        });

        List<PedidoDetalleDTO> detalleDTOs = new ArrayList<>();
        
        items.forEach(item -> {
            PedidoDetalleDTO detalleDTO = new PedidoDetalleDTO();
            detalleDTO.setProductoID(item.getProducto().getId());
            detalleDTO.setCantidad(item.getCantidad());
            BigDecimal subtotal = item.getProducto().getPrecioOnline().multiply(BigDecimal.valueOf(item.getCantidad()));
            detalleDTO.setSubtotal(subtotal);
            detalleDTOs.add(detalleDTO);
        });

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setTiendaID( deliveryBean.getTiendaId() );
        pedidoDTO.setTotal( getTotal() );
        pedidoDTO.setDetalles( detalleDTOs );

        PedidoDTO pedidoDB = pedidoConsumer.crearPedido(token, pedidoDTO);

        if (pedidoDB != null) {
            System.out.println("Pedido creado exitosamente: " + pedidoDB.getId());
            items.clear();
            return "checkout.jsf?idPedido=" + pedidoDB.getId() + "&faces-redirect=true";
        }
        
        return "carrito.jsf?faces-redirect=true";
    }
}
