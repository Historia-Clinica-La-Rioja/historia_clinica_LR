package ar.lamansys.sgx.shared.templating.domain;

import java.util.Locale;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationContext {
	public final Locale locale;
	public final Map<String,Object> variables;
}
