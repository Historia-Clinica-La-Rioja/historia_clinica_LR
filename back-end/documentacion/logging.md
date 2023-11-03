# ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Back-end | Logging

En la configuración de registros, existe un equilibrio delicado entre registrar demasiada información y no registrar lo suficiente. Registrar en exceso puede generar un ruido que dificulta la identificación de problemas reales, mientras que no registrar lo suficiente puede dificultar el diagnóstico de problemas y la comprensión del comportamiento de la aplicación. Los diferentes niveles de registro - TRACE, DEBUG, INFO, WARN - permiten categorizar la relevancia de los mensajes capturados.

TRACE y DEBUG se utilizan para detalles de bajo nivel y depuración, INFO proporciona información general del flujo de la aplicación, y WARN indica situaciones potencialmente problemáticas que no afectan directamente el funcionamiento. 

Los registros de nivel ERROR indican que ha ocurrido un problema grave en la aplicación que necesita ser abordado y corregido. Los errores representan situaciones en las que la aplicación no puede funcionar correctamente o ha entrado en un estado inesperado.

Comprender estos niveles ayuda a establecer una estrategia de registro efectiva que facilita tanto el desarrollo como la operación de la aplicación.

```properties
# Logging por defecto
logging.level.root=ERROR
logging.level.ar.lamansys=WARN
logging.level.net.pladema=WARN
```

```properties
# Logging en profile dev
logging.level.root=INFO
logging.level.ar.lamansys=INFO
logging.level.net.pladema=INFO
```

