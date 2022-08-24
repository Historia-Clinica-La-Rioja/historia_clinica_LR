package ar.lamansys.sgx.shared.restclient.configuration.interceptors;

import ar.lamansys.sgx.shared.context.BeanUtil;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.restclient.infrastructure.output.repository.RestClientMeasure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;


@Service
@Slf4j
public class MonitoringRequestInterceptor implements ClientHttpRequestInterceptor {
	private final static short BAD_CONNECTION_STATUS_CODE = -1;
	private final DateTimeProvider dateTimeProvider;

	public MonitoringRequestInterceptor() {
		this.dateTimeProvider = new DateTimeProvider();
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		traceRequest(request);
		Instant start = Instant.now();
		ClientHttpResponse response;
		Short statusCode = BAD_CONNECTION_STATUS_CODE;
		try {
			response = execution.execute(request, body);
			statusCode = (short)response.getStatusCode().value();
		} finally {
			Instant finish = Instant.now();
			Duration timeElapsed = Duration.between(start, finish);
			traceResponse(statusCode, timeElapsed);
			storageMeasure(request, statusCode , timeElapsed);
		}
		return response;
	}

	public void storageMeasure(HttpRequest request, short statusCode, Duration timeElapsed) {
		BeanUtil.publishEvent(new OnRequestEvent(
				new RestClientMeasure(request.getURI().toString(),
						request.getURI().getHost(),
						request.getURI().getPath(),
						request.getMethodValue(),
						statusCode,
						timeElapsed.toMillis(),
						dateTimeProvider.nowDateTime())));
		log.debug("continue");
	}

	private void traceRequest(HttpRequest request){
		log.debug("===========================Request begin================================================");
		log.debug("URI         : {}", request.getURI());
		log.debug("Method      : {}", request.getMethod());
		log.debug("===========================Request end================================================");
	}

	private void traceResponse(short statusCode, Duration timeElapsed) throws IOException {
		log.debug("============================Response begin==========================================");
		log.debug("Status code  : {}", statusCode);
		log.debug("Elapsed time : {}", timeElapsed);
		log.debug("============================Response end=================================================");
	}

}