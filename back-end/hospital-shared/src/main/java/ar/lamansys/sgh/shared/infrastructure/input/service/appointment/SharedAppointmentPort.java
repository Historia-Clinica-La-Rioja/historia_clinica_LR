package ar.lamansys.sgh.shared.infrastructure.input.service.appointment;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;

public interface SharedAppointmentPort {

	boolean hasConfirmedAppointment(Integer patientId, Integer doctorId, LocalDate date);

	void serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

	Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId);

	void cancelAppointment(Integer institutionId, Integer appointmentId);

	String saveBooking(BookingAppointmentDto bookingAppointmentDto, BookingPersonDto bookingPersonDto, String email);

	boolean existsEmail(String email);

	void cancelBooking(String uuid);

	Optional<String> getPatientName(String uuid);

	Optional<String> getProfessionalName(Integer diaryId);

	List<PublicAppointmentListDto> fetchAppointments(Integer institutionId, String identificationNumber,
													 List<Short> includeAppointmentStatus,
													 LocalDate startDate,
													 LocalDate endDate);
}
