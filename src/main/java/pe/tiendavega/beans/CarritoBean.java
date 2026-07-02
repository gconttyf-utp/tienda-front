package pe.tiendavega.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import pe.tiendavega.consumer.ProductoConsumer;
import pe.tiendavega.consumer.PublicoConsumer;
import pe.tiendavega.model.ItemCarrito;
import pe.tiendavega.model.dto.ProductoDTO;

@Named("carritoBean")
@SessionScoped
public class CarritoBean implements Serializable{

    private static final long serialVersionUID = 2314514747321L;

    private final List<ItemCarrito> items = new ArrayList<>();

    @Inject
    private PublicoConsumer publicoConsumer;

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
}
