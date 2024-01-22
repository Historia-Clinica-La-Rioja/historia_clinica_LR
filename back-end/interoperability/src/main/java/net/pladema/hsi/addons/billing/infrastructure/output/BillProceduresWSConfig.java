package net.pladema.hsi.addons.billing.infrastructure.output;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConditionalOnProperty(
		value="app.addons.billing.enabled",
		havingValue = "true"
)
@ConfigurationProperties(prefix = "app.addons.billing")
public class BillProceduresWSConfig {
	private String url;
	private String appKey;
	private String appKeyHeader = "external-token";

	private String encounterUrl = "encounter";
}
