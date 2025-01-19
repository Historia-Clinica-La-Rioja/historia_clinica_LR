package ar.lamansys.sgh.shared.infrastructure.input.service.appointment;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.AppointmentDataDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;

public interface SharedAppointmentPort {

	boolean hasCurrentAppointment(Integer patientId, Integer doctorId, LocalDate date);

	boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId);

	Integer serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

	Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId);

	void cancelAppointment(Integer institutionId, Integer appointmentId, String reason);

	void cancelAbsentAppointment(Integer appointmentId, String reason);

	SavedBookingAppointmentDto saveBooking(BookingAppointmentDto bookingAppointmentDto, BookingPersonDto bookingPersonDto, String email) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException;

	boolean existsEmail(String email);

	void cancelBooking(String uuid);

	Optional<String> getPatientName(String uuid);

	Optional<String> getProfessionalName(Integer diaryId);

	List<PublicAppointmentListDto> fetchAppointments(Integer institutionId, String identificationNumber,
													 List<Short> includeAppointmentStatus,
													 LocalDate startDate,
													 LocalDate endDate);

	void saveDocumentAppointment(DocumentAppointmentDto documentAppointmentDto);

	void deleteDocumentAppointment(DocumentAppointmentDto documentAppointmentDto);

	List<ReferenceAppointmentStateDto> getReferencesAppointmentState(Map<Integer, List<Integer>> referenceAppointments);

	Optional<AppointmentDataDto> getNearestAppointmentData(List<Integer> appointments);

	Integer getDiaryId(Integer appointmentId);

	Integer getInstitutionId(Integer diaryId);

	boolean appointmentDateAndTimeAlreadyUsed(Integer diaryId, Integer openingHoursId, LocalDate appointmentDte, LocalTime appointmentTime);

}
