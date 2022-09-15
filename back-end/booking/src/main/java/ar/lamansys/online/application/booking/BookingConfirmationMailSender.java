package ar.lamansys.online.application.booking;

import ar.lamansys.online.domain.booking.BookingBo;

public interface BookingConfirmationMailSender {
	void sendEmail(
		BookingBo bookingBo,
		String uuid
	);
}
