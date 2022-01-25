package ar.lamansys.sgx.shared.i18n;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.LinkedHashSet;
import java.util.Set;

@RestController
@RequestMapping("/i18n")
@Tag(name = "I18n", description = "I18n")
public class I18NController {

	private static final Logger LOG = LoggerFactory.getLogger(I18NController.class);

	@Value("${app.default.language}")
	protected String defaultLanguage;

	@Value("${app.other.languages}")
	protected Set<String> othersLanguages;

	private LocaleResolver localeResolver;

	public I18NController(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	@GetMapping(value = "/support-languages")
	public ResponseEntity<Set<String>> getLanguages() {
		Set<String> result = new LinkedHashSet<>();
		result.add(defaultLanguage);
		result.addAll(othersLanguages);
		AcceptHeaderLocaleResolver locale = (AcceptHeaderLocaleResolver)localeResolver;
		locale.getDefaultLocale();
		locale.getSupportedLocales();
		LOG.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}
	
}