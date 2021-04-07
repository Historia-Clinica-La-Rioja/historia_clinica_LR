package net.pladema.odontology.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "net.pladema.odontology")
@PropertySource(value = "classpath:odontology.properties", ignoreResourceNotFound = true)
public class OdontologyAutoConfiguration {
}
