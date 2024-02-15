package net.pladema.digitalsignature.infrastructure.output.configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import ar.lamansys.sgx.shared.restclient.services.AuthService;
import net.pladema.digitalsignature.domain.FirmadorLoginResponse;

import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class FirmadorAuthService extends AuthService<FirmadorLoginResponse> {

	private final String AUTHORIZATION = "Authorization";
	private final FirmadorWSConfig wsConfig;
	public FirmadorAuthService(HttpClientConfiguration configuration,
							   FirmadorWSConfig wsConfig) throws Exception {
		super(wsConfig.getPathLogin() + "?grant_type=client_credentials", new RestTemplateSSL(configuration), wsConfig);
		this.wsConfig = wsConfig;
	}

	@Override
	protected ResponseEntity<FirmadorLoginResponse> callLogin() {
		ResponseEntity<FirmadorLoginResponse> result = exchangePost(relUrl, null, FirmadorLoginResponse.class);
		return result;
	}

	@Override
	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(APPLICATION_JSON);
		headers.add(AUTHORIZATION, getBasicAuthenticationHeader());
		return headers;
	}


	private String getBasicAuthenticationHeader() {
		String valueToEncode = Stream.of(wsConfig.getApiKey(),wsConfig.getApiToken()).collect(Collectors.joining(":"));
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}
}
