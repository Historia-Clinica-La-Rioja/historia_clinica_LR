package net.pladema.hl7.supporting.conformance;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class InteroperabilityCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Boolean enabled = context.getEnvironment().getProperty("ws.federar.enabled", Boolean.class);
        if (enabled != null){
            return enabled;
        }
        return false;
    }
}
