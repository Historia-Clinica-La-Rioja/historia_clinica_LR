package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.interoperability.cipres.CipresOutpatientConsultationDto;

import java.util.List;

public interface SharedOutpatientConsultationPort {

	List<CipresOutpatientConsultationDto> getOutpatientConsultationsToCipres();

}
