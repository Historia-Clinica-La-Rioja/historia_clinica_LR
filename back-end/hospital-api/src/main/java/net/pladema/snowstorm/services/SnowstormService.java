package net.pladema.snowstorm.services;

import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;

import java.util.List;

public interface SnowstormService {

    public SnowstormSearchResponse getConcepts(String term, String ecl);

}
