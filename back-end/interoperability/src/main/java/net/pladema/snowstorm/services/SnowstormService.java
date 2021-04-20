package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;

import java.util.List;

public interface SnowstormService {

    SnowstormSearchResponse getConcepts(String term, String ecl);

    List<SnowstormItemResponse> getConceptAncestors(String conceptId);

    SnowstormCie10RefsetMembersResponse getCie10RefsetMembers(String referencedComponentId);

}
