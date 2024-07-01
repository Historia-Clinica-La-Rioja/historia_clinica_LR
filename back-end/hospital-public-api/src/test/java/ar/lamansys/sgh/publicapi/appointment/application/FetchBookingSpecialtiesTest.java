package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties.FetchBookingSpecialties;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties.exception.FetchBookingSpecialtiesAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchBookingSpecialtiesTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchBookingSpecialties fetchBookingSpecialties;

	@BeforeEach
	public void setUp() {
		fetchBookingSpecialties = new FetchBookingSpecialties(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		List<BookingSpecialtyDto> list = Arrays.asList(
				new BookingSpecialtyDto(1, "Specialty 1"),
				new BookingSpecialtyDto(2, "Specialty 2")
		);
		when(appointmentPublicApiPermissions.canAccessFetchBookingSpecialties()).thenReturn(true);
		when(bookAppointmentPort.fetchBookingSpecialties()).thenReturn(list);

		List<BookingSpecialtyDto> result = fetchBookingSpecialties.run();

		assertEquals(list, result);
	}

	@Test
	void testAccessDenied() {
		when(appointmentPublicApiPermissions.canAccessFetchBookingSpecialties()).thenReturn(false);

		assertThrows(FetchBookingSpecialtiesAccessDeniedException.class, () -> {
			fetchBookingSpecialties.run();
		});
	}
}