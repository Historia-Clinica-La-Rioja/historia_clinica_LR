package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;

public interface SharedReferencePort {

	void closeReference(ReferenceClosureDto counterReference, Integer institutionId, Integer patientId);

	void validateReference(ReferenceClosureDto counterReference, Integer institutionId, Integer patientId);
}
