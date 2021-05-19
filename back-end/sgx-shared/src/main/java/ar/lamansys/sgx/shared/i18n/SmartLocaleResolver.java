package ar.lamansys.sgx.shared.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Set;

public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {

    @Value("${app.default.language}")
    protected String defaultLanguage;

    @Value("${app.other.languages}")
    protected Set<String> othersLanguages;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        final String acceptLanguage = request.getHeader("Accept-Language");
        return othersLanguages.stream().filter(l -> l.equalsIgnoreCase(acceptLanguage))
                .findAny().map(Locale::forLanguageTag).orElse(Locale.forLanguageTag(defaultLanguage));
        }
}