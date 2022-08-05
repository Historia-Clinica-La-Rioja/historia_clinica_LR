package ar.lamansys.sgx.shared.appnode.configuration;


import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.actuator.infrastructure.configuration.SystemPropertiesConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class AppInitialization {

	private final NodeService nodeService;
    private final SystemPropertiesConfiguration systemPropertiesConfiguration;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Got application event: {}", event);
		nodeService.updateDBNodeData();
		systemPropertiesConfiguration.loadProperties();
    }

}
