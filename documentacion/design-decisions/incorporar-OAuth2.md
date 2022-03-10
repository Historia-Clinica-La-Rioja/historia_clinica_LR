Incorporar OAuth2 para la autenticación en el sistema  
------------------------

### Requerimiento

Es necesario agregar la posibilidad de configurar un servidor de OAuth2 para la autenticación de usuarios en el sistema de hospitales. Esto permitiría desligar a HSI de la responsabilidad del manejo de passwords. Por otro lado, un servidor de OAuth2 permitiría implementar Single Sign-On (SSO), para tener una única autenticación que sirva para más de un sistema, es decir, un mismo usuario podría acceder a varios sistemas habiéndose logueado sólo en este servidor.

#### Parámetros a tener en cuenta

* La autorización (asignación de roles de usuario) seguirá siendo realizada en HSI.
* La autenticación debe permitir obtener datos del usuario (principalmente, username) para su mapeo a usuarios de nuestro sistema.

### Solución de diseño  

Se evaluaron dos posibilidades para desarrollar esta solución: tabla en la base de datos, properties en el sistema.

#### Base de datos

Se opta por utilizar un servidor de OAuth 2.0 que además implemente el protocolo OpenID Connect. OAuth 2.0 permite el SSO y centralizar el manejo de credenciales en un servidor separado al sistema de hospitales. 

Por otro lado, la implementación del protocolo OpenID Connect agrega a OAuth2 funciones de autenticación necesarias para poder obtener datos de los usuarios, que OAuth por sí solo no provee (ya que es un protocolo de autorización).

Aunque para desarrollo y testing se utilizará el servidor de OAuth open source Keycloak, la implementación se hará de forma agnóstica a la tecnología. 
