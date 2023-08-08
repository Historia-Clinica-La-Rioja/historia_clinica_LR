package ar.lamansys.virtualConsultation.application.changeVirtualConsultationStatus;

import ar.lamansys.virtualConsultation.domain.enums.EVirtualConsultationStatus;

public interface ChangeVirtualConsultationStatusService {

	Boolean run(Integer virtualConsultationId, EVirtualConsultationStatus status);

}
