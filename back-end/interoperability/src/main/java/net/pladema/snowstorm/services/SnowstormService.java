package net.pladema.snowstorm.services;


import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface SnowstormService {

    SnowstormSearchResponse getConcepts(String term, String ecl) throws SnowstormApiException;

    SnowstormSearchResponse getConceptsByEclKey(String eclKey, String searchAfter) throws SnowstormApiException;

    SnowstormSearchResponse getConceptsByEcl(String ecl) throws SnowstormApiException;

    List<SnowstormItemResponse> getConceptParents(String conceptId) throws SnowstormApiException;

    List<SnowstormItemResponse> getConceptAncestors(String conceptId) throws SnowstormApiException;

    <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) throws SnowstormApiException;

    ResponseEntity<SnowstormSearchResponse> status();

	SnowstormSearchResponse getConcepts(List<Integer> ids) throws SnowstormApiException;

	SnowstormItemResponse getConceptById(String conceptId) throws SnowstormApiException;

}
