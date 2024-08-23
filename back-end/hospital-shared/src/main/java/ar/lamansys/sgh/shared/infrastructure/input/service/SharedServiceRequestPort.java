package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceStudyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceServiceRequestProcedureDto;

import java.util.List;

public interface SharedServiceRequestPort {

	Integer create(CompleteReferenceStudyDto study);

	List<ReferenceServiceRequestProcedureDto> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds);

	void cancelServiceRequest(Integer serviceRequestId);

	List<SharedSnomedDto> getMostFrequentStudies(Integer professionalId, Integer institutionId, Integer limit);

}
