package ar.lamansys.sgx.shared.templating;

import java.util.Map;

import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;

/**
 * Permite obtener instancias de un tipo genérico T que son creadas a partir de:
 * 		* Un ID de plantilla y un conjunto de parámetros
 */
public interface TemplateEngine<T> {
	T process(final String templateId, final Map<String, String> params) throws TemplateException;
}
