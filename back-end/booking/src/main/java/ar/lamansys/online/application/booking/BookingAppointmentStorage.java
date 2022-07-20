package ar.lamansys.online.application.booking;

import java.util.Optional;

import ar.lamansys.online.domain.booking.BookingBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;

public interface BookingAppointmentStorage {
    SavedBookingAppointmentDto save(BookingBo bookingBo) throws ProfessionalAlreadyBookedException, BookingPersonMailNotExistsException;
    boolean existsEmail(String email);
    void cancelBooking(String uuid);
    Optional<String> getPatientName(String uuid);
    Optional<String> getProfessionalName(Integer diaryId);
	String getInstitutionAddress(Integer diaryId);
}
