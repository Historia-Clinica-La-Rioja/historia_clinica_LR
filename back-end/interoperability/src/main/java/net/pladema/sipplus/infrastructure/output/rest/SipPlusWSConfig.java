package net.pladema.sipplus.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.WSConfig;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ws.sip.plus")
@Getter
@Setter
public class SipPlusWSConfig extends WSConfig {

	private final String username;

	private final String password;

	public SipPlusWSConfig(@Value("${ws.sip.plus.url.base:}") String baseUrl,
						   @Value("${ws.sip.plus.username:}") String username,
						   @Value("${ws.sip.plus.password:}") String password) {
		super(baseUrl, false);
		this.username = username;
		this.password = password;
	}
}