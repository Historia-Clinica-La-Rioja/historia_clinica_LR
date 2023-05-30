package net.pladema.clinichistory.requests.servicerequests.application;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.StudyAppointmentReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.InformerObservationBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateDraftStudyAppointmentReport {

	private final StudyAppointmentReportStorage studyAppointmentReportStorage;

	public Long execute(Integer appointmentId, InformerObservationBo informerObservation) {
		return studyAppointmentReportStorage.updateDraftReport(appointmentId, informerObservation);
	}
}
