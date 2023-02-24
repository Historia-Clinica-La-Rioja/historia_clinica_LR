package net.pladema.sgh.app.seeds;


import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sgh.app.seeds.data.SampleData;

@AllArgsConstructor
@Slf4j
@Component
public class StartUpConfiguration {

	private final SampleData sampleData;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug("Got application event: {}", event);

		if (sampleData.shouldRun()) {
			log.warn("Agregando información de ejemplo en la base de datos");
			sampleData.populateDB();
		}
		log.info("Tareas de inicio completadas");
	}


}
