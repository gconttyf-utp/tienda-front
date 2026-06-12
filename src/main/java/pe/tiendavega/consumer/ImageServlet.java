package pe.tiendavega.consumer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// Interceptará cualquier ruta que empiece con /media/
@WebServlet("/media/*")
public class ImageServlet extends HttpServlet {

    private String rutaBaseImagenes;

    @Override
    public void init() throws ServletException {
        // LEER LA RUTA DINÁMICAMENTE:
        // Buscamos una propiedad del sistema de Java. Si no existe, usamos una por defecto.
        // En Java, las barras hacia adelante (/) funcionan tanto en Windows como en Linux.
        this.rutaBaseImagenes = System.getProperty("app.ruta.imagenes", "D:/PROYECTOS/utp/tienda-front/src/main/webapp/resources/imagenes");
        
        System.out.println("====== IMAGE SERVLET INICIALIZADO ======");
        System.out.println("Ruta estática externa configurada: " + this.rutaBaseImagenes);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtiene el nombre del archivo (ejemplo: /OIP(10).webp)
        String archivoSolicitado = request.getPathInfo();

        if (archivoSolicitado == null || archivoSolicitado.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Construimos el archivo apuntando al disco duro real
        File archivoImagen = new File(this.rutaBaseImagenes, archivoSolicitado);
        System.out.println("Ruta del archivo: " + archivoImagen.getCanonicalPath());
        System.out.println("Existe: " + archivoImagen.exists());
        System.out.println("Es directorio: " + archivoImagen.isDirectory());

        // Verificamos si el archivo realmente existe en el disco duro
        if (!archivoImagen.exists() || archivoImagen.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Detectamos el tipo de contenido (MIME Type) automáticamente (png, webp, jpg)
        String contentType = getServletContext().getMimeType(archivoImagen.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Escribimos la imagen en la respuesta HTTP
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(archivoImagen.length()));
        
        // Copiamos el archivo del disco directamente al flujo de salida del navegador
        Files.copy(archivoImagen.toPath(), response.getOutputStream());
    }

}
