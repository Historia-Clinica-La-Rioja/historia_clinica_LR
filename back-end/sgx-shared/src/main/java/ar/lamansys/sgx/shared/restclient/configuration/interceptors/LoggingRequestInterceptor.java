package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.stats.TimeProfilingUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {
	
	@Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var req = traceRequest(request, body);
		var requestStat = TimeProfilingUtil.start("REST");
        ClientHttpResponse response = execution.execute(request, body);
		requestStat.done(req);
		var respStatus = debugResponse(response, req);
		traceResponse(response, req, respStatus);
        return response;
    }

	private String traceRequest(HttpRequest request, byte[] body) {
		var req = String.format("%s: %s", request.getMethod(), request.getURI());
		log.trace("===========================Request begin================================================");
		log.trace(req);
		log.trace("Method      : {}", request.getMethod());
		log.trace("Headers     : {}", request.getHeaders() );
		log.trace("Request body: {}", new String(body, StandardCharsets.UTF_8));
		log.trace("===========================Request end================================================");
		return req;
    }

	private String debugResponse(ClientHttpResponse response, String req) throws IOException {
		var resp = String.format("%s '%s'", response.getStatusCode(), response.getStatusText());
		log.debug("Obtuve {} desde {}", resp, req);
		return resp;
	}

    private void traceResponse(ClientHttpResponse response, String req, String respStatus) throws IOException {
		if (!log.isTraceEnabled()) {
			return;
		}

        log.trace("============================Response begin==========================================");
		log.trace(req);
		log.trace("Status code  : {}", response.getStatusCode());
		log.trace("Status text  : {}", response.getStatusText());
		log.trace("Headers      : {}", response.getHeaders());
		log.trace("Response body: {}", buildBody(response));
		log.trace("============================Response end=================================================");

    }

	private static StringBuilder buildBody(ClientHttpResponse response) throws IOException {
		StringBuilder inputStringBuilder = new StringBuilder();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
		String line = bufferedReader.readLine();
		while (line != null) {
			inputStringBuilder.append(line);
			inputStringBuilder.append('\n');
			line = bufferedReader.readLine();
		}
		return inputStringBuilder;
	}

}
