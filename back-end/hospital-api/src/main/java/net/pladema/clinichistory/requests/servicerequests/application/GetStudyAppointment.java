package net.pladema.clinichistory.requests.servicerequests.application;

import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.StudyAppointmentReportStorage;


import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetStudyAppointment {

	private final StudyAppointmentReportStorage studyAppointmentReportStorage;

	public StudyAppointmentBo run(Integer appointmentId) {
		return studyAppointmentReportStorage.getStudyByAppointment(appointmentId);
	}
}
