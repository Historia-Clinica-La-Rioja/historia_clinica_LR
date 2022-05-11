package ar.lamansys.sgh.shared.infrastructure.input.service.booking;


public interface SharedBookingPort {

	String makeBooking(BookingDto bookingDto);

	void cancelBooking(String uuid);
}
