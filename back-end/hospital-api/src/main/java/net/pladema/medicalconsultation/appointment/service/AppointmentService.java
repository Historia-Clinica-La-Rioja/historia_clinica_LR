package net.pladema.medicalconsultation.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

public interface AppointmentService {

	Optional<AppointmentBo> getAppointment(Integer appointmentId);

	Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds, LocalDate from, LocalDate to);

	Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId, LocalDate from, LocalDate to);

	boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour);
	boolean existAppointment(Integer diaryId, LocalDate date, LocalTime hour);

	Optional<AppointmentBo> findAppointmentBy(Integer diaryId, LocalDate date, LocalTime hour);

	Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId);

	boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason);

	boolean hasCurrentAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

	boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId);

	List<Integer> getOldAppointments(Integer patientId, Integer healthProfessionalId);

	List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

	boolean updatePhoneNumber(Integer appointmentId, String phonePrefix, String phoneNumber, Integer userId);

	boolean updateDate(Integer appointmentId,  LocalDate date, LocalTime time, Integer openingHoursId);

	boolean updateMedicalCoverage(Integer appointmentId, Integer patientMedicalCoverage);

	boolean saveObservation(Integer appointmentId, String observation);

	Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId, LocalDate currentDate);

	PatientMedicalCoverageBo getCurrentAppointmentMedicalCoverage(Integer patientId, Integer institutionId);

	Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId);

	AppointmentBo updateAppointment(UpdateAppointmentBo appointmentDto);

    void delete(AppointmentBo appointmentBo);

	AppointmentTicketBo getAppointmentTicketData(Integer appointmentId);

	AppointmentShortSummaryBo getAppointmentFromDeterminatedDate(Integer patientId, LocalDate date);

	List<Integer> getAppointmentsBeforeDateByStates(List<Short> statesIds, LocalDateTime maxAppointmentDate, Short limit);

	List<AppointmentBo> generateBlockedAppointments(Integer diaryId, BlockBo blockBo, DiaryBo diaryBo, LocalDate startingBlockingDate, LocalDate endingBlockingDate);

	List<AppointmentBo> unblockAppointments(BlockBo unblockDto, DiaryBo diaryBo, LocalDate startingBlockingDate, LocalDate endingBlockingDate);
}
