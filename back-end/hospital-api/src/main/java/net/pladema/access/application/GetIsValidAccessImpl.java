package net.pladema.access.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.userlogged.UserLogged;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;

import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeProcessBo;

import net.pladema.emergencycare.service.EmergencyCareEpisodeService;

import net.pladema.medicalconsultation.appointment.repository.entity.Appointment;

import net.pladema.medicalconsultation.appointment.service.AppointmentService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class GetIsValidAccessImpl implements GetIsValidAccess {

	private final InternmentPatientService internmentPatientService;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	private final AppointmentService appointmentService;

	private final UserLogged userLogged;

	private final Short MIN_DATE_LIMIT = 5;

	@Override
	public boolean run(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		boolean isIntermentEpisodeActive = internmentPatientService.internmentEpisodeInProcess(institutionId, patientId).isInProgress();
		boolean isEmergencyCareEpisodeActive = emergencyCareEpisodeService.emergencyCareEpisodeInProgressByInstitution(institutionId, patientId).isInProgress();
		boolean hasCurrentAppointment = appointmentService.hasCurrentAppointment(patientId, userLogged.getProfessionalId(), LocalDate.now());
		boolean hasPastAppointment = appointmentService.hasOldAppointmentWithMinDateLimit(patientId, userLogged.getProfessionalId(), MIN_DATE_LIMIT);
		boolean hasFutureAppointment = appointmentService.hasFutureAppointmentByPatientId(patientId, userLogged.getProfessionalId());
		return isIntermentEpisodeActive || isEmergencyCareEpisodeActive || hasCurrentAppointment || hasPastAppointment || hasFutureAppointment;
	}
}
