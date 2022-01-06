package net.pladema.snvs.infrastructure.output.rest.report;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@Conditional(SnvsCondition.class)
public class SisaAuthInterceptor implements ClientHttpRequestInterceptor {

	private SisaWSConfig sisaWSConfig;

	public SisaAuthInterceptor(SisaWSConfig sisaWSConfig) {
		super();
		this.sisaWSConfig = sisaWSConfig;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		log.debug("SisaAuthInterceptor set authentication headers");
		headers.add(sisaWSConfig.getAppIdHeader(), sisaWSConfig.getAppId());
		headers.add(sisaWSConfig.getAppKeyHeader(), sisaWSConfig.getAppKey());
		return execution.execute(request, body);
	}


}
