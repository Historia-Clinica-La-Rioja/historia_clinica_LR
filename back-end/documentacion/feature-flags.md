# FeatureFlags

## Definición de FF

Todos los FF están definidos como enumerados en la clase [AppFeature.java](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/featureflags/AppFeature.java).

## Estados de FF

Cada sabor definido en la clase [FlavorBo.java](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/flavor/FlavorBo.java) debe definir los FF activos usando la clase [InitialFeatureStatesStrategy.java](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/flavor/InitialFeatureStatesStrategy.java).

## Sobreescribir FF
 
Se puede sobreescribir el valor inicial de los FF definiendo propiedades como:

```properties
app.feature.MAIN_DIAGNOSIS_REQUIRED=true
app.feature.HABILITAR_ALTA_SIN_EPICRISIS=false 
```

## Uso de FF en el backend

La implementación de FF se hizo usando [togglz](https://www.togglz.org/).

Para evaluar si una Feature se encuentra activada o desactivada se puede hacer:

```java
AppFeature.HABILITAR_ALTA_SIN_EPICRISIS.isActive()
```

## Uso de FF en el frontend

Los FF en el frontend están disponibles también como el enum AppFeature definido en [@api-rest/api-model.d.ts](../../front-end/apps/projects/hospital/src/app/modules/api-rest/api-model.d.ts).

Para evaluar si una Feature se encuentra activada o desactivada se debe hacer:

```typescript
// imports (ver aclaración abajo)
import {
	AppFeature
} from '@api-rest/api-model.d';

import { FeatureFlagService } from "@core/services/feature-flag.service";

// dentro del componente:
    featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS)
        .subscribe(isActive => if (isActive) { /* ... */});

```

> Aclaración: hay que tener cuidado con el import ya que hay que incluir el .d porque los archivos de definiciones (*.d.ts) no pueden contener tipos como este `const enum`, ver:
>   1. Bug reportado en Angular (cerrado): https://github.com/angular/angular-cli/issues/4874
>   2. Bug reportado en TypeScript: https://github.com/microsoft/TypeScript/issues/17552


#### Uso de FF mediante guards

Para evaluar un ff desde un router es posible hacerlo mediante `FeatureFlagGuard` de la siguiente forma:

```typescript 
canActivate: [FeatureFlagGuard],
data: {
	featureFlag: AppFeature.CUSTOM_FLAG
}
