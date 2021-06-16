# Contribuciones

En este documento se definen los requerimientos minimos para poder contribuir con nueva funcionalidad al sistema. 

## Requerimientos

### Funcionales

- [x] Presentar una especificación funcional
  -  Diseño de la solución
  -  Plan de desarrollo del cambio a realizar
  -  Documentación de la API (Swagger)
  -  Prototipo navegable donde se pueda apreciar el cambio funcional (Aprueba Ministerio de Salud)
  
  ### Repositorio

- [x] El cambio debe ser enviado al branch master, mediante un merge request.
- [x] Los nombres de los commits deben contemplar la siguiente nomeclatura: **git-###. descripción**.  Donde **###** es el número del ticket en GIT
- [x] Debe pasar el pipeline del CI

### Base de datos

- [x] Debe cumplir con las convenciones definidas en [base de datos](../dba/documentacion/convenciones.md)

### Código

- [x] Debe cumplir con las convenciones definidas en [back-end](back-end/documentacion/convenciones.md)
- [x] Debe cumplir con las convenciones definidas en [front-end](front-end/README.md)

### Testing

- [x] Plan de test a ejecutar.
- [x] Debe tener test unitarios para la nueva funcionalidad agregada



La nueva funcionalidad será validada desde el equipo de PLADEMA, mediante herramientas automáticas o manuales según corresponda. Durante este proceso pueden surgir pedidos de cambios sobre código, funcionalidad, etc.

Una vez validado el merge request, el equipo de QA perteneciente a PLADEMA y al Ministerio de Salud realizará los test de integración y aceptación correspondientes para finalmente enviar el código al repositorio.