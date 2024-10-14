package snomed.relations.cache.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SnomedRelationsCacheCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Boolean enabled = context.getEnvironment()
				.getProperty("app.feature.HABILITAR_RELACIONES_SNOMED", Boolean.class);
		return enabled != null && enabled;
	}

}
