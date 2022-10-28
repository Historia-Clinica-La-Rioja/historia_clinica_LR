package ar.lamansys.sgx.shared.actuator.infrastructure.configuration;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import ar.lamansys.sgx.shared.actuator.domain.PropertyBo;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemProperty;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemPropertyRepository;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.ToggleConfiguration;
import ar.lamansys.sgx.shared.flavor.FlavorService;
import ar.lamansys.sgx.shared.strings.StringHelper;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class SystemPropertiesConfiguration {

    private final DateTimeProvider dateTimeProvider;

    private final Optional<EnvironmentEndpoint> environ;

    private final FlavorService flavorService;

	private final AppNode appNode;

    private final SystemPropertyRepository systemPropertyRepository;

    private final MessageSource messageSource;

    private final String defaultLanguage;

	private final String include;

    public SystemPropertiesConfiguration(
			Optional<EnvironmentEndpoint> environ,
		 	SystemPropertyRepository systemPropertyRepository,
		 	DateTimeProvider dateTimeProvider,
		 	FlavorService flavorService,
            MessageSource messageSource,
			AppNode appNode,
            @Value("${app.default.language:es}") String defaultLanguage
    ) {
        this.environ = environ;
        this.systemPropertyRepository = systemPropertyRepository;
        this.dateTimeProvider = dateTimeProvider;
        this.messageSource = messageSource;
		this.appNode = appNode;
        this.flavorService = flavorService;
        this.defaultLanguage = defaultLanguage;
		Locale.setDefault(new Locale(defaultLanguage));
		this.include = "(spring\\.*|email-*|ws\\.*|logging\\.*|ws\\.*|jobs\\.*|"
				+ "app\\.*|api\\.*|admin\\.*|token\\.*|management\\.*|"
				+ "oauth\\.*|externalurl\\.*|integration\\.*|actuator\\.*|"
				+ "internment\\.*|images\\.*|mail\\.*|recaptcha\\.*|.*\\.cron\\.*|"
				+ "habilitar\\.*|hsi\\.*|booking\\.*|mqtt\\.*|snomed-semantics\\.*|"
				+ "scheduledjobs\\.*|auth\\.*"
				+ ")";

    }

    public void loadProperties() {
		log.info("Loading properties");
        systemPropertyRepository.deleteByIpNodeId(appNode.nodeId);
        systemPropertyRepository.saveAll(environ.map(env -> env.environment(include))
                .map(environmentDescriptor -> generateSource(environmentDescriptor.getPropertySources()))
                .orElse(Collections.emptySet()));
    }


    private Set<SystemProperty> generateSource(List<EnvironmentEndpoint.PropertySourceDescriptor> propertySources) {
        Set<PropertyBo> sortedSet = new LinkedHashSet<>();
        propertySources.forEach(propertySourceDescriptor -> {
            Set<PropertyBo> subProperties = buildProperty(propertySourceDescriptor);
            sortedSet.addAll(subProperties);
        });
        sortedSet.addAll(buildDefaultFeatureFlags());
        String exclude = "app.data.sample.*";
        return sortedSet.stream()
                .filter(propertyBo -> !Pattern.matches(exclude,propertyBo.getProperty()))
                .map(this::mapToEntity)
                .collect(Collectors.toSet());

    }

    private Set<PropertyBo> buildDefaultFeatureFlags() {
        return flavorService.getFeaturesState().getStates().entrySet()
                .stream()
                .map(entry -> new PropertyBo(null,
                        entry.getKey().propertyNameFor(),
                        entry.getValue().toString(),
                        entry.getKey().getLabel(),
                        "Default feature flag",
						appNode.nodeId,
                        dateTimeProvider.nowDateTime()))
                .collect(Collectors.toSet());
    }

    private SystemProperty mapToEntity(PropertyBo propertyBo) {
        return new SystemProperty(propertyBo.getId(), propertyBo.getProperty(), propertyBo.getValue(), propertyBo.getDescription(), propertyBo.getOrigin(),
                    propertyBo.getNodeId(), propertyBo.getUpdatedOn());
    }

    private Set<PropertyBo> buildProperty(EnvironmentEndpoint.PropertySourceDescriptor propertySourceDescriptor) {
		log.info("Building properties from source {}", propertySourceDescriptor.getName());
        LinkedHashSet<PropertyBo> sortedSet = new LinkedHashSet();
        propertySourceDescriptor
                .getProperties()
                .forEach((propertyKey, propertyValueDescriptor) -> sortedSet.add(
                        new PropertyBo(
								null,
                                propertyKey,
								StringHelper.toString(propertyValueDescriptor.getValue()),
                                buildLabel(propertyKey),
                                propertySourceDescriptor.getName(),
								appNode.nodeId,
								dateTimeProvider.nowDateTime())
						)
				);
        return sortedSet;
    }

    private String buildLabel(String propertyKey) {
        return isFeatureFlag(propertyKey) ?
                getLabel(propertyKey) :
                messageSource.getMessage(propertyKey, null, propertyKey, new Locale(defaultLanguage));
    }

    private String getLabel(String propertyKey) {
        return flavorService.getFeaturesState()
                .getAppFeatureByPropertyKey(propertyKey)
                .map(AppFeature::getLabel)
                .orElse(propertyKey);
    }

    private boolean isFeatureFlag(String propertyKey) {
        return propertyKey.startsWith(ToggleConfiguration.PREFIX_APP_FEATURE_PROPERTY);
    }

}