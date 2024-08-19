package net.pladema.medicalconsultation.appointment.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentEquipmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentShortSummaryBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketBo;
import net.pladema.medicalconsultation.appointment.repository.domain.AppointmentTicketImageBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentAssignedBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBookingBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSummaryBo;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.UpdateAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.BlockBo;
import net.pladema.medicalconsultation.diary.service.domain.CustomRecurringAppointmentBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;

public interface AppointmentService {

	Optional<AppointmentBo> getAppointment(Integer appointmentId);

	Optional<AppointmentBo> getAppointmentSummary(Integer appointmentId);

	List<AppointmentBookingBo> getCompleteBookingAppointmentInfo(String identificationNumber);

	Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId);

	Collection<AppointmentBo> getAppointmentsByDiaries(List<Integer> diaryIds, LocalDate from, LocalDate to);

	Collection<AppointmentBo> getAppointmentsByEquipmentDiary(Integer equipmentDiaryId, LocalDate from, LocalDate to);

	Collection<EquipmentAppointmentBo> getAppointmentsByEquipmentId(Integer equipmentDiaryId, Integer institutionId, LocalDate from, LocalDate to);

	Collection<AppointmentBo> getAppointmentsByProfessionalInInstitution(Integer healthcareProfessionalId, Integer institutionId, LocalDate from, LocalDate to);

	boolean existAppointment(Integer diaryId, Integer openingHoursId, LocalDate date, LocalTime hour);

	boolean existAppointment(Integer diaryId, LocalDate date, LocalTime hour, Integer appointmentId);

	Optional<AppointmentBo> findBlockedAppointmentBy(Integer diaryId, LocalDate date, LocalTime hour);

	Collection<AppointmentBo> getFutureActiveAppointmentsByDiary(Integer diaryId);

	Collection<AppointmentBo> getFutureActiveAppointmentsByEquipmentDiary(Integer diaryId);

	boolean updateState(Integer appointmentId, short appointmentStateId, Integer userId, String reason);

	boolean hasCurrentAppointment(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

	boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId);

	List<Integer> getOldAppointments(Integer patientId, Integer healthProfessionalId);

	List<Integer> getAppointmentsId(Integer patientId, Integer healthcareProfessionalId, LocalDate date);

	boolean updatePhoneNumber(Integer appointmentId, String phonePrefix, String phoneNumber, Integer userId);

	boolean updateDate(Integer appointmentId,  LocalDate date, LocalTime time, Integer openingHoursId, Short recurringAppointmentTypeId);

	boolean updateMedicalCoverage(Integer appointmentId, Integer patientMedicalCoverage);

	boolean saveObservation(Integer appointmentId, String observation);

	Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId, LocalDate currentDate);

	PatientMedicalCoverageBo getCurrentAppointmentMedicalCoverage(Integer patientId, Integer institutionId);

	Collection<AppointmentAssignedBo> getCompleteAssignedAppointmentInfo(Integer patientId, LocalDate minDate, LocalDate maxDate);

	AppointmentBo updateAppointment(UpdateAppointmentBo appointmentDto);

    void delete(AppointmentBo appointmentBo);

	AppointmentTicketBo getAppointmentTicketData(Integer appointmentId);

	AppointmentTicketImageBo getAppointmentImageTicketData(Integer appointmentId, boolean isTranscribed);

	AppointmentShortSummaryBo getAppointmentFromDeterminatedDate(Integer patientId, Integer institutionId, LocalDate date, LocalTime hour);

	AppointmentEquipmentShortSummaryBo getAppointmentEquipmentFromDeterminatedDate(Integer patientId, LocalDate date);

	List<Integer> getAppointmentsBeforeDateByStates(List<Short> statesIds, LocalDateTime maxAppointmentDate, Short limit);

	List<AppointmentBo> generateBlockedAppointments(Integer diaryId, BlockBo blockBo, DiaryBo diaryBo, LocalDate startingBlockingDate, LocalDate endingBlockingDate);

	List<AppointmentBo> unblockAppointments(BlockBo unblockDto, DiaryBo diaryBo, LocalDate startingBlockingDate, LocalDate endingBlockingDate);

	boolean setAppointmentPatientMedicalCoverageId(Integer patientId, List<Integer> patientMedicalCoverages, Integer newPatientMedicalCoverageId);

	Integer patientHasCurrentAppointment(Integer institutionId, Integer patientId);

	List<AppointmentSummaryBo> getAppointmentDataByAppointmentIds(List<Integer> appointmentIds);

	Boolean openingHourAllowedProtectedAppointment(Integer openingHoursId, Integer diaryId);

	void deleteLabelFromAppointment(Integer diaryId, List<Integer> ids);

	PatientMedicalCoverageBo getMedicalCoverageFromAppointment(Integer appointmentId);
	
	Boolean hasOldAppointmentWithMinDateLimit(Integer patientId, Integer healthcareProfessionalId, Short minDateLimit);

	Boolean hasFutureAppointmentByPatientId(Integer patientId, Integer healthcareProfessionalId);
	void checkAppointmentEveryWeek(Integer diaryId, LocalTime hour, LocalDate date, Short dayWeekId, Integer appointmentId);

	void updateAppointmentByOptionId(AppointmentBo currentAppointment, AppointmentBo newAppointment);

	CustomRecurringAppointmentBo getCustomAppointment(Integer appointmentId);

	void checkRemainingChildAppointments(Integer appointmentId);

	void deleteCustomAppointment(Integer appointmentId);

	void deleteParentId(Integer appointmentId);

	Boolean isAppointmentOverturn(Integer appointmentId);

	void verifyRecurringAppointmentsOverturn(Integer diaryId);

	Collection<AppointmentBo> hasAppointment(String dni, Integer id);
}
