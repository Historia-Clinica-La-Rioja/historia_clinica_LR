package ar.lamansys.sgh.publicapi.patient.application.fetchappointmentsdatabydni;

import ar.lamansys.sgh.publicapi.patient.application.port.out.AppointmentStorage;
import ar.lamansys.sgh.publicapi.patient.domain.AppointmentsByUserBo;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientPersonAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchAppointmentsDataByDniTest {

	@Mock
	private AppointmentStorage appointmentStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@InjectMocks
	private FetchAppointmentsDataByDni fetchAppointmentsDataByDni;

	@BeforeEach
	void setup() {
		fetchAppointmentsDataByDni = new FetchAppointmentsDataByDni(appointmentStorage, patientInformationPublicApiPermission);
	}

	@Test
	void fetchAppointmentsSuccess() {
		String identificationNumber = "12345678";
		Short identificationTypeId = 1;
		Short genderId = 1;
		String birthDate = "2024-01-01";

		AppointmentsByUserBo expectedAppointments = new AppointmentsByUserBo();
		when(patientInformationPublicApiPermission.canAccessAppointmentsDataFromPatientIdNumber()).thenReturn(true);
		when(appointmentStorage.getAppointmentsDataByDni(anyString(), anyShort(), anyShort(), anyString()))
				.thenReturn(expectedAppointments);

		AppointmentsByUserBo result = fetchAppointmentsDataByDni.run(identificationNumber, identificationTypeId, genderId, birthDate);

		assertNotNull(result);
	}

	@Test
	void fetchAppointmentsAccessDenied() {
		String identificationNumber = "12345678";
		Short identificationTypeId = 1;
		Short genderId = 1;
		String birthDate = "2024-01-01";

		when(patientInformationPublicApiPermission.canAccessAppointmentsDataFromPatientIdNumber()).thenReturn(false);

		assertThrows(PatientPersonAccessDeniedException.class, () -> {
			fetchAppointmentsDataByDni.run(identificationNumber, identificationTypeId, genderId, birthDate);
		});
	}
}