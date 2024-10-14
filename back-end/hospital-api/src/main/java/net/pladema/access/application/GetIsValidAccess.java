package net.pladema.access.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.userlogged.UserLogged;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetIsValidAccess {

	private final InternmentPatientService internmentPatientService;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	private final AppointmentService appointmentService;

	private final UserLogged userLogged;
	private final ClinicHistoryAccessService clinicHistoryAccessService;

	@Value("${clinic.history.appointment.min.days:5}")
	private Short MIN_DATE_LIMIT;

	private final static String OUTPUT = "Output -> {} {} {} {} {} {}";

	public boolean run(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		boolean isIntermentEpisodeActive = internmentPatientService.internmentEpisodeInProcess(institutionId, patientId).isInProgress();
		boolean isEmergencyCareEpisodeActive = emergencyCareEpisodeService.getEmergencyCareEpisodeInProgressByInstitution(institutionId, patientId).isInProgress();
		boolean hasCurrentAppointment = appointmentService.hasCurrentAppointment(patientId, userLogged.getProfessionalId(), LocalDate.now());
		boolean hasPastAppointment = appointmentService.hasOldAppointmentWithMinDateLimit(patientId, userLogged.getProfessionalId(), MIN_DATE_LIMIT);
		boolean hasFutureAppointment = appointmentService.hasFutureAppointmentByPatientId(patientId, userLogged.getProfessionalId());
		boolean has24HoursAccess = clinicHistoryAccessService.has24HoursAccessByPatientId(patientId);
		log.debug(OUTPUT, isIntermentEpisodeActive, isEmergencyCareEpisodeActive, hasCurrentAppointment, hasPastAppointment, hasFutureAppointment, has24HoursAccess);
		return isIntermentEpisodeActive || isEmergencyCareEpisodeActive || hasCurrentAppointment || hasPastAppointment || hasFutureAppointment || has24HoursAccess;
	}
}
