package net.pladema.snowstorm.services.impl;

import ar.lamansys.sgx.restclient.services.RestClient;
import net.pladema.snowstorm.configuration.SnowstormRestTemplateAuth;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import net.pladema.snowstorm.services.domain.SnowstormConcept;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.pladema.snowstorm.services.exceptions.SnowstormEnumException.SNOWSTORM_TIMEOUT_SERVICE;

@Service
public class SnowstormServiceImpl extends RestClient implements SnowstormService {

    public static final String FAIL_COMMUNICATION = "Fallo la comunicación con el servidor de snowstorm -> %s";
    public static final String SNOWSTORM_FAIL_SERVICE = "Snowstorm fail service ";
    private final Logger logger;

    private final SnowstormWSConfig snowstormWSConfig;

    public SnowstormServiceImpl(SnowstormRestTemplateAuth restTemplateSSL, SnowstormWSConfig wsConfig) {
        super(restTemplateSSL, wsConfig);
        this.snowstormWSConfig = wsConfig;
        logger = LoggerFactory.getLogger(CalculateCie10CodesServiceImpl.class);
    }

    @Override
    public SnowstormSearchResponse getConcepts(String term, String ecl) {

        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=" + snowstormWSConfig.getTermActive());
        urlWithParams.append("&limit=" + snowstormWSConfig.getConceptsLimit());

        for (Long preferredOrAcceptableIn : snowstormWSConfig.getPreferredOrAcceptableIn()) {
            if (preferredOrAcceptableIn != null) {
                urlWithParams.append("&preferredOrAcceptableIn=" + preferredOrAcceptableIn);
            }
        }

        urlWithParams.append("&term=" + term);

        if (ecl != null) {
            urlWithParams.append("&ecl=" + ecl);
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

    @Override
    public SnowstormCie10RefsetMembersResponse getCie10RefsetMembers(String referencedComponentId) {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getRefsetMembersUrl());

        urlWithParams.append("?referenceSet=" + SnowstormWSConfig.CIE10_REFERENCE_SET_ID);
        urlWithParams.append("&referencedComponentId=" + referencedComponentId);
        urlWithParams.append("&active=true");
        urlWithParams.append("&offset=0");
        urlWithParams.append("&limit=" + SnowstormWSConfig.CIE10_LIMIT);

        SnowstormCie10RefsetMembersResponse result;
        try {
            ResponseEntity<SnowstormCie10RefsetMembersResponse> response = exchangeGet(urlWithParams.toString(), SnowstormCie10RefsetMembersResponse.class);
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
}
