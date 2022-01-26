package ar.lamansys.sgx.shared.actuator.infrastructure.configuration;

import ar.lamansys.sgx.shared.actuator.domain.PropertyBo;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemProperty;
import ar.lamansys.sgx.shared.actuator.infrastructure.output.repository.SystemPropertyRepository;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.ToggleConfiguration;
import ar.lamansys.sgx.shared.flavor.FlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Configuration
@Slf4j
public class SystemPropertiesConfiguration {

    private final DateTimeProvider dateTimeProvider;

    private final Optional<EnvironmentEndpoint> environ;

    private final FlavorService flavorService;

    private final String nodeId;

    private final SystemPropertyRepository systemPropertyRepository;

    public SystemPropertiesConfiguration(Optional<EnvironmentEndpoint> environ,
                                         SystemPropertyRepository systemPropertyRepository,
                                         DateTimeProvider dateTimeProvider,
                                         FlavorService flavorService) {
        this.environ = environ;
        this.systemPropertyRepository = systemPropertyRepository;
        this.dateTimeProvider = dateTimeProvider;
        this.nodeId = UUID.randomUUID().toString();
        this.flavorService = flavorService;
        systemPropertyRepository.deleteAll();
    }

    @Scheduled(cron = "0 0/1 * * * * ")
    public void loadProperties() {
        String include = "(spring\\.*|email-*|ws\\.*|logging\\.*|ws\\.*|jobs\\.*|"
                + "app\\.*|api\\.*|admin\\.*|token\\.*|management\\.*|"
                + "oauth\\.*|externalurl\\.*|integration\\.*|actuator\\.*|"
                + "internment\\.*|images\\.*|mail\\.*|recaptcha\\.*|.*\\.cron\\.*|"
                + "habilitar\\.*|hsi\\.*"
                + ")";
        systemPropertyRepository.deleteByIpNodeId(nodeId);
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
        return sortedSet.stream()
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
                        nodeId,
                        dateTimeProvider.nowDateTime()))
                .collect(Collectors.toSet());
    }

    private SystemProperty mapToEntity(PropertyBo propertyBo) {
        return new SystemProperty(propertyBo.getId(), propertyBo.getProperty(), propertyBo.getValue(), propertyBo.getDescription(), propertyBo.getOrigin(),
                    propertyBo.getNodeId(), propertyBo.getUpdatedOn());
    }

    private Set<PropertyBo> buildProperty(EnvironmentEndpoint.PropertySourceDescriptor propertySourceDescriptor) {
        LinkedHashSet<PropertyBo> sortedSet = new LinkedHashSet();
        propertySourceDescriptor
                .getProperties()
                .forEach((propertyKey, propertyValueDescriptor) -> sortedSet.add(
                        new PropertyBo(null,
                                propertyKey,
                                (String)(propertyValueDescriptor.getValue()),
                                isFeatureFlag(propertyKey) ? getLabel(propertyKey) : propertyKey,
                                propertySourceDescriptor.getName(),
                                nodeId, dateTimeProvider.nowDateTime())));
        return sortedSet;
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