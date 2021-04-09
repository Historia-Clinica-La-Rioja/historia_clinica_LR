package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.SnowstormCie10RefsetMembersResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;

public interface SnowstormService {

    public SnowstormSearchResponse getConcepts(String term, String ecl);

    public SnowstormCie10RefsetMembersResponse getCie10RefsetMembers(String referencedComponentId);

}
