package net.pladema.sgx.restclient.configuration.resttemplate;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

public class RestTemplateExceptionHandler extends DefaultResponseErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestTemplateExceptionHandler.class);
	
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		LOG.error("Error en Cliente: {}", response);
	}
}