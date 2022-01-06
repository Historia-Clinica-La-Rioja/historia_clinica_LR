package ar.lamansys.sgx.shared.restclient.configuration.resttemplate;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;

public class RestTemplateExceptionHandler extends DefaultResponseErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateExceptionHandler.class);
	
	@SneakyThrows
	@Override
	public void handleError(ClientHttpResponse httpResponse) {
		try {
			super.handleError(httpResponse);
		} catch (HttpStatusCodeException e) {
			var body = e.getResponseBodyAsString();
			LOG.error("Body error: {}", body);
			throw new RestTemplateApiException(httpResponse.getStatusCode(), body, body);
		}
	}
}