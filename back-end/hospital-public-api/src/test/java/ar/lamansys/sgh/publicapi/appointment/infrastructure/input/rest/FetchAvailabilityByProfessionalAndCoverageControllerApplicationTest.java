package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessionalandcoverage.exception.AvailabilityByProfessionalAndCoverageAccessDeniedException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessionalandcoverage.FetchAvailabilityByProfessionalAndCoverage;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentAvailabilityPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

@ExtendWith(MockitoExtension.class)
public class FetchAvailabilityByProfessionalAndCoverageControllerApplicationTest {

	private static final Integer INSTITUTION_ID = 1;
	private static final Integer PROFESSIONAL_ID = 1;
	private static final Integer CLINICAL_SPECIALTY_ID = 1;
	private static final Integer PRACTICE_ID = 1;
	private static final Integer COVERAGE_ID = 1;

	@Mock
	private AppointmentAvailabilityPublicApiPermissions appointmentAvailabilityPublicApiPermissions;

	@Mock
	private SharedBookingPort sharedBookingPort;

	private FetchAvailabilityByProfessionalAndCoverageController fetchAvailabilityByProfessionalAndCoverageController;


	@BeforeEach
	public void setup() {
		FetchAvailabilityByProfessionalAndCoverage fetchAvailabilityByProfessionalAndCoverage =
				new FetchAvailabilityByProfessionalAndCoverage(sharedBookingPort, appointmentAvailabilityPublicApiPermissions);
		this.fetchAvailabilityByProfessionalAndCoverageController = new FetchAvailabilityByProfessionalAndCoverageController(fetchAvailabilityByProfessionalAndCoverage);
	}

	@Test
	void failFetchFetchAvailabilityByProfessionalAccessDeniedException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(AvailabilityByProfessionalAndCoverageAccessDeniedException.class,
				() -> fetchAvailabilityByProfessionalAndCoverageController.getProfessionalAvailability(INSTITUTION_ID,
						PROFESSIONAL_ID,
						CLINICAL_SPECIALTY_ID,
						PRACTICE_ID,
						COVERAGE_ID,
						"2024-12-12"
				));
	}

	@Test
	void successFetchFetchAvailabilityBySpecialtyAccess() {
		allowAccessPermission(true);
		assertAvailability();
	}

	private void assertAvailability() {
		mockSomeValidEmptyAvailability();
		var response = fetchAvailabilityByProfessionalAndCoverageController.getProfessionalAvailability(
				INSTITUTION_ID,
				PROFESSIONAL_ID,
				CLINICAL_SPECIALTY_ID,
				PRACTICE_ID,
				COVERAGE_ID,
				"2024-12-12"
		);

		Assertions.assertTrue(response.hasBody());
		var availability = response.getBody();
		Assertions.assertNotNull(availability);
		Assertions.assertTrue(availability.getAvailability().isEmpty());
		Assertions.assertEquals(availability.getProfessional().getId(), 1);
		Assertions.assertEquals(availability.getProfessional().getCoverage(), true);
		Assertions.assertEquals(availability.getProfessional().getName(), "Juan");
	}

	private void mockSomeValidEmptyAvailability() {
		var mockedEmptyAvailability = new ProfessionalAvailabilityDto(new ArrayList<>(),
				new BookingProfessionalDto(1, "Juan", true));

		when(sharedBookingPort.fetchAvailabilityByPracticeAndProfessional(INSTITUTION_ID,
				PROFESSIONAL_ID,
				CLINICAL_SPECIALTY_ID,
				PRACTICE_ID,
				COVERAGE_ID,
				"2024-12-12"
		)).thenReturn(mockedEmptyAvailability);
	}

	private void allowAccessPermission(boolean canAccess) {
		when(appointmentAvailabilityPublicApiPermissions.canCheckAvailability(any())).thenReturn(canAccess);
	}
}
