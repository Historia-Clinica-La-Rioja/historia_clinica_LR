package net.pladema.renaper.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class RenaperCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        Boolean enabled = context.getEnvironment()
                .getProperty("ws.renaper.enabled", Boolean.class);
        return enabled != null && enabled;
    }
}
