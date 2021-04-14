package net.pladema.snowstorm.services.impl;

import ar.lamansys.sgx.restclient.services.RestClient;
import net.pladema.snowstorm.configuration.SnowstormRestTemplateAuth;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import net.pladema.snowstorm.services.domain.SnowstormConcept;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SnowstormServiceImpl extends RestClient implements SnowstormService {

    private SnowstormWSConfig snowstormWSConfig;

    public SnowstormServiceImpl(SnowstormRestTemplateAuth restTemplateSSL, SnowstormWSConfig wsConfig) {
        super(restTemplateSSL, wsConfig);
        this.snowstormWSConfig = wsConfig;
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

        ResponseEntity<SnowstormSearchResponse> response = exchangeGet(urlWithParams.toString(), SnowstormSearchResponse.class);
        return response.getBody();
    }

    @Override
    public List<SnowstormItemResponse> getConceptAncestors(String conceptId) {
        String urlWithParams = snowstormWSConfig.getBrowserConceptUrl()
                .concat(conceptId)
                .concat("/ancestors")
                .concat("?form=inferred");
        ResponseEntity<SnowstormConcept> entityResponse = exchangeGet(urlWithParams, SnowstormConcept.class);
        if(entityResponse.getBody() != null)
            return entityResponse.getBody().getItems();
        return Collections.emptyList();
    }

    @Override
    public SnowstormCie10RefsetMembersResponse getCie10RefsetMembers(String referencedComponentId) {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getRefsetMembersUrl());

        urlWithParams.append("?referenceSet=" + SnowstormWSConfig.CIE10_REFERENCE_SET_ID);
        urlWithParams.append("&referencedComponentId=" + referencedComponentId);
        urlWithParams.append("&active=true");
        urlWithParams.append("&offset=0");
        urlWithParams.append("&limit=" + SnowstormWSConfig.CIE10_LIMIT);

        ResponseEntity<SnowstormCie10RefsetMembersResponse> response = exchangeGet(urlWithParams.toString(), SnowstormCie10RefsetMembersResponse.class);
        return response.getBody();
    }
}
