package net.pladema.imagenetwork.application.getfileinfo.getfileuuid;

import ar.lamansys.sgx.shared.restclient.configuration.HttpClientConfiguration;
import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.RestTemplateSSL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.application.exception.StudyExceptionEnum;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenJWT;
import net.pladema.imagenetwork.domain.StudyFileInfoBo;
import net.pladema.imagenetwork.domain.filters.StudyInstanceFilter;
import net.pladema.imagenetwork.infrastructure.input.rest.exception.PacsResponseErrorHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Service
public class GetFileUuid {

    private final GenerateStudyTokenJWT generateStudyTokenJWT;
    private final RestTemplate restTemplate;
    private static final String pathContextToFindInfo = "/tools/find";
    private final ObjectMapper objectMapper;

    public GetFileUuid(GenerateStudyTokenJWT generateStudyTokenJWT,
                       HttpClientConfiguration configuration,
                       ObjectMapper objectMapper) throws Exception {
        this.generateStudyTokenJWT = generateStudyTokenJWT;
        this.restTemplate = new RestTemplateSSL(configuration);
        restTemplate.setErrorHandler(new PacsResponseErrorHandler());
        this.objectMapper = objectMapper;
    }

    public StudyFileInfoBo run(String studyInstanceUID, List<String> urls) throws JsonProcessingException {
        log.debug("Input parameters -> studyInstanceUID {}", studyInstanceUID);


        String token = generateStudyTokenJWT.run(studyInstanceUID);
        var body = objectMapper.writeValueAsString(new StudyInstanceFilter(studyInstanceUID));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(APPLICATION_JSON);
        headers.setContentLength(body.length());

        var result = urls.stream()
                .map(pacUrl -> getFileUUIDFromPACS(pacUrl, headers, body))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new StudyException(StudyExceptionEnum.ANY_FILEUUID_WAS_FOUND, "app.imagenetwork.error.any-file-uuid-was-found"));

        result.setToken(token);

        log.debug("Output -> studyInstanceUID {} studyFileInfoBo {}", studyInstanceUID, result);
        return result;

    }

    private StudyFileInfoBo getFileUUIDFromPACS(String pacUrl, HttpHeaders headers, String body) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(pacUrl)
                .path(pathContextToFindInfo);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            String[] response = restTemplate.exchange(
                            uriBuilder.build().toUri(),
                            HttpMethod.POST,
                            entity,
                            String[].class)
                    .getBody();

            if (response == null || response.length == 0) {
                log.warn("No se obtuvieron resultados para el PACS {}", pacUrl);
                return null;
            }
            if (response.length > 1)
                log.warn("No se obtuvo un único file-uuid en el PACS {}", pacUrl);

            return new StudyFileInfoBo(pacUrl, response[0]);

        } catch(RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

}
