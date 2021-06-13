package net.pladema.federar.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FederarCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        Boolean enabled = context.getEnvironment()
                .getProperty("ws.federar.enabled", Boolean.class);
        return enabled != null && enabled;
    }
}
