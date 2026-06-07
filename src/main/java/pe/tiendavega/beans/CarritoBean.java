package pe.tiendavega.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import pe.tiendavega.model.ItemCarrito;

@Named("carritoBean")
@SessionScoped
public class CarritoBean implements Serializable{

    private static final long serialVersionUID = 2314514747321L;

    private final List<ItemCarrito> items = new ArrayList<>();

    public void agregar(ItemCarrito item) {
        items.add(item);
    }

    public void eliminar(ItemCarrito item) {
        items.remove(item);
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

}
