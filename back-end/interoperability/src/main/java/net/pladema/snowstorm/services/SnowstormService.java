package net.pladema.snowstorm.services;


import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

import java.util.List;

public interface SnowstormService {

    SnowstormSearchResponse getConcepts(String term, String ecl) throws SnowstormApiException;

    SnowstormSearchResponse getConcepts(String ecl) throws SnowstormApiException;

    List<SnowstormItemResponse> getConceptAncestors(String conceptId) throws SnowstormApiException;

    <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) throws SnowstormApiException;

}
