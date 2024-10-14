package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.FetchAllBookingInstitutionsExtended;
import ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.exception.FetchAllBookingInstitutionsExtendedAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionExtendedDto;
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
public class FetchAllBookingInstitutionsExtendedTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchAllBookingInstitutionsExtended fetchAllBookingInstitutionsExtended;

	@BeforeEach
	public void setUp() {
		fetchAllBookingInstitutionsExtended = new FetchAllBookingInstitutionsExtended(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		List<BookingInstitutionExtendedDto> list = Arrays.asList(
				new BookingInstitutionExtendedDto(1, "Institution 1", "SISA1", "Dependency1", "Address1", "City1", "Department1", Arrays.asList("Specialty1"), Arrays.asList("Alias1")),
				new BookingInstitutionExtendedDto(2, "Institution 2", "SISA2", "Dependency2", "Address2", "City2", "Department2", Arrays.asList("Specialty2"), Arrays.asList("Alias2"))
		);
		when(appointmentPublicApiPermissions.canAccessFetchAllBookingInstitutionsExtended()).thenReturn(true);
		when(bookAppointmentPort.fetchAllBookingInstitutionsExtended()).thenReturn(list);

		List<BookingInstitutionExtendedDto> result = fetchAllBookingInstitutionsExtended.run();

		assertEquals(list, result);
	}

	@Test
	void testRunAccessDenied() {
		when(appointmentPublicApiPermissions.canAccessFetchAllBookingInstitutionsExtended()).thenReturn(false);

		assertThrows(FetchAllBookingInstitutionsExtendedAccessDeniedException.class, () -> {
			fetchAllBookingInstitutionsExtended.run();
		});
	}
}
