# README imagenetwork en construcción

## Componentes participantes en el flujo que corresponde a obtener un estudio médico de un paciente
<img src="documentacion/images/componentes.png" width="70%" height="70%" alt="componentes.png">

## Diagrama de secuencia del manejo de autenticación del backend de HSI y el proxy del PAC global
``` mermaid
sequenceDiagram
    FE->>FE: Médico clickea para ver estudio
    FE->>+BE: Obtener datos de estudio
    Note right of BE: PAC_TOKEN_SECRET
    BE-->>-FE: {pac/id/token}
    FE->>+OHIF: Abrir estudio(pac/id/token)
    OHIF->>+Proxy: Abrir estudio(id/token)
    Note right of Proxy: PAC_TOKEN_SECRET
    Proxy->>+Proxy: VerificarToken(id,token)
    Proxy->>+PAC:  Abrir estudio(id)
    PAC-->>-Proxy: (estudio)
    Proxy-->>-OHIF: (estudio)
```

## Comentarios adicionales

PATHS HABILITADOS. Solamente se permite consultar los paths:

`/dicom-web/studies`
`/wado`

Se bloquearon todos los demás *paths* porque no son necesarios y algunos no deben exponerse a la red.