package net.pladema.sgh.app.seeds;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.sgh.app.seeds.data.SampleData;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
@Profile("dev")
@ConditionalOnProperty(
        value="spring.datasource.url",
        havingValue = "jdbc:postgresql://localhost:5432/hospitalDB")
public class StartUpConfiguration {

    private final SampleData sampleData;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("Got application event: {}", event);
        sampleData.populateDB();
    }

}
