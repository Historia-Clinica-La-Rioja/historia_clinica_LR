package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;

public interface SnowstormService {

    public SnowstormSearchResponse getConcepts(String term, String ecl);

}
