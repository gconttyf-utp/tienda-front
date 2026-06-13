package pe.tiendavega.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import pe.tiendavega.model.Beneficio;

@Named
@RequestScoped
public class GlobalBean implements Serializable{

    private static final long serialVersionUID = 1122333455L;

    @Produces
    @Model
    public String titulo(){
        return "VegaMarket";
    }
    
    @Produces
    @ApplicationScoped
    @Named("promosTop")
    public List<String> getPromosTop() {
        return List.of(
            "Recojo GRATIS en cualquiera de nuestras tiendas",
            "Hasta 40% OFF en ofertas seleccionadas",
            "Atencion al cliente de 7am a 11pm"
        );
    }

    @Produces
    @ApplicationScoped
    @Named("beneficios")
    public List<Beneficio> getBeneficios() {
        List<Beneficio> beneficios  = new ArrayList<>();

        beneficios.add(Beneficio.builder()
            .titulo("Recojo gratis")
            .descripcion("En cualquiera de nuestras tiendas.")
            .icono("truck")
            .build());

        beneficios.add(Beneficio.builder()
            .titulo("Ofertas diarias")
            .descripcion("Precios al por mayor, todos los dias.")
            .icono("tag")
            .build());

        beneficios.add(Beneficio.builder()
            .titulo("Atencion 24/7")
            .descripcion("Te respondemos en minutos.")
            .icono("support")
            .build());

        beneficios.add(Beneficio.builder()
            .titulo("Pagos protegidos")
            .descripcion("Tarjeta, Yape, Plin y mas.")
            .icono("shield")
            .build());

        return beneficios;
    }

    @Produces
    @Named("rutaImagenes")
    public String rutaImagenes() {
        return "/home/gconti/proyectos/utp/proyectos/tienda-front/src/main/webapp/resources/imagenes";
        //return "D:/PROYECTOS/utp/tienda-front/src/main/webapp/resources/imagenes";
    }

}
