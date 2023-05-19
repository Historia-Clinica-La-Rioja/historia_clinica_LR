# ![logo](apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Adjuntos

Los archivos adjuntos se terminan guardando en el [Almacenamiento](./almacenamiento.md) pero además intervienen los siguientes componentes.

En Nginx, se debe ajustar la configuración del tamaño máximo de los adjuntos utilizando la directiva `client_max_body_size` en el archivo de configuración de Nginx [nginx.conf](../../ci/docker/nginx-jar/nginx.conf). Esto te permite controlar el tamaño máximo permitido para los archivos que los clientes pueden adjuntar a través de la aplicación.

Por otro lado, en Spring Boot, se debe configurar el tamaño máximo de los adjuntos utilizando la propiedad `spring.servlet.multipart.max-file-size` en el archivo de propiedades de la aplicación. Esta propiedad te permite establecer un límite en el tamaño máximo de los archivos adjuntos que pueden ser procesados por el servlet de Spring.

Además, para HSI, la propiedad `app.files.folder.multipart.location` especifica la ubicación donde se almacenarán temporalmente los archivos adjuntos durante el proceso de carga. Por defecto se configura en [las properties de sgx-shared](../sgx-shared/src/main/resources/sgx_shared.properties).

Finalmente, habrá que ajustar cualquier otro proxy o balanceador de carga que forme parte del ambiente hasta llegar a HSI. Por ejemplo, en Kubernetes habrá que configurar la siguiente `annotation` al *Ingress*:  `nginx.ingress.kubernetes.io/proxy-body-size: "30m"`

