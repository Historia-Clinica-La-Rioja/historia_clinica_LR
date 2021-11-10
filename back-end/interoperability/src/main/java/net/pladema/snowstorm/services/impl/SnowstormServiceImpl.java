package net.pladema.snowstorm.services.impl;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;
import net.pladema.snowstorm.configuration.SnowstormRestTemplateAuth;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.repository.SnowstormRepository;
import net.pladema.snowstorm.repository.entity.ManualClassification;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snowstorm.services.domain.SnowstormConcept;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SnowstormServiceImpl implements SnowstormService {

    public static final String FAIL_COMMUNICATION = "Fallo la comunicación con el servidor de snowstorm -> %s";
    public static final String OUTPUT = "Output -> {}";
    private final Logger logger;

    private final SnowstormWSConfig snowstormWSConfig;

    private final SnomedSemantics snomedSemantics;

    private final SnowstormRepository snowstormRepository;

    private final RestClientInterface restClientInterface;

    public SnowstormServiceImpl(SnowstormRestTemplateAuth restTemplateSSL,
                                SnowstormWSConfig wsConfig,
                                SnomedSemantics snomedSemantics,
                                SnowstormRepository snowstormRepository) {
        this.snowstormWSConfig = wsConfig;
        this.snomedSemantics = snomedSemantics;
        this.snowstormRepository = snowstormRepository;
        this.restClientInterface = new RestClient(restTemplateSSL, wsConfig);
        this.logger = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    }

    @Override
    public SnowstormSearchResponse getConcepts(String term, String eclKey) throws RestTemplateApiException {

        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=" + snowstormWSConfig.getTermActive());
        urlWithParams.append("&limit=" + snowstormWSConfig.getConceptsLimit());

        for (Long preferredOrAcceptableIn : snowstormWSConfig.getPreferredOrAcceptableIn()) {
            if (preferredOrAcceptableIn != null) {
                urlWithParams.append("&preferredOrAcceptableIn=" + preferredOrAcceptableIn);
            }
        }

        urlWithParams.append("&term=" + term);

        if (eclKey != null) {
            var snomedEcl = SnomedECL.map(eclKey);
            urlWithParams.append("&ecl=" + snomedSemantics.getEcl(snomedEcl));
        }
        ResponseEntity<SnowstormSearchResponse> response = restClientInterface.exchangeGet(urlWithParams.toString(), SnowstormSearchResponse.class);
        SnowstormSearchResponse result = response.getBody();
        if (result == null)
            throw new RestTemplateApiException(HttpStatus.OK, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        return result;
    }

    @Override
    public List<SnowstormItemResponse> getConceptAncestors(String conceptId) throws RestTemplateApiException {
        String urlWithParams = snowstormWSConfig.getBrowserConceptUrl()
                .concat(conceptId)
                .concat("/ancestors")
                .concat("?form=inferred");

        SnowstormConcept result;

        ResponseEntity<SnowstormConcept> response = restClientInterface.exchangeGet(urlWithParams, SnowstormConcept.class);
        result = response.getBody();
        if (result == null)
            throw new RestTemplateApiException(HttpStatus.OK, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));

        return result.getItems();
    }

    public <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) throws RestTemplateApiException {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getRefsetMembersUrl());

        urlWithParams.append("?referenceSet=" + referenceSetId);
        urlWithParams.append("&referencedComponentId=" + referencedComponentId);
        urlWithParams.append("&active=true");
        urlWithParams.append("&offset=0");
        urlWithParams.append("&limit=" + limit);

        T result;

        ResponseEntity<T> response = restClientInterface.exchangeGet(urlWithParams.toString(), type);
        result = response.getBody();
        if (result == null)
            throw new RestTemplateApiException(HttpStatus.OK, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));

        return result;
    }

    @Override
    public SnowstormSearchResponse getConcepts(String ecl) throws RestTemplateApiException {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=" + snowstormWSConfig.getTermActive());
        urlWithParams.append("&ecl=" + ecl);

        SnowstormSearchResponse result;

        ResponseEntity<SnowstormSearchResponse> response = restClientInterface.exchangeGet(urlWithParams.toString(), SnowstormSearchResponse.class);
        result = response.getBody();
        if (result == null)
            throw new RestTemplateApiException(HttpStatus.OK, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));

        return result;
    }

    @Override
    public List<ManualClassificationBo> isSnvsReportable(String sctid, String pt) {
        logger.debug("Input parameters -> sctid {}, pt {}", sctid, pt);
        List<ManualClassification> resultQuery = snowstormRepository.isSnvsReportable(sctid, pt);
        List<ManualClassificationBo> result = resultQuery.stream().map(ManualClassificationBo::new).collect(Collectors.toList());
        logger.debug(OUTPUT, result);
        return result;
    }
}
