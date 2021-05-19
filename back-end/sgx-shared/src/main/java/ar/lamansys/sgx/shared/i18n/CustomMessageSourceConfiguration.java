package ar.lamansys.sgx.shared.i18n;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CustomMessageSourceConfiguration implements WebMvcConfigurer {

    @Value("${app.default.language}")
    protected String defaultLanguage;

    @Value("${app.other.languages}")
    protected Set<String> othersLanguages;

    @Bean
    public LocaleResolver localeResolver() {
        SmartLocaleResolver resolver = new SmartLocaleResolver();
        List<Locale> otherLocales = othersLanguages.stream().map(Locale::forLanguageTag).collect(Collectors.toList());
        resolver.setSupportedLocales(otherLocales);
        resolver.setDefaultLocale(Locale.forLanguageTag(defaultLanguage));
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Override
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

}