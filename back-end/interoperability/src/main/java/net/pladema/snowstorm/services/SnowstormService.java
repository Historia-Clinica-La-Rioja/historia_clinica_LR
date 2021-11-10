package net.pladema.snowstorm.services;


import ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception.RestTemplateApiException;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;

import java.util.List;

public interface SnowstormService {

    SnowstormSearchResponse getConcepts(String term, String ecl) throws RestTemplateApiException;

    SnowstormSearchResponse getConcepts(String ecl) throws RestTemplateApiException ;
    List<SnowstormItemResponse> getConceptAncestors(String conceptId) throws RestTemplateApiException ;

    List<ManualClassificationBo> isSnvsReportable(String sctid, String pt);

    <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) throws RestTemplateApiException ;

}
