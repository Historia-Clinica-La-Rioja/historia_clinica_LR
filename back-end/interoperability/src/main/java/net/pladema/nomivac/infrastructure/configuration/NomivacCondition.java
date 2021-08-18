package net.pladema.nomivac.infrastructure.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class NomivacCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        Boolean synchronizedEnabled = isPropertyEnabled(context, "ws.nomivac.synchronization.enabled");
        Boolean federarEnabled = isPropertyEnabled(context, "ws.federar.enabled");
        return synchronizedEnabled && federarEnabled;
    }


    private boolean isPropertyEnabled(ConditionContext context, String propertyName) {
        Boolean enabled = context.getEnvironment().getProperty(propertyName, Boolean.class);
        if (enabled != null){
            return enabled;
        }
        return false;
    }
}
