package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.time.LocalDate;
import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingPersonDto;

public interface SharedAppointmentPort {

    boolean hasConfirmedAppointment(Integer patientId, Integer doctorId, LocalDate date);

    void serveAppointment(Integer patientId, Integer doctorId, LocalDate date);

    Integer getMedicalCoverage(Integer patientId, Integer healthcareProfessionalId);

	String saveBooking(BookingAppointmentDto bookingAppointmentDto, BookingPersonDto bookingPersonDto, String email);

	boolean existsEmail(String email);

	void cancelBooking(String email);

	Optional<String> getPatientName(String uuid);

	Optional<String> getProfessionalName(Integer diaryId);

    List<AppointmentListDto> fetchAppointments(String sisaCode, String specialtySctid, LocalDate startDate, LocalDate endDate);
}
