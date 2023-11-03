package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceStudyDto;

public interface SharedServiceRequestPort {

	Integer create(CompleteReferenceStudyDto study);

}
