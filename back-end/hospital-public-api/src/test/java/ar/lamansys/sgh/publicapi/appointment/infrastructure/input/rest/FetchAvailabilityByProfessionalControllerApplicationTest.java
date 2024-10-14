package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessional.exception.AvailabilityByProfessionalAccessDeniedException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessional.FetchAvailabilityByProfessional;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentAvailabilityPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

@ExtendWith(MockitoExtension.class)
public class FetchAvailabilityByProfessionalControllerApplicationTest {
	private static final Integer INSTITUTION_ID = 1;
	private static final Integer PROFESSIONAL_ID = 1;
	private static final Integer CLINICAL_SPECIALTY_ID = 1;
	private static final Integer PRACTICE_ID = 1;

	@Mock
	private AppointmentAvailabilityPublicApiPermissions appointmentAvailabilityPublicApiPermissions;

	@Mock
	private SharedBookingPort sharedBookingPort;

	private FetchAvailabilityByProfessionalController fetchAvailabilityByProfessionalController;


	@BeforeEach
	public void setup() {
		FetchAvailabilityByProfessional fetchAvailabilityByProfessional = new FetchAvailabilityByProfessional(sharedBookingPort,
				appointmentAvailabilityPublicApiPermissions);
		this.fetchAvailabilityByProfessionalController = new FetchAvailabilityByProfessionalController(fetchAvailabilityByProfessional);
	}

	@Test
	void failFetchFetchAvailabilityByProfessionalAccessDeniedException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(AvailabilityByProfessionalAccessDeniedException.class,
				() -> fetchAvailabilityByProfessionalController.getProfessionalAvailability(INSTITUTION_ID,
						PROFESSIONAL_ID,
						CLINICAL_SPECIALTY_ID,
						PRACTICE_ID
				));
	}

	@Test
	void successFetchFetchAvailabilityBySpecialtyAccess() {
		allowAccessPermission(true);
		assertAvailability();
	}

	private void assertAvailability() {
		mockSomeValidEmptyAvailability();
		var response = fetchAvailabilityByProfessionalController.getProfessionalAvailability(
				INSTITUTION_ID,
				PROFESSIONAL_ID,
				CLINICAL_SPECIALTY_ID,
				PRACTICE_ID
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
				PRACTICE_ID
		)).thenReturn(mockedEmptyAvailability);
	}

	private void allowAccessPermission(boolean canAccess) {
		when(appointmentAvailabilityPublicApiPermissions.canCheckAvailability(any())).thenReturn(canAccess);
	}
}
