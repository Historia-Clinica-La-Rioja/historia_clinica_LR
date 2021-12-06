package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
public class MonitoringRequestInterceptor implements ClientHttpRequestInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request);
        Instant start = Instant.now();
        ClientHttpResponse response = execution.execute(request, body);
        Instant finish = Instant.now();
        Duration timeElapsed = Duration.between(start, finish);
        traceResponse(response, timeElapsed);
        return response;
    }

    private void traceRequest(HttpRequest request){
		logger.debug("===========================Request begin================================================");
        logger.debug("URI         : {}", request.getURI());
        logger.debug("Method      : {}", request.getMethod());
        logger.debug("===========================Request end================================================");
    }

    private void traceResponse(ClientHttpResponse response, Duration timeElapsed) throws IOException {
        logger.debug("============================Response begin==========================================");
        logger.debug("Status code  : {}", response.getStatusCode());
        logger.debug("Elapsed time : {}", timeElapsed);
        logger.debug("============================Response end=================================================");
    }

}
