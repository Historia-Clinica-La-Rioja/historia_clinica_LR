package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance.FetchBookingPracticesByProfessionalAndHealthInsurance;
import ar.lamansys.sgh.publicapi.appointment.application.fetchbookingpracticesbyprofessionalandhealthinsurance.exception.FetchBookingPracticesByProfessionalAndHealthInsuranceAccessDeniedException;
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
public class FetchBookingPracticesByProfessionalAndHealthInsuranceTest {

	@Mock
	private SharedBookingPort bookAppointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	private FetchBookingPracticesByProfessionalAndHealthInsurance fetchBookingPracticesByProfessionalAndHealthInsurance;

	@BeforeEach
	public void setUp() {
		fetchBookingPracticesByProfessionalAndHealthInsurance = new FetchBookingPracticesByProfessionalAndHealthInsurance(bookAppointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void testRun() {
		Integer healthcareProfessionalId = 1;
		Integer clinicalSpecialtyId = 1;
		Integer medicalCoverageId = 1;
		boolean all = true;

		List<PracticeDto> list = Arrays.asList(
				new PracticeDto(1, "Practice 1", true, "Coverage Text 1", 100),
				new PracticeDto(2, "Practice 2", false, "Coverage Text 2", 200)
		);
		when(appointmentPublicApiPermissions.canAccessFetchBookingPracticesByProfessionalAndHealthInsurance()).thenReturn(true);
		when(bookAppointmentPort.fetchBookingPracticesByProfessionalAndHealthInsurance(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all)).thenReturn(list);

		List<PracticeDto> result = fetchBookingPracticesByProfessionalAndHealthInsurance.run(healthcareProfessionalId, clinicalSpecialtyId, medicalCoverageId, all);

		assertEquals(list, result);
	}

	@Test
	void testAccessDenied() {
		Integer healthcareProfessionalId = 1;
		Integer clinicalSpecialtyId = 1;
		Integer medicalCoverageId = 1;
		boolean all = true;

		when(appointmentPublicApiPermissions.canAccessFetchBookingPracticesByProfessionalAndHealthInsurance()).thenReturn(false);

		assertThrows(FetchBookingPracticesByProfessionalAndHealthInsuranceAccessDeniedException.class, () -> {
			fetchBookingPracticesByProfessionalAndHealthInsurance.run(healthcareProfessionalId, clinicalSpecialtyId, medicalCoverageId, all);
		});
	}
}