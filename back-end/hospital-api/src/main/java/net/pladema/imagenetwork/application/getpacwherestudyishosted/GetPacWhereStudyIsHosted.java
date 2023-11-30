package net.pladema.imagenetwork.application.getpacwherestudyishosted;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.application.port.StudyPacAssociationStorage;
import net.pladema.imagenetwork.domain.PacsListBo;
import net.pladema.imagenetwork.infrastructure.input.rest.exception.PacsResponseErrorHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GetPacWhereStudyIsHosted {

    private final StudyPacAssociationStorage studyStorage;
    private final RestTemplate restTemplate;
    private static final String pathContextToHealthcheckPACS = "/public/pac";

    public GetPacWhereStudyIsHosted(StudyPacAssociationStorage studyStorage,
                                    HttpClientConfiguration configuration) throws Exception {
        this.studyStorage = studyStorage;
        this.restTemplate = new RestTemplateSSL(configuration);
        restTemplate.setErrorHandler(new PacsResponseErrorHandler());
    }

    public PacsListBo run(String studyInstanceUID) throws MalformedURLException {
        log.debug("Get PAC URL where the study '{}' is hosted", studyInstanceUID);
        List<PacServer> pacServers = studyStorage.getPacServersBy(studyInstanceUID);
        var result = this.filterUnrecheablePACS(new PacsListBo(pacServers));
        log.debug("Output -> {}", result);
        return result;
    }

    private PacsListBo filterUnrecheablePACS(PacsListBo pacs) {
        pacs.setUrls(pacs.getUrls().stream()
                .map(this::doHealthcheckProof)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        return pacs;
    }

    private URI doHealthcheckProof(URI pacURI) {
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
            return pacURI;

        } catch(RestClientException e) {
            log.warn("Falló prueba de healthcheck para el PACS {}", pacURI);
            return null;
        }
    }
}
