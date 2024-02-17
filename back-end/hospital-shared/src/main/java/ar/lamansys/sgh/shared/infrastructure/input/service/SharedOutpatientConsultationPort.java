package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedOutpatientConsultationPort {

	List<OutpatientConsultationDto> getOutpatientConsultationsToCipres();

}
