package net.pladema.imagenetwork.infrastructure.output.rest;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.PacsCommunicationPort;
import net.pladema.imagenetwork.domain.PacsBo;
import net.pladema.imagenetwork.infrastructure.input.rest.exception.PacsResponseErrorHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class RestPacsCommunicationPortImpl implements PacsCommunicationPort {

    private static final String pathContextToHealthcheckPACS = "/public/pac";

    private final RestTemplate restTemplate;

    public RestPacsCommunicationPortImpl(HttpClientConfiguration configuration) throws Exception {
        this.restTemplate = new RestTemplateSSL(configuration);
        restTemplate.setErrorHandler(new PacsResponseErrorHandler());
    }

    @Override
    public PacsBo doHealthcheckProof(PacsBo pacsBo) {
        URI pacURI = pacsBo.getURI();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(pacURI.toString())
                .path(pathContextToHealthcheckPACS);

        try {
            var status = restTemplate.exchange(
                    uriBuilder.build().toUri(),
                    HttpMethod.GET,
                    HttpEntity.EMPTY, Object.class).getStatusCode();
            if (status.isError())
                throw new RestClientException(status.getReasonPhrase());
            return pacsBo;

        } catch(RestClientException e) {
            log.warn("Falló prueba de healthcheck para el PACS {}", pacURI);
            return null;
        }
    }

}
