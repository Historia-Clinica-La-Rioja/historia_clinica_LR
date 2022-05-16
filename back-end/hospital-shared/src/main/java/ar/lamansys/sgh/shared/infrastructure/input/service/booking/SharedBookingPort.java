package ar.lamansys.sgh.shared.infrastructure.input.service.booking;


import java.util.List;

public interface SharedBookingPort {

	String makeBooking(BookingDto bookingDto);

	void cancelBooking(String uuid);

	List<BookingInstitutionDto> fetchAllBookingInstitutions();
}
