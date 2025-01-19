package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.FetchBookingPracticesBySpecialtyAndHealthInsurance;
import ar.lamansys.sgh.publicapi.appointment.application.fetchBookingPracticesBySpecialtyAndHealthInsurance.exception.FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.PracticeDto;
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
public class FetchBookingPracticesBySpecialtyAndHealthInsuranceTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchBookingPracticesBySpecialtyAndHealthInsurance fetchBookingPracticesBySpecialtyAndHealthInsurance;

	@BeforeEach
	public void setUp() {
		fetchBookingPracticesBySpecialtyAndHealthInsurance = new FetchBookingPracticesBySpecialtyAndHealthInsurance(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		Integer clinicalSpecialtyId = 1;
		Integer medicalCoverageId = 1;
		boolean all = true;

		List<PracticeDto> list = Arrays.asList(
				new PracticeDto(1, "Practice 1", true, "Coverage 1", 123),
				new PracticeDto(2, "Practice 2", false, "Coverage 2", 456)
		);
		when(appointmentPublicApiPermissions.canAccessFetchBookingPracticesBySpecialtyAndHealthInsurance()).thenReturn(true);
		when(bookAppointmentPort.fetchBookingPracticesBySpecialtyAndHealthInsurance(clinicalSpecialtyId, medicalCoverageId, all)).thenReturn(list);

		List<PracticeDto> result = fetchBookingPracticesBySpecialtyAndHealthInsurance.run(clinicalSpecialtyId, medicalCoverageId, all);

		assertEquals(list, result);
	}

	@Test
	void testAccessDenied() {
		Integer clinicalSpecialtyId = 1;
		Integer medicalCoverageId = 1;
		boolean all = true;

		when(appointmentPublicApiPermissions.canAccessFetchBookingPracticesBySpecialtyAndHealthInsurance()).thenReturn(false);

		assertThrows(FetchBookingPracticesBySpecialtyAndHealthInsuranceAccessDeniedException.class, () -> {
			fetchBookingPracticesBySpecialtyAndHealthInsurance.run(clinicalSpecialtyId, medicalCoverageId, all);
		});
	}
}
