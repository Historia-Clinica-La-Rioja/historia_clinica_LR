package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.FetchBookingSpecialtiesByProfessional;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.exception.FetchBookingSpecialtiesByProfessionalAccessDeniedException;
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
public class FetchBookingSpecialtiesByProfessionalTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchBookingSpecialtiesByProfessional fetchBookingSpecialtiesByProfessional;

	@BeforeEach
	public void setUp() {
		fetchBookingSpecialtiesByProfessional = new FetchBookingSpecialtiesByProfessional(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		Integer healthcareProfessionalId = 1;

		List<BookingSpecialtyDto> list = Arrays.asList(
				new BookingSpecialtyDto(1, "Specialty 1"),
				new BookingSpecialtyDto(2, "Specialty 2")
		);
		when(appointmentPublicApiPermissions.canAccessFetchBookingSpecialtiesByProfessional()).thenReturn(true);
		when(bookAppointmentPort.fetchBookingSpecialtiesByProfessional(healthcareProfessionalId)).thenReturn(list);

		List<BookingSpecialtyDto> result = fetchBookingSpecialtiesByProfessional.run(healthcareProfessionalId);

		assertEquals(list, result);
	}

	@Test
	void testAccessDenied() {
		Integer healthcareProfessionalId = 1;

		when(appointmentPublicApiPermissions.canAccessFetchBookingSpecialtiesByProfessional()).thenReturn(false);

		assertThrows(FetchBookingSpecialtiesByProfessionalAccessDeniedException.class, () -> {
			fetchBookingSpecialtiesByProfessional.run(healthcareProfessionalId);
		});
	}
}