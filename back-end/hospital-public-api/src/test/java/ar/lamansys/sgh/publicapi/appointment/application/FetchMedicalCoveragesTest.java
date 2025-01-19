package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.FetchMedicalCoverages;
import ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.exception.FetchMedicalCoveragesAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;
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
public class FetchMedicalCoveragesTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchMedicalCoverages fetchMedicalCoverages;

	@BeforeEach
	public void setUp() {
		fetchMedicalCoverages = new FetchMedicalCoverages(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		List<BookingHealthInsuranceDto> list = Arrays.asList(
				new BookingHealthInsuranceDto(1, "Health Insurance 1"),
				new BookingHealthInsuranceDto(2, "Health Insurance 2")
		);
		when(appointmentPublicApiPermissions.canAccessFetchMedicalCoverages()).thenReturn(true);
		when(bookAppointmentPort.fetchMedicalCoverages()).thenReturn(list);

		List<BookingHealthInsuranceDto> result = fetchMedicalCoverages.run();

		assertEquals(list, result);
	}

	@Test
	void testAccessDenied() {
		when(appointmentPublicApiPermissions.canAccessFetchMedicalCoverages()).thenReturn(false);

		assertThrows(FetchMedicalCoveragesAccessDeniedException.class, () -> {
			fetchMedicalCoverages.run();
		});
	}

}