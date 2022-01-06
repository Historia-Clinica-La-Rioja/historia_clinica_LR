package net.pladema.snvs.infrastructure.output.rest.report;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="ws.sisa.snvs")
@Getter
@Setter
@Conditional(SnvsCondition.class)
public class SisaWSConfig extends WSConfig {

	//Headers
	private final String appIdHeader;

	private final String appId;

	private final String appKeyHeader;

	private final String appKey;

	private final String nominalCase;

	public SisaWSConfig(@Value("${ws.sisa.snvs.url.base:https://ws400-qa.sisa.msal.gov.ar}") String baseUrl,
						@Value("${ws.sisa.snvs.appId.header:APP_ID}") String appIdHeader,
						@Value("${ws.sisa.snvs.appId.value:PruebasWSQA_SNVS_ID}") String appId,
						@Value("${ws.sisa.snvs.appId.header:APP_KEY}") String appKeyHeader,
						@Value("${ws.sisa.snvs.appKey.value:PruebasWSQA_SNVS_KEY}") String appKey,
						@Value("${ws.sisa.snvs.url.nominalcase:/snvsCasoNominal/v2/snvsCasoNominal}") String nominalCase) {
		super(baseUrl, false);
		this.appIdHeader = appIdHeader;
		this.appId = appId;
		this.appKeyHeader = appKeyHeader;
		this.appKey = appKey;
		this.nominalCase = nominalCase;
	}
}
