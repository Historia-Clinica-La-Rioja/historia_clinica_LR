package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

@Service
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body){
		logger.debug("===========================Request begin================================================");
        logger.debug("URI         : {}", request.getURI());
        logger.debug("Method      : {}", request.getMethod());
        logger.debug("Headers     : {}", request.getHeaders() );
        logger.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));
        logger.debug("===========================Request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        logger.debug("============================Response begin==========================================");
        logger.debug("Status code  : {}", response.getStatusCode());
        logger.debug("Status text  : {}", response.getStatusText());
        logger.debug("Headers      : {}", response.getHeaders());
        logger.debug("Response body: {}", inputStringBuilder);
        logger.debug("============================Response end=================================================");
    }

}
