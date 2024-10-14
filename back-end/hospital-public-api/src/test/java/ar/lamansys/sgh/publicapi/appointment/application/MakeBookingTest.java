package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.makeBooking.MakeBooking;
import ar.lamansys.sgh.publicapi.appointment.application.makeBooking.exception.MakeBookingAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.BookingPersonMailNotExistsException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.ProfessionalAlreadyBookedException;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions.SaveExternalBookingException;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SavedBookingAppointmentDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class MakeBookingTest {

	private static final Integer institutionId = 1;

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private MakeBooking makeBooking;

	@BeforeEach
	public void setUp() {
		makeBooking = new MakeBooking(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() throws ProfessionalAlreadyBookedException, SaveExternalBookingException, BookingPersonMailNotExistsException {
		BookingDto bookingDto = new BookingDto();
		SavedBookingAppointmentDto savedBookingAppointmentDto = new SavedBookingAppointmentDto(1,1,"uuid");
		when(appointmentPublicApiPermissions.canAccessMakeBooking(institutionId)).thenReturn(true);
		when(bookAppointmentPort.makeBooking(bookingDto,true)).thenReturn(savedBookingAppointmentDto);
		SavedBookingAppointmentDto result = makeBooking.run(institutionId,bookingDto);

		assertEquals(result,savedBookingAppointmentDto);
	}

	@Test
	public void makeBookingAccessDenied(){
		BookingDto bookingDto = new BookingDto();
		when(appointmentPublicApiPermissions.canAccessMakeBooking(institutionId)).thenReturn(false);
		assertThrows(MakeBookingAccessDeniedException.class, () -> {
			makeBooking.run(institutionId,bookingDto);
		});
	}

}
