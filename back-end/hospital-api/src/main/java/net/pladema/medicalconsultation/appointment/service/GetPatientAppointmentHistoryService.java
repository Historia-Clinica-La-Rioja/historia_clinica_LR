package net.pladema.medicalconsultation.appointment.service;

import net.pladema.medicalconsultation.appointment.service.domain.PatientAppointmentHistoryBo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPatientAppointmentHistoryService {

	Page<PatientAppointmentHistoryBo> run(Integer patientId, Pageable pageable);

}
