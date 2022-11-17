package net.pladema.snowstorm.services.impl;

import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import ar.lamansys.sgx.shared.restclient.services.RestClient;
import ar.lamansys.sgx.shared.restclient.services.RestClientInterface;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.pladema.snowstorm.configuration.SnowstormAuthInterceptor;
import net.pladema.snowstorm.configuration.SnowstormRestTemplateAuth;
import net.pladema.snowstorm.configuration.SnowstormWSConfig;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormConcept;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.exceptions.SnowstormEnumException;
import net.pladema.snowstorm.services.exceptions.SnowstormStatusException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Map;


@Service
public class SnowstormServiceImpl implements SnowstormService {

	private static final String RESPUESTA_NULA = "Respuesta nula";

	public static final String OVER_300000_RESULTS = "There are over 300000 results. Aggregating these results would be too costly.";

	private final SnowstormWSConfig snowstormWSConfig;

    private final SnomedSemantics snomedSemantics;

    private final RestClientInterface restClientInterface;

    public SnowstormServiceImpl(SnowstormRestTemplateAuth restTemplateSSL,
                                SnowstormWSConfig wsConfig,
                                SnomedSemantics snomedSemantics) {
        super();
        this.snowstormWSConfig = wsConfig;
        this.snomedSemantics = snomedSemantics;
        this.restClientInterface = new RestClient(restTemplateSSL, wsConfig);
    }

    @Override
    public SnowstormSearchResponse getConcepts(String term, String eclKey) throws SnowstormApiException {

        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=").append(snowstormWSConfig.getTermActive());
        urlWithParams.append("&limit=").append(snowstormWSConfig.getConceptsLimit());

        for (Long preferredOrAcceptableIn : snowstormWSConfig.getPreferredOrAcceptableIn()) {
            if (preferredOrAcceptableIn != null) {
                urlWithParams.append("&preferredOrAcceptableIn=").append(preferredOrAcceptableIn);
            }
        }

        urlWithParams.append("&term=").append(term);

        if (eclKey != null) {
            var snomedEcl = SnomedECL.map(eclKey);
            urlWithParams.append("&ecl=").append(snomedSemantics.getEcl(snomedEcl));
        }

        SnowstormSearchResponse result = getSnowstormSearchResponse(urlWithParams);
        return result;
    }

    @Override
    public List<SnowstormItemResponse> getConceptParents(String conceptId) throws SnowstormApiException {
        String urlWithParams = snowstormWSConfig.getBrowserConceptUrl()
                .concat("/")
                .concat(conceptId)
                .concat("/parents")
                .concat("?form=inferred");
        ResponseEntity<SnowstormConcept> response;
        try {
            response = restClientInterface.exchangeGet(urlWithParams, SnowstormConcept.class);
        } catch (RestTemplateApiException e) {
            throw mapException(e);
        }
        SnowstormConcept result = response.getBody();
        if (result == null)
            throw new SnowstormApiException(SnowstormEnumException.NULL_RESPONSE, response.getStatusCode(), RESPUESTA_NULA);
        return result.getItems();
    }

    @Override
    public List<SnowstormItemResponse> getConceptAncestors(String conceptId) throws SnowstormApiException {
        String urlWithParams = snowstormWSConfig.getBrowserConceptUrl()
                .concat("/")
                .concat(conceptId)
                .concat("/ancestors")
                .concat("?form=inferred");
        ResponseEntity<SnowstormConcept> response;
        try {
            response = restClientInterface.exchangeGet(urlWithParams, SnowstormConcept.class);
        } catch (RestTemplateApiException e) {
            throw mapException(e);
        }
        SnowstormConcept result = response.getBody();
        if (result == null)
            throw new SnowstormApiException(SnowstormEnumException.NULL_RESPONSE, response.getStatusCode(), RESPUESTA_NULA);
        return result.getItems();
    }

