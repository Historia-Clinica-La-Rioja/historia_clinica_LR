package net.pladema.snowstorm.services.impl;

import ar.lamansys.sgx.shared.restclient.services.RestClient;
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
import net.pladema.snowstorm.services.exceptions.SnowstormTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static net.pladema.snowstorm.services.exceptions.SnowstormEnumException.SNOWSTORM_TIMEOUT_SERVICE;

@Service
public class SnowstormServiceImpl extends RestClient implements SnowstormService {

    public static final String FAIL_COMMUNICATION = "Fallo la comunicación con el servidor de snowstorm -> %s";
    public static final String SNOWSTORM_FAIL_SERVICE = "Snowstorm fail service ";
    public static final String OUTPUT = "Output -> {}";
    private final Logger logger;

    private final SnowstormWSConfig snowstormWSConfig;

    private final SnomedSemantics snomedSemantics;

    private final SnowstormRepository snowstormRepository;

    public SnowstormServiceImpl(SnowstormRestTemplateAuth restTemplateSSL,
                                SnowstormWSConfig wsConfig,
                                SnomedSemantics snomedSemantics,
                                SnowstormRepository snowstormRepository) {
        super(restTemplateSSL, wsConfig);
        this.snowstormWSConfig = wsConfig;
        this.snomedSemantics = snomedSemantics;
        this.snowstormRepository = snowstormRepository;
        logger = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    }

    @Override
    public SnowstormSearchResponse getConcepts(String term, String eclKey) {

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

        SnowstormSearchResponse result;
        try {
            ResponseEntity<SnowstormSearchResponse> response = exchangeGet(urlWithParams.toString(), SnowstormSearchResponse.class);
            result = response.getBody();
            if (result == null)
                throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        } catch (Exception e) {
            logger.error(SNOWSTORM_FAIL_SERVICE, e);
            throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        }
        return result;
    }

    @Override
    public List<SnowstormItemResponse> getConceptAncestors(String conceptId) {
        String urlWithParams = snowstormWSConfig.getBrowserConceptUrl()
                .concat(conceptId)
                .concat("/ancestors")
                .concat("?form=inferred");

        SnowstormConcept result;
        try {
            ResponseEntity<SnowstormConcept> response = exchangeGet(urlWithParams, SnowstormConcept.class);
            result = response.getBody();
            if (result == null)
                throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        } catch (Exception e) {
            logger.error(SNOWSTORM_FAIL_SERVICE, e);
            throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        }
        return result.getItems();
    }

    public <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getRefsetMembersUrl());

        urlWithParams.append("?referenceSet=" + referenceSetId);
        urlWithParams.append("&referencedComponentId=" + referencedComponentId);
        urlWithParams.append("&active=true");
        urlWithParams.append("&offset=0");
        urlWithParams.append("&limit=" + limit);

        T result;
        try {
            ResponseEntity<T> response = exchangeGet(urlWithParams.toString(), type);
            result = response.getBody();
            if (result == null)
                throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        } catch (Exception e) {
            logger.error(SNOWSTORM_FAIL_SERVICE, e);
            throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        }
        return result;
    }

    @Override
    public SnowstormSearchResponse getConcepts(String ecl) {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=" + snowstormWSConfig.getTermActive());
        urlWithParams.append("&ecl=" + ecl);

        SnowstormSearchResponse result;
        try {
            ResponseEntity<SnowstormSearchResponse> response = exchangeGet(urlWithParams.toString(), SnowstormSearchResponse.class);
            result = response.getBody();
            if (result == null)
                throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        } catch (Exception e) {
            logger.error(SNOWSTORM_FAIL_SERVICE, e);
            throw new SnowstormTimeoutException(SNOWSTORM_TIMEOUT_SERVICE, String.format(FAIL_COMMUNICATION, snowstormWSConfig.getBaseUrl()+urlWithParams));
        }
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
