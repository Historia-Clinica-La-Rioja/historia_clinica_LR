package ar.lamansys.sgh.publicapi.appointment.application;

import ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.FetchAppointmentsByInstitution;
import ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.exception.AppointmentAccessDeniedException;
import ar.lamansys.sgh.publicapi.appointment.infrastructure.input.service.AppointmentPublicApiPermissions;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchAppointmentsByInstitutionTest {

	@Mock
	private SharedAppointmentPort appointmentPort;

	@Mock
	private AppointmentPublicApiPermissions appointmentPublicApiPermissions;

	@InjectMocks
	private FetchAppointmentsByInstitution fetchAppointmentsByInstitution;

	private List<PublicAppointmentListDto> appointments;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		appointments = Collections.singletonList(
				new PublicAppointmentListDto(
						1,
						"2023-07-01",
						"10:00",
						false,
						"123456789",
						null,
						null,
						null,
						null,
						null,
						null
				)
		);
		fetchAppointmentsByInstitution = new FetchAppointmentsByInstitution(appointmentPort, appointmentPublicApiPermissions);
	}

	@Test
	void fetchAppointments_Success() {
		Integer institutionId = 1;
		String identificationNumber = "12345678";
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 12, 31);

		when(appointmentPublicApiPermissions.canAccessAppointment(institutionId)).thenReturn(true);
		when(appointmentPort.fetchAppointments(institutionId, identificationNumber, null, startDate, endDate)).thenReturn(appointments);

		List<PublicAppointmentListDto> result = fetchAppointmentsByInstitution.run(institutionId, identificationNumber, startDate, endDate);

		assertEquals(appointments, result);
	}

	@Test
	void fetchAppointments_AccessDenied() {
		Integer institutionId = 1;
		String identificationNumber = "12345678";
		LocalDate startDate = LocalDate.of(2023, 1, 1);
		LocalDate endDate = LocalDate.of(2023, 12, 31);

		when(appointmentPublicApiPermissions.canAccessAppointment(institutionId)).thenReturn(false);

		assertThrows(AppointmentAccessDeniedException.class, () -> {
			fetchAppointmentsByInstitution.run(institutionId, identificationNumber, startDate, endDate);
		});
	}
}
