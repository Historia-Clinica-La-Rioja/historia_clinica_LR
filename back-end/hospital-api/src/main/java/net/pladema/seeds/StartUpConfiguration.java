package net.pladema.seeds;


import net.pladema.seeds.data.SampleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@ConditionalOnProperty(
        value="spring.datasource.url",
        havingValue = "jdbc:postgresql://localhost:5432/hospitalDB")
public class StartUpConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(StartUpConfiguration.class);

    private final SampleData sampleData;

    public StartUpConfiguration(SampleData sampleData) {
        this.sampleData = sampleData;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.debug("Got application event: {}", event);
        sampleData.populateDB();
    }

}