    public <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) throws SnowstormApiException {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getRefsetMembersUrl());

        urlWithParams.append("?referenceSet=" + referenceSetId);
        urlWithParams.append("&referencedComponentId=" + referencedComponentId);
        urlWithParams.append("&active=true");
        urlWithParams.append("&offset=0");
        urlWithParams.append("&limit=" + limit);

        ResponseEntity<T> response;
        try {
            response = restClientInterface.exchangeGet(urlWithParams.toString(), type);
        } catch (RestTemplateApiException e) {
            throw mapException(e);
        }
        T result = response.getBody();
        if (result == null)
            throw new SnowstormApiException(SnowstormEnumException.NULL_RESPONSE, response.getStatusCode(), RESPUESTA_NULA);
        return result;
    }

    @Override
    public SnowstormSearchResponse getConceptsByEclKey(String eclKey, String searchAfter) throws SnowstormApiException {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=" + snowstormWSConfig.getTermActive());
		urlWithParams.append("&limit=" + snowstormWSConfig.getConceptsLimit());

        if (searchAfter != null) {
            urlWithParams.append("&searchAfter=" + searchAfter);
        }

        if (eclKey != null) {
            var snomedEcl = SnomedECL.map(eclKey);
            urlWithParams.append("&ecl=").append(snomedSemantics.getEcl(snomedEcl));
        }

        SnowstormSearchResponse result = getSnowstormSearchResponse(urlWithParams);
        return result;
    }

    @Override
    public SnowstormSearchResponse getConceptsByEcl(String ecl) throws SnowstormApiException {
        StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getConceptsUrl());

        urlWithParams.append("?termActive=" + snowstormWSConfig.getTermActive());
        urlWithParams.append("&ecl=" + ecl);
        SnowstormSearchResponse result = getSnowstormSearchResponse(urlWithParams);
        return result;
    }

    private SnowstormSearchResponse getSnowstormSearchResponse(StringBuilder urlWithParams) throws SnowstormApiException {
        ResponseEntity<SnowstormSearchResponse> response;
        try {
            response = restClientInterface.exchangeGet(urlWithParams.toString(), SnowstormSearchResponse.class);
        } catch (RestTemplateApiException e) {
            throw mapException(e);
        } catch (ResourceAccessException e) {
			throw new SnowstormApiException(SnowstormEnumException.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, "Se venció el tiempo de lectura de la respuesta del servidor de Snowstorm.");
		}
		SnowstormSearchResponse result = response.getBody();
        if (result == null)
            throw new SnowstormApiException(SnowstormEnumException.NULL_RESPONSE, response.getStatusCode(), RESPUESTA_NULA);
        return result;
    }

    @Override
    public ResponseEntity<SnowstormSearchResponse> status() {
        try {
            return restClientInterface.exchangeGet(snowstormWSConfig.getConceptsUrl(), SnowstormSearchResponse.class);
        } catch (Exception e) {
            throw new SnowstormStatusException(e);
        }
    }

	@Override
	public SnowstormSearchResponse getConcepts(List<Integer> ids) throws SnowstormApiException {

		StringBuilder urlWithParams = new StringBuilder(snowstormWSConfig.getBrowserConceptUrl()).append("?");

		for (Integer id: ids){
			urlWithParams.append("conceptIds=").append((long) id).append("&");
		}
        
		urlWithParams.append("size=").append(snowstormWSConfig.getConceptsLimit());

		SnowstormSearchResponse result = getSnowstormSearchResponse(urlWithParams);
		return result;
	}

	@Override
	public SnowstormItemResponse getConceptById(String conceptId) throws SnowstormApiException {
		String urlWithParams = new String(snowstormWSConfig.getBrowserConceptUrl()
				.concat("/")
				.concat(conceptId)
				.concat("?form=inferred"));
		ResponseEntity<SnowstormItemResponse> response;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Accept-Language", snowstormWSConfig.getLanguage());
			response = restClientInterface.exchangeGet(urlWithParams, headers, SnowstormItemResponse.class);
		} catch (RestTemplateApiException e) {
			throw mapException(e);
		}
		SnowstormItemResponse result = response.getBody();
		return result;
	}

	private SnowstormApiException mapException(RestTemplateApiException apiException) {
        if (apiException.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR)
            return new SnowstormApiException(SnowstormEnumException.SERVER_ERROR, apiException.getStatusCode(), "El servicio de Snowstorm tiene un error interno.");
        if (apiException.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            if (apiException.getStatusCode() == HttpStatus.REQUEST_TIMEOUT)
                return new SnowstormApiException(SnowstormEnumException.API_TIMEOUT, apiException.getStatusCode(), "El servicio de Snowstorm esta tardando en responder.");
            if (apiException.getStatusCode() == HttpStatus.NOT_FOUND)
                return new SnowstormApiException(SnowstormEnumException.NOT_FOUND, apiException.getStatusCode(), "El servicio consultado no existe.");
            if (apiException.getStatusCode() == HttpStatus.BAD_REQUEST)
                return new SnowstormApiException(SnowstormEnumException.BAD_REQUEST, apiException.getStatusCode(), "No se están cumpliendo con los requisitos de Snowstorm.");
			if (apiException.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					String message = ((Map<String, String>)mapper.readValue(apiException.getBody(), Map.class)).get("message");
					if (message.equals(OVER_300000_RESULTS))
						return new SnowstormApiException(SnowstormEnumException.UNPROCESSABLE_ENTITY, apiException.getStatusCode(), "Hay más de 300000 resultados. Por favor refine la búsqueda.");
				} catch (JsonProcessingException ignored) {}
			}
		}
        return new SnowstormApiException(SnowstormEnumException.UNKNOWN_ERROR, apiException.getStatusCode(), "Estado de error desconocido");
    }

}
