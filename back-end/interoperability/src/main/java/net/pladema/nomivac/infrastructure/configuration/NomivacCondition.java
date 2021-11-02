package net.pladema.nomivac.infrastructure.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.net.MalformedURLException;
import java.net.URL;

public class NomivacCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context,
                           AnnotatedTypeMetadata annotatedTypeMetadata) {
        return  cronConfigured(context) &&
                isPropertyEnabled(context, "ws.federar.enabled") &&
                isPropertyEnabled(context, "app.feature.HABILITAR_BUS_INTEROPERABILIDAD") &&
                validUrlProperty(context,"ws.nomivac.synchronization.url.base") &&
                validUrlProperty(context,"ws.federar.url.base");
    }

    private boolean validUrlProperty(ConditionContext context, String propertyName){
        String url = context.getEnvironment().getProperty(propertyName, String.class);
        if (url == null)
            return false;
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }
    private boolean cronConfigured(ConditionContext context){
        String cron = context.getEnvironment().getProperty("ws.nomivac.synchronization.cron.config", String.class);
        return cron != null && !cron.isBlank();
    }
    private boolean isPropertyEnabled(ConditionContext context, String propertyName) {
        Boolean enabled = context.getEnvironment().getProperty(propertyName, Boolean.class);
        if (enabled != null){
            return enabled;
        }
        return false;
    }
}
