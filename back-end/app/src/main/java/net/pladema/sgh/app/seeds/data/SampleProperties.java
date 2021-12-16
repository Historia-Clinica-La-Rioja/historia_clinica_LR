package net.pladema.sgh.app.seeds.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@Profile("dev")
@ConfigurationProperties(prefix = "app.data.sample")
@PropertySource(value = "classpath:sample-data.properties")
public class SampleProperties {

    private List<InstitutionInfoSeed> institutions;

    // standard getters and setters
}