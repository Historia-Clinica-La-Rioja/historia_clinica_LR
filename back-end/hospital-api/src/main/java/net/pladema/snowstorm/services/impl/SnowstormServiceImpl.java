package net.pladema.snowstorm.services.impl;

import net.pladema.sgx.restclient.services.RestClient;
import net.pladema.snowstorm.configuration.SnowstormRestTemplateAuth;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        urlWithParams.append("&limit=" + snowstormWSConfig.getLimit());

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
}
