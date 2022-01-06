package net.pladema.snvs.infrastructure.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SnvsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        return  isPropertyEnabled(context, "ws.sisa.snvs.enabled") &&
                isPropertyEnabled(context, "app.feature.HABILITAR_REPORTE_EPIDEMIOLOGICO");
    }

    private boolean isPropertyEnabled(ConditionContext context, String propertyName) {
        Boolean enabled = context.getEnvironment().getProperty(propertyName, Boolean.class);
        if (enabled != null){
            return enabled;
        }
        return false;
    }
}
