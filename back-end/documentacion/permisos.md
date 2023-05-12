# ### ![logo](../../front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Back-end | Permisos


## Authorization

El filtro de Autorización (AuthorizationFilter) va a cargar la lista de roles del usuario.

## Permiso en cada endpoint

La tabla `user_role` se utiliza para que en cada request, el backend pueda cargar los roles del usuario. Para esto se realiza una consulta similar a:

```SELECT ur.roleId, ur.institutionId FROM UserRole ur WHERE ur.userId = :userId```

Con esta única consulta realizada al procesar el token de sesión del usuario se carga en memoria la información necesaria para realizar cualquier chequeo de permisos. Esta lista de roles por institución se utiliza para definir los Authorities que tiene el usuario y así permitir que se realice el chequeo de nivel global en `net.pladema.sgh.app.security.infraestructure.configuration.WebSecurityConfiguration`.

Para los chequeos a nivel endpoint se utiliza la anotación `@PreAutorize`, que se puede utilizar para validar que el usuario tenga algún rol a nivel sistema o a nivel institución.

### Permisos a nivel sistema

Se chequea si el usuario tiene uno o varios roles.

```java
@PreAuthorize("hasAnyAuthority('ROOT')")
public boolean uploadFile(/* parametros */) {
    /** contenido del método */
}
```

### Permisos a nivel institución

Se chequea si el usuario tiene uno o varios roles en la institución dada.

```java
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO_ADULTO_MAYOR')")
public ResponseEntity<Boolean> createAnamnesis(
    @PathVariable(name = "institutionId") Integer institutionId
) {
    /** contenido del método */
}
```

### Permisos en tableros

Para cada tablero de cubejs se define el cubo a partir del userId de la sesión de usuario y usando joins y condiciones con `user_role` y el `id` del rol.


