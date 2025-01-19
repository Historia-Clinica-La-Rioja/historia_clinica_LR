package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.interoperability.cipres.CipresOutpatientConsultationDto;

public interface SharedCipresEncounterPort {

	void forwardOutpatientConsultation(CipresOutpatientConsultationDto createOutpatientDto, Integer cipresEncounterId);

}
