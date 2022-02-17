# ![logo](apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) HSI | Ingress

En este documento se detalla las particularidades del balanceador de carga que tendrá la responsabilidad de ser el punto de acceso principal del sistema. En [Kubernetes este objeto se llama Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/).

> An API object that manages external access to the services in a cluster, typically HTTP. Ingress may provide load balancing, SSL termination and name-based virtual hosting.


## Reglas

### Api

Los pedidos a `/api/**` se deben redireccionar al servicio [back-end](../../back-end).

### Assets

Los pedidos a los siguientes archivos deben redireccionar al servicio [back-end](../../back-end) en las rutas indicadas:
1. `/favicon.ico` a `/api/assets/favicon.ico`
2. `/assets/custom/**` a `/api/assets/**`.

### Todo lo demás

Por último, todos los pedidos deben ser redireccionados al servicio [front-end](../../front-end).