package pe.tiendavega.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("flexBooleanConverter")
public class FlexBooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean getAsObject(FacesContext context, UIComponent component, String value) {
        // Qué hacer con lo que llega de la URL
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("si") || value.equalsIgnoreCase("yes");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Boolean value) {
        // Qué hacer si tenemos que imprimir el booleano en el HTML
        return (value != null && value) ? "1" : "0";
    }

}
