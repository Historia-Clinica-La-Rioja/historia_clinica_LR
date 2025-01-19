package net.pladema.imagenetwork.infrastructure.input.rest.exception;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
public class PacsResponseErrorHandler  extends DefaultResponseErrorHandler {

    @Override
    public void handleError(@NonNull ClientHttpResponse httpResponse) throws IOException {
        try {
            super.handleError(httpResponse);
        } catch (HttpStatusCodeException e) {
            var body = e.getResponseBodyAsString();
            log.warn("status: {}, body error: {}", e.getStatusCode(), body);
        }
    }
}