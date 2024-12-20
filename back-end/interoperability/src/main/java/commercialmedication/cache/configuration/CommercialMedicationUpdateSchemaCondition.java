package commercialmedication.cache.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class CommercialMedicationUpdateSchemaCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Boolean enabled = context.getEnvironment()
				.getProperty("app.feature.HABILITAR_SERVICIO_INFO_COMERCIAL_MEDICAMENTOS", Boolean.class);
		return enabled != null && enabled;
	}

}
