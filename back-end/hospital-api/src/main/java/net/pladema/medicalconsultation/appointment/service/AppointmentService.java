package net.pladema.medicalconsultation.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

public interface AppointmentService {

	Optional<AppointmentBo> getAppointment(Integer appointmentId);

	Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds);

	Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId);

	boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour);
	boolean existAppointment(Integer diaryId, LocalDate date, LocalTime hour);

	Optional<AppointmentBo> findAppointmentBy(Integer diaryId, LocalDate date, LocalTime hour);

	Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId);

	boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason);

	boolean hasConfirmedAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

	List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

	boolean updatePhoneNumber(Integer appointmentId, String phonePrefix, String phoneNumber, Integer userId);

	boolean updateDate(Integer appointmentId,  LocalDate date, LocalTime time);

	boolean updateMedicalCoverage(Integer appointmentId, Integer patientMedicalCoverage);

	boolean saveObservation(Integer appointmentId, String observation);

	Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId, LocalDate currentDate);

	PatientMedicalCoverageBo getCurrentAppointmentMedicalCoverage(Integer patientId, Integer institutionId);

	Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId);

	AppointmentBo updateAppointment(UpdateAppointmentBo appointmentDto);

	void delete(AppointmentBo appointmentBo);
}
