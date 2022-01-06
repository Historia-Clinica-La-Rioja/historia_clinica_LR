package ar.lamansys.sgx.shared.templating;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;

/**
 * Una implementación de {@link TemplateEngine} utilizando {@link SpringTemplateEngine} para usar el templateId como
 * nombre de un archivo JSON que se procesa con los parámetros dados para generar el JSON final que con
 * {@link ObjectMapper} se crea una nueva instancia de T.
 *
 * Además esta implementación por cuestiones de seguridad toma la decisión de que se definan los nombres de los
 * templateId habilitados de antemano.
 */
public class JSONTemplateEngine<T> implements TemplateEngine<T>{
	private final String[] allowTemplateIds;
	private final SpringTemplateEngine springTemplateEngine;
	private final ObjectMapper objectMapper;
	private final TypeReference<T> valueTypeRef;

	public JSONTemplateEngine(
			String[] allowTemplateIds,
			SpringTemplateEngine springTemplateEngine,
			ObjectMapper objectMapper,
			TypeReference<T> valueTypeRef
	) {
		this.allowTemplateIds = allowTemplateIds;
		this.springTemplateEngine = springTemplateEngine;
		this.objectMapper = objectMapper;
		this.valueTypeRef = valueTypeRef;
	}

	@Override
	public T process(final String templateId, final Map<String, String> params) throws TemplateException {
		assertTemplateIdAllowed(templateId);
		try {
			return objectMapper.readValue(
					new ByteArrayInputStream(
						springTemplateEngine.process(templateId, buildContext(params)).getBytes()
					),
					valueTypeRef
			);
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), e);
		}
	}

	private void assertTemplateIdAllowed(String templateId) throws TemplateException {
		if (!Arrays.stream(allowTemplateIds).anyMatch(templateId::equals)) {
			throw new TemplateException(templateId, null);
		}
	}

	private static Context buildContext(Map<String,String> contextMap) {
		Context ctx = new Context(Locale.getDefault());
		contextMap.forEach((String name, String value) -> ctx.setVariable(name, value));

		return ctx;
	}
}
