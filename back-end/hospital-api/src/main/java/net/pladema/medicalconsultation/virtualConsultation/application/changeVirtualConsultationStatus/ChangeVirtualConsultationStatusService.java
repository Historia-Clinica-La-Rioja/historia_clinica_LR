package net.pladema.medicalconsultation.virtualConsultation.application.changeVirtualConsultationStatus;

import net.pladema.medicalconsultation.virtualConsultation.domain.enums.EVirtualConsultationStatus;

public interface ChangeVirtualConsultationStatusService {

	Boolean run(Integer virtualConsultationId, EVirtualConsultationStatus status);

}
