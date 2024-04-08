package net.pladema.report.infrastructure.output.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConditionalOnProperty(
		value="app.usage-metrics.enabled",
		havingValue = "true"
)
@ConfigurationProperties(prefix = "app.usage-metrics")
public class ReportWSConfig {

	private String url;
	private String appId;
	private String appKey;

}