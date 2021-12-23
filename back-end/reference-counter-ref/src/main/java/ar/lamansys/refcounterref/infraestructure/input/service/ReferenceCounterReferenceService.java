package ar.lamansys.refcounterref.infraestructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ReferenceCounterReferenceFileDto;

import java.util.List;

public interface ReferenceCounterReferenceService {

    List<ReferenceCounterReferenceFileDto> getReferenceFiles(Integer referenceId);

    CounterReferenceSummaryDto getCounterReference(Integer referenceId);
}