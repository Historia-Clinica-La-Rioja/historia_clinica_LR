# FeatureFlags

## Definición de FF

Todos los FF están definidos en [net.pladema.sgx.featureflags.AppFeature](../hospital-api/src/main/java/net/pladema/sgx/featureflags/AppFeature.java).

## Estados de FF

Cada sabor definido en [net.pladema.featureflags.service.domain.FlavorBo](../hospital-api/src/main/java/net/pladema/featureflags/service/domain/FlavorBo.java) debe definir los FF activos usando [net.pladema.flavor.service.impl.InitialFeatureStatesStrategy](../hospital-api/src/main/java/net/pladema/flavor/service/impl/InitialFeatureStatesStrategy.java).

## Sobreescribir FF
 
Se puede sobreescribir el valor inicial de los FF definiendo propiedades como:

```properties
app.feature.MAIN_DIAGNOSIS_REQUIRED=true
app.feature.HABILITAR_ALTA_SIN_EPICRISIS=false 
```

## Uso de FF

La implementación de FF se hizo usando [togglz](https://www.togglz.org/).

Para evaluar si una Feature se encuentra activada o desactivada se puede hacer:

```java
AppFeature.HABILITAR_ALTA_SIN_EPICRISIS.isActive()
```