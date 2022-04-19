package ar.lamansys.online.application.booking;

import ar.lamansys.online.domain.booking.BookingBo;
import org.springframework.stereotype.Service;

@Service
public class BookAppointment {

	private final BookingAppointmentStorage bookingAppointmentStorage;

	private final BookingConfirmationMailSender bookingConfirmationMailSender;

	public BookAppointment(BookingAppointmentStorage bookingAppointmentStorage, BookingConfirmationMailSender bookingConfirmationMailSender) {
		this.bookingAppointmentStorage = bookingAppointmentStorage;
		this.bookingConfirmationMailSender = bookingConfirmationMailSender;
	}

	public String run(BookingBo bookingBo) {
		var uuid = this.bookingAppointmentStorage.save(bookingBo);
		bookingConfirmationMailSender.sendEmail(bookingBo, uuid);
		return uuid;
	}
}
