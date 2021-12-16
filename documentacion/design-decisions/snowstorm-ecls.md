Manejo de ECLs en snowstorm  
------------------------

### Requerimiento

Es necesario mover la responsabilidad de definir las ECLs usadas por el sistema desde el front-end al back-end. Esto se debe a que un cambio de la ecl en el front requiere un sprint o un hotfix para que el mismo actue en un sistema.  
#### Parámetros a tener en cuenta

* La respuesta de snowstorm debe comportarse igual a como se estaba comportando
* Las ecls deben ser fácil de cambiar
* Debe mantenerse simple el uso de las ECls desde el frontend.

### Solución de diseño  

Se evaluaron dos posibilidades para desarrollar esta solución: tabla en la base de datos, properties en el sistema.

#### Base de datos

Básicamente consiste en tener una tabla que contenga todas las ecls manejadas por el sistema. Esta solución nos permite en tiempo de ejecución poder ajustar el valor de una ecl sin la necesidad de redeployar el sistema. Cada entrada en la tabla tiene una clave de búsqueda que va a ser usada por el frontend para elegir la ecl deseada. A su vez requiere agregar al backoffice un menu para poder administrar las ecls.


#### Properties

Se define propiedades para cada ecl requerida. Estas propiedades son cargadas por el sistema cada vez que inicia. El cambio de una ecl requiere redeployar el sistema. A su vez se requiere agregar un enumerado que se use como clave de las ecls para que el front elija cuál usar.

#### Diseño final

Por simplicidad se decidió usar las properties. La otra opción requeria programar en el backoffice. No se descarta usar en el futuro por los beneficios que aporta.

Dejo [link](../funcionalidad/snowstorm-ecls-guiadeuso.md) a la documentación.

### Issues

No se puede cambiar las ecls en caliente. Es necesario redeployar.