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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
        this.objectMapper = objectMapper;
    }

    public StudyFileInfoBo run(String studyInstanceUID, String pacUrl) throws JsonProcessingException {
        log.debug("Input parameters -> studyInstanceUID {} pacUrl {}", studyInstanceUID, pacUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateStudyTokenJWT.run(studyInstanceUID));
        headers.setContentType(APPLICATION_JSON);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(pacUrl)
                .path(pathContextToFindInfo);

        var body = objectMapper.writeValueAsString(new StudyInstanceFilter(studyInstanceUID));
        headers.setContentLength(body.length());

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String[] response = restTemplate.exchange(
                uriBuilder.build().toUri(),
                HttpMethod.POST,
                entity,
                String[].class)
                .getBody();

        assertContextValidResponse(response);
        if (response.length > 1)
            log.warn("No se obtuvo un único file-uuid en este PACS {}, para el estudio {}", pacUrl, studyInstanceUID);

        var result = new StudyFileInfoBo(response[0]);

        log.debug("Output -> studyInstanceUID {} studyFileInfoBo {}", studyInstanceUID, result);
        return result;

    }

    private void assertContextValidResponse(String[] result) {
        if (result == null || result.length == 0)
            throw new StudyException(StudyExceptionEnum.FILEUUID_NOT_FOUND, "app.imagenetwork.error.file-uuid_not_found");
    }
}
