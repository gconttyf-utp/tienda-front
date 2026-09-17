# Proyecto: Tienda Vega - Frontend (E-Commerce)

## 📌 Descripción del Proyecto
Este proyecto es el frontend de una aplicación de comercio electrónico (E-Commerce) denominada "Tienda Vega". Está diseñado para brindar una experiencia de usuario fluida, permitiendo a los clientes explorar productos, agregarlos a un carrito de compras y realizar el proceso de pago (checkout). Además, cuenta con un panel de administración interno para la gestión del catálogo, inventario, reportes, reclamos y seguimiento de despachos.

## 🚀 Arquitectura Tecnológica y Stack
El proyecto está desarrollado sobre la plataforma Java empresarial, orientado a un entorno de servidor de aplicaciones.
- **Lenguaje Core:** Java (versión 21)
- **Framework Web:** Jakarta EE 11 (JavaServer Faces - JSF)
- **Biblioteca de Componentes UI:** PrimeFaces 14 
- **Estilos / CSS:** Integración con utilidades modernas y base de Bootstrap para layouts responsivos.
- **Gestor de Dependencias y Construcción:** Apache Maven
- **Servidor de Aplicaciones Objetivo:** WildFly
- **Seguridad:** JSON Web Tokens (JJWT) para la autenticación y autorización segura con las APIs del backend.
- **Mapeo de Objetos:** MapStruct para la conversión ágil entre DTOs y modelos de vista.

## 🏗️ Patrones de Diseño 
La aplicación implementa varios patrones de diseño propios del desarrollo empresarial en Java y aplicaciones distribuidas:
1. **MVC (Model-View-Controller):**
   - **Vista:** Archivos `.xhtml` que renderizan las interfaces de usuario.
   - **Controlador:** *Managed Beans* o *Backing Beans* (en el paquete `pe.tiendavega.beans`) que manejan la lógica de presentación, eventos de la interfaz e interacción con los servicios.
   - **Modelo:** Clases que representan el estado de la aplicación y los DTOs (`pe.tiendavega.model`).
2. **API Consumer / Client Pattern:** Clases en el paquete `pe.tiendavega.consumer` que encapsulan la lógica como clientes HTTP para consumir de forma estructurada los servicios RESTful del backend.
3. **Data Transfer Object (DTO):** Patrón utilizado (`pe.tiendavega.model.dto`) para definir estructuras ligeras de datos que viajan entre el frontend y el backend, reduciendo el acoplamiento y optimizando la red.

## 📂 Estructura del Proyecto
El proyecto sigue el estándar de directorios de Maven para aplicaciones web (WAR):

```text
tienda-front/
├── pom.xml                 # Archivo de configuración de Maven (dependencias, plugins)
└── src/
    └── main/
        ├── java/
        │   └── pe/tiendavega/
        │       ├── beans/      # JSF Backing Beans (Controladores de la UI)
        │       ├── consumer/   # Clientes REST para conectarse a las APIs del backend
        │       ├── converters/ # Conversores JSF personalizados (ej. String a Objeto Complejo)
        │       └── model/      # Modelos de datos y DTOs
        └── webapp/             # Raíz de la aplicación web / Vistas
            ├── WEB-INF/        # Archivos de configuración web de Jakarta (web.xml)
            ├── resources/      # Archivos estáticos (CSS, JS, imágenes de productos y pagos)
            ├── includes/       # Plantillas parciales o fragmentos (header, footer, navbar)
            ├── interno/        # Módulo de administración (Dashboard, CRUDs, Reportes)
            └── *.xhtml         # Páginas públicas (index, carrito, checkout, catálogo, login)
```

## ⚙️ Características y Módulos Principales
- **Catálogo de Productos:** Visualización de productos organizados por tienda, marca o categoría, con un diseño moderno.
- **Carrito de Compras y Checkout:** Flujo completo de compra y registro de medios de pago.
- **Módulo de Administración (Dashboard):** Interfaces seguras para gestionar almacenes, abastecimiento, categorías, productos, marcas, clientes y roles. Incluye reportes (diarios, semanales, mensuales).
- **Gestión de Reclamaciones:** Libro de reclamaciones digital para registrar y administrar reclamos (con panel de seguimiento administrativo).
- **Gestión de Usuarios y Seguridad:** Login protegido para clientes y para administradores.
- **Diseño Adaptable (Responsive):** Interfaces ajustables a distintos tamaños de pantalla mediante componentes PrimeFaces y hojas de estilo complementarias.

## 🛠️ Cómo ejecutar el proyecto en entorno de desarrollo
1. Asegúrate de tener instalado **Java 21 (JDK 21)** y **Maven**.
2. Configura y levanta tu servidor **WildFly**.
3. En la raíz del proyecto, ejecuta el comando para compilar y empaquetar el proyecto (crear el archivo WAR):
   ```bash
   mvn clean package
   ```
4. Despliega el archivo resultante `target/tienda-front.war` en tu servidor de aplicaciones WildFly.

---
*Este proyecto es parte de mi portafolio profesional y demuestra mis capacidades para estructurar y desarrollar aplicaciones frontend empresariales sólidas, modulares y mantenibles sobre el ecosistema Jakarta EE.*
