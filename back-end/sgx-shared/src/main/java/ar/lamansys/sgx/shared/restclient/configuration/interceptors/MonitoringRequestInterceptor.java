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


@Service
@Slf4j
public class MonitoringRequestInterceptor implements ClientHttpRequestInterceptor {

    private final DateTimeProvider dateTimeProvider;

    public MonitoringRequestInterceptor() {
        this.dateTimeProvider = new DateTimeProvider();
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request);
        Instant start = Instant.now();
        ClientHttpResponse response = execution.execute(request, body);
        Instant finish = Instant.now();
        Duration timeElapsed = Duration.between(start, finish);
        traceResponse(response, timeElapsed);
        storageMeasure(request, response, timeElapsed);
        return response;
    }

    public void storageMeasure(HttpRequest request, ClientHttpResponse response, Duration timeElapsed) {
        try {
            BeanUtil.publishEvent(new OnRequestEvent(
                    new RestClientMeasure(request.getURI().toString(),
                            request.getURI().getHost(),
                            request.getURI().getPath(),
                            request.getMethodValue(),
                            (short)response.getStatusCode().value(),
                            timeElapsed.toMillis(),
                            dateTimeProvider.nowDateTime())));
        } catch (IOException e) {
            log.error("Error saving measures", e);
        }
        log.debug("continue");
    }

    private void traceRequest(HttpRequest request){
		log.debug("===========================Request begin================================================");
        log.debug("URI         : {}", request.getURI());
        log.debug("Method      : {}", request.getMethod());
        log.debug("===========================Request end================================================");
    }

    private void traceResponse(ClientHttpResponse response, Duration timeElapsed) throws IOException {
        log.debug("============================Response begin==========================================");
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Elapsed time : {}", timeElapsed);
        log.debug("============================Response end=================================================");
    }

}
