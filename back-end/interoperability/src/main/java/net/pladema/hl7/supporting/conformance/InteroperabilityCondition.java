package net.pladema.hl7.supporting.conformance;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class InteroperabilityCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Boolean interoperabilityFFEnabled = isPropertyEnabled(context, "app.feature.HABILITAR_BUS_INTEROPERABILIDAD");
        Boolean federarEnabled = isPropertyEnabled(context, "ws.federar.enabled");
        return interoperabilityFFEnabled && federarEnabled;
    }

    private boolean isPropertyEnabled(ConditionContext context, String propertyName) {
        Boolean enabled = context.getEnvironment().getProperty(propertyName, Boolean.class);
        if (enabled != null){
            return enabled;
        }
        return false;
    }
}
