package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions.FetchAllBookingInstitutions;
import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions.exception.FetchAllBookingInstitutionsAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
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
public class FetchAllBookingInstitutionsTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchAllBookingInstitutions fetchAllBookingInstitutions;

	@BeforeEach
	public void setUp() {
		fetchAllBookingInstitutions = new FetchAllBookingInstitutions(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		List<BookingInstitutionDto> list = Arrays.asList(
				new BookingInstitutionDto(1, "Institution 1"),
				new BookingInstitutionDto(2, "Institution 2")
		);
		when(appointmentPublicApiPermissions.canAccessFetchAllBookingInstitutions()).thenReturn(true);
		when(bookAppointmentPort.fetchAllBookingInstitutions()).thenReturn(list);

		List<BookingInstitutionDto> result = fetchAllBookingInstitutions.run();

		assertEquals(list, result);
	}

	@Test
	void testAccessDenied() {
		when(appointmentPublicApiPermissions.canAccessFetchAllBookingInstitutions()).thenReturn(false);

		assertThrows(FetchAllBookingInstitutionsAccessDeniedException.class, () -> {
			fetchAllBookingInstitutions.run();
		});
	}
}
