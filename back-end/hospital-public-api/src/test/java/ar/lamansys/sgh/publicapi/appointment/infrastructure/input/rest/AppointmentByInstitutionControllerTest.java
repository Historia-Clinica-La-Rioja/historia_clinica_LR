package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.FetchAppointmentsByInstitution;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentByInstitutionControllerTest {

	@Mock
	private LocalDateMapper localDateMapper;

	@Mock
	private FetchAppointmentsByInstitution fetchAppointmentsByInstitution;

	@InjectMocks
	private AppointmentByInstitutionPublicController appointmentByInstitutionPublicController;

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
		appointmentByInstitutionPublicController = new AppointmentByInstitutionPublicController(localDateMapper, fetchAppointmentsByInstitution);
	}

	@Test
	void testGetList() {
		when(localDateMapper.fromStringToLocalDate("2024-07-01")).thenReturn(LocalDate.of(2024, 7, 1));
		when(localDateMapper.fromStringToLocalDate("2024-07-31")).thenReturn(LocalDate.of(2024, 7, 31));

		when(fetchAppointmentsByInstitution.run(1, "123456", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 7, 31))).thenReturn(appointments);

		Collection<PublicAppointmentListDto> result = appointmentByInstitutionPublicController.getList(1, "123456", "2024-07-01", "2024-07-31");

		assertEquals(appointments, result);
	}

}
