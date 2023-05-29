# README en construcción

## sgx-reverseproxy
Este módulo se creó con la finalidad de facilitar el despliegue independiente de un componente que actúa de 'proxy inverso' entre el servidor que se busca proteger de la red, y la aplicación cliente en cuestión.
Existe un componente base encargado de realizar todas las peticiones del cliente, hacia el servidor ubicado detrás. En resumen, un proxy inverso ofrece una capa de abstracción y control entre los clientes y los servidores, mejorando la escalabilidad, el rendimiento, la seguridad y la flexibilidad de un sistema de red.

Si uno desea adaptar el comportamiento del componente, es posible hacerlo utilizando debidamente ```Spring``` y lograr hacer uso de una base de este proxy inverso, para adecuarlo a las necesidades del caso particular.

### Comentarios adicionales
El código fuente base fue extraído del módulo ```sgx-shared```, si se encuentra desarrollando en la aplicación y no necesita desplegar un componente aparte, no es necesario que importe este módulo al suyo, utilice las clases ubicadas en el módulo mencionado para tal fin.

## Propiedades de base
| Propiedad                               | Variable de ambiente | Valor por defecto | Necesidad       | Descripcion                                                                                                                                                            | Desde |
|-----------------------------------------|----------------------|-------------------|-----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|
| server.servlet.context-path             | SERVLET_CONTEXT_PATH | -                 | **Obligatorio** | Nombre del contexto para servir los recursos, o en este caso, para delegar las solicitudes. Por ejemplo para red de imágenes el valor definido es ```/imagenetwork```. | 0.0.1 |
| app.proxy.type                          | APP_PROXY_TYPE       | base              | **Obligatorio** | En caso de que el módulo soporte más de un tipo de proxy inverso, hay que definir cual instanciar. Esta propiedad identifica a las clases con ```@Configuration```.    | 0.0.1 |
| app.proxy.server                        | APP_URL              | -                 | **Obligatorio** | Dirección URL del servidor que se encuentra detrás del proxy inverso.                                                                                                  | 0.0.1 |
| app.proxy.secure                        | APP_SECURE           | false             | Opcional        | Por ahora sin uso.                                                                                                                                                     | 0.0.1 |
| app.filters.enable-cors                 | ENABLE_CORS          | true              | Opcional        | Activa la política de CORS.                                                                                                                                            | 0.0.1 |
| app.http.client.trustInvalidCertificate | -                    | false             | Opcional        | Configura el cliente REST que utiliza el proxy inverso, para confiar en certificados no válidos.                                                                       | 0.0.1 |
| app.http.client.timeout                 | HTTP_CLIENT_TIMEOUT  | 30000             | Opcional        | Configura el cliente REST que utiliza el proxy inverso, para establecer un máximo de espera de respuesta del servidor ubicado detrás.                                  | 0.0.1 |
| app.http.client.proxy                   | -                    | -                 | Opcional        | Define un servidor proxy para realizar todas las solicitudes. En general no es necesario.                                                                              | 0.0.1 |

## Casos de uso requeridos
[Image Network](documentacion/imagenetwork.md)
