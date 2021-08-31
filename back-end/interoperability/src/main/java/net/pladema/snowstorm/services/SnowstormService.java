package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;

import java.util.List;

public interface SnowstormService {

    SnowstormSearchResponse getConcepts(String term, String ecl);

    SnowstormSearchResponse getConcepts(String ecl);

    List<SnowstormItemResponse> getConceptAncestors(String conceptId);

    <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type);

}
