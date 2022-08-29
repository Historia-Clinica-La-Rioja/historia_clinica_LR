package ar.lamansys.sgh.shared.infrastructure.input.service.appointment;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;

public interface SharedAppointmentPort {

	boolean hasCurrentAppointment(Integer patientId, Integer doctorId, LocalDate date);

	boolean hasOldAppointment(Integer patientId, Integer healthProfessionalId);

	Integer serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

	Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId);

	void cancelAppointment(Integer institutionId, Integer appointmentId, String reason);

	String saveBooking(BookingAppointmentDto bookingAppointmentDto, BookingPersonDto bookingPersonDto, String email);

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
}
