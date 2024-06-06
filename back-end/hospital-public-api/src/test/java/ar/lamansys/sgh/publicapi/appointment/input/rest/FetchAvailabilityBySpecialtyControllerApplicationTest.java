package ar.lamansys.sgh.publicapi.appointment.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.appointment.application.FetchAvailabilityBySpecialty;
import ar.lamansys.sgh.publicapi.appointment.application.exception.AppointmentAvailabilityAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest.FetchAvailabilityBySpecialtyController;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentAvailabilityPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingProfessionalDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.ProfessionalAvailabilityDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;

@ExtendWith(MockitoExtension.class)
public class FetchAvailabilityBySpecialtyControllerApplicationTest {

	private static final Integer INSTITUTION_ID = 1;
	private static final Integer CLINICAL_SPECIALTY_ID = 1;
	private static final Integer PRACTICE_ID = 1;
	private static final Integer MEDICAL_COVERAGE_ID = 1;

	@Mock
	private AppointmentAvailabilityPublicApiPermissions appointmentAvailabilityPublicApiPermissions;

	@Mock
	private SharedBookingPort sharedBookingPort;

	private FetchAvailabilityBySpecialtyController fetchAvailabilityBySpecialtyController;


	@BeforeEach
	public void setup() {
		FetchAvailabilityBySpecialty fetchAvailabilityBySpecialty = new FetchAvailabilityBySpecialty(sharedBookingPort,
				appointmentAvailabilityPublicApiPermissions);
		this.fetchAvailabilityBySpecialtyController = new FetchAvailabilityBySpecialtyController(fetchAvailabilityBySpecialty);
	}

	@Test
	void failFetchFetchAvailabilityBySpecialtyAccessDeniedException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(AppointmentAvailabilityAccessDeniedException.class,
				() -> fetchAvailabilityBySpecialtyController.getProfessionalsAvailability(INSTITUTION_ID,
						CLINICAL_SPECIALTY_ID,
						PRACTICE_ID,
						MEDICAL_COVERAGE_ID));
	}

	@Test
	void successFetchFetchAvailabilityBySpecialtyAccess() {
		allowAccessPermission(true);
		assertAvailability();
	}

	private void assertAvailability() {
		mockSomeValidEmptyAvailability();
		var response = fetchAvailabilityBySpecialtyController.getProfessionalsAvailability(
				INSTITUTION_ID,
				CLINICAL_SPECIALTY_ID,
				PRACTICE_ID,
				MEDICAL_COVERAGE_ID
		);

		Assertions.assertTrue(response.hasBody());
		var availability = response.getBody();
		Assertions.assertNotNull(availability);
		Assertions.assertEquals(1, availability.size());
		Assertions.assertTrue(availability.get(0).getAvailability().isEmpty());
		Assertions.assertEquals(availability.get(0).getProfessional().getId(), 1);
		Assertions.assertEquals(availability.get(0).getProfessional().getCoverage(), true);
		Assertions.assertEquals(availability.get(0).getProfessional().getName(), "Juan");
	}

	private void mockSomeValidEmptyAvailability() {
		var mockedEmptyAvailability = List.of(new ProfessionalAvailabilityDto(new ArrayList<>(),
				new BookingProfessionalDto(1, "Juan", true)));

		when(sharedBookingPort.fetchAvailabilityByPractice(INSTITUTION_ID,
				CLINICAL_SPECIALTY_ID,
				PRACTICE_ID,
				MEDICAL_COVERAGE_ID)).thenReturn(mockedEmptyAvailability);
	}

	private void allowAccessPermission(boolean canAccess) {
		when(appointmentAvailabilityPublicApiPermissions.canCheckAvailability(any())).thenReturn(canAccess);
	}

}
