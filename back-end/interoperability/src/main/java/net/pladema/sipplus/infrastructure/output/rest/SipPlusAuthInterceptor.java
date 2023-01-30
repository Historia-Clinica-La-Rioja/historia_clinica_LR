package net.pladema.sipplus.infrastructure.output.rest;

import lombok.extern.slf4j.Slf4j;
import net.pladema.snvs.infrastructure.configuration.SnvsCondition;
import net.pladema.snvs.infrastructure.output.rest.report.SisaWSConfig;

import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class SipPlusAuthInterceptor implements ClientHttpRequestInterceptor {

	private SipPlusWSConfig sipPlusWSConfig;

	public SipPlusAuthInterceptor(SipPlusWSConfig sipPlusWSConfig) {
		super();
		this.sipPlusWSConfig = sipPlusWSConfig;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		log.debug("SipPlusAuthInterceptor set authentication headers");
		headers.add("Authorization", getBasicAuthenticationHeader(sipPlusWSConfig.getUsername(), sipPlusWSConfig.getPassword()));
		return execution.execute(request, body);
	}

	private static String getBasicAuthenticationHeader(String username, String password) {
		String valueToEncode = Stream.of(username,password).collect(Collectors.joining(":"));
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

}