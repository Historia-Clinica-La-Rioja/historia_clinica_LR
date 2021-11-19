package net.pladema.snowstorm.services;


import net.pladema.snowstorm.controller.dto.SnvsToReportDto;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import net.pladema.snowstorm.services.domain.SnowstormItemResponse;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.domain.SnvsEventRegisterResponse;
import net.pladema.snowstorm.services.domain.SnvsToReportBo;

import java.util.List;

public interface SnowstormService {

    SnowstormSearchResponse getConcepts(String term, String ecl) throws SnowstormApiException;

    SnowstormSearchResponse getConcepts(String ecl) throws SnowstormApiException;

    List<SnowstormItemResponse> getConceptAncestors(String conceptId) throws SnowstormApiException;

    List<ManualClassificationBo> isSnvsReportable(String sctid, String pt);

    List<SnvsToReportBo> tryReportAndSave(List<SnvsToReportDto> toReportList);

    SnvsEventRegisterResponse snvsEventRegister(SnvsToReportBo toReport);

    <T> T getRefsetMembers(String referencedComponentId, String referenceSetId, String limit, Class<T> type) throws SnowstormApiException;

}
