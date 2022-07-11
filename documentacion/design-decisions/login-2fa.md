# Secuencia de Login con autenticación por doble factor

En los siguientes diagramas se esquematiza el orden de ejecución de las distintas etapas de LOGIN en un escenario con el doble factor de autenticación activado. A diferencia del login normal, este login se debe realizar en 2 pasos separados. En el primero se validan las credenciales del usuario, es decir, su username y contraseña. En el segundo se valida un código de 6 dígitos asociado al usuario. 

## Diagrama de flujo para escenario con guardado de tokens en Local Storage del browser

```mermaid
sequenceDiagram
    participant Usuario
    participant Front End
    participant Back End


    Usuario->>Front End: Ingreso a HSI    
    activate Front End
    Front End-->>Usuario: Muestra login form
    deactivate Front End

    Usuario->>Front End: (username, password)
    activate Front End
    Front End->>Back End: POST /auth (username, password)

        activate Back End
            Note right of Back End: Caso de uso login()

    alt FF de 2FA activado && <br/> Usuario tiene 2FA activado && <br/> Usuario tiene 2FA para LOGIN

        Back End->>Back End: Verifica credenciales
        Back End->>Back End: Verifica FF activado
        Back End->>Back End: Verifica que usuario tiene 2FA activado
        Back End->>Back End: Verifica que usuario tiene 2FA para LOGIN activado

        
        Back End->>Back End: Genera TOKEN TEMPORAL

        Back End-->>Front End: TOKEN TEMPORAL
        deactivate Back End


    Front End->>Front End: Guarda token en local storage

    Front End->>Back End: GET /account/permissions <br/> Auth header: TOKEN TEMPORAL

        activate Back End
            Note right of Back End: Security filters
        Back End->>Back End: 2FA filter

        Note right of Back End: 2FA filter se encarga de:<br/> - verificar tipo de token <br/>- cargar datos del usuario <br/> - cargar ROL TEMPORAL

        Back End-->>Front End: roles: [TEMPORAL]
        deactivate Back End


    Front End-->>Usuario: Redirige a 2FA form (o abre pop-up)

    deactivate Front End

    Usuario->>Front End: (6-digit code)
    activate Front End
    Front End->>Back End: POST /2fa <br/> Auth header: TOKEN TEMPORAL <br/>(6-digit code)


        activate Back End
            Note right of Back End: Security filters
        Back End->>Back End: 2FA filter

        Note right of Back End: 2FA filter se encarga de:<br/> - verificar tipo de token <br/>- cargar datos del usuario <br/> - cargar ROL TEMPORAL

            Note right of Back End: Caso de uso validate2FA() <br/> (sólo se accede con ROL TEMPORAL)
        Back End->>Back End: Verifica 6-digit code
        Back End->>Back End: Genera ACCESS TOKEN y REFRESH TOKEN

        Back End-->>Front End: ACCESS TOKEN, REFRESH TOKEN
        deactivate Back End


    Front End->>Front End: Guarda tokens en local storage

    Front End-->>Usuario: Redirige a home de HSI

    deactivate Front End

    end %% fin de camino alternativo

```


## Diagrama de flujo para escenario con manejo de tokens con Cookies

```mermaid
sequenceDiagram
    participant Usuario
    participant Front End
    participant Back End


    Usuario->>Front End: Ingreso a HSI    
    activate Front End
    Front End-->>Usuario: Muestra login form
    deactivate Front End

    Usuario->>Front End: (username, password)
    activate Front End
    Front End->>Back End: POST /auth (username, password)

        activate Back End
            Note right of Back End: Caso de uso login()


    alt FF de 2FA activado && <br/> Usuario tiene 2FA activado && <br/> Usuario tiene 2FA para LOGIN

        Back End->>Back End: Verifica credenciales
        Back End->>Back End: Verifica FF activado
        Back End->>Back End: Verifica que usuario tiene 2FA activado
        Back End->>Back End: Verifica que usuario tiene 2FA para LOGIN activado

        
        Back End->>Back End: Genera TOKEN TEMPORAL

        Back End-->>Front End: SET COOKIE (TOKEN TEMPORAL)
        deactivate Back End

    Front End->>Back End: GET /account/permissions <br/> Cookie: TOKEN TEMPORAL

        activate Back End
            Note right of Back End: Security filters
        Back End->>Back End: 2FA filter

        Note right of Back End: 2FA filter se encarga de:<br/> - verificar tipo de token <br/>- cargar datos del usuario <br/> - cargar ROL TEMPORAL

        Back End-->>Front End: roles: [TEMPORAL]
        deactivate Back End


    Front End-->>Usuario: Redirige a 2FA form (o abre pop-up)

    deactivate Front End



    Usuario->>Front End: (6-digit code)
    activate Front End
    Front End->>Back End: POST /2fa <br/> Cookie: TOKEN TEMPORAL <br/>(6-digit code)


        activate Back End
            Note right of Back End: Security filters
        Back End->>Back End: 2FA filter

        Note right of Back End: 2FA filter se encarga de:<br/> - verificar tipo de token <br/>- cargar datos del usuario <br/> - cargar ROL TEMPORAL

            Note right of Back End: Caso de uso validate2FA() <br/> (sólo se accede con ROL TEMPORAL)
        Back End->>Back End: Verifica 6-digit code
        Back End->>Back End: Genera ACCESS TOKEN y REFRESH TOKEN

        Back End-->>Front End: SET COOKIE: (ACCESS TOKEN, REFRESH TOKEN)
        deactivate Back End


    Front End-->>Usuario: Redirige a home de HSI

    deactivate Front End

    end %% fin de camino alternativo

```