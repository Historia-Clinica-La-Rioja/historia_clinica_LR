package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;

import java.util.List;

public interface SharedReferenceCounterReference {

    List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId);

    CounterReferenceSummaryDto getCounterReference(Integer referenceId);

    void saveReferences(Integer encounterId, Integer sourceTypeId, List<ReferenceDto> refrenceDto);

}
