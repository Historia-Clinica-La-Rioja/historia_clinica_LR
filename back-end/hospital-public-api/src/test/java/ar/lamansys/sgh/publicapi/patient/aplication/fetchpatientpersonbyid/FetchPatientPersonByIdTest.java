package ar.lamansys.sgh.publicapi.patient.aplication.fetchpatientpersonbyid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.patient.application.exception.PatientNotExistsException;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.FetchPatientPersonById;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientPersonAccessDeniedException;
import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.patient.domain.PersonBo;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FetchPatientPersonByIdTest {

	private FetchPatientPersonById fetchPatientPersonById;

	@Mock
	private ExternalPatientStorage externalPatientStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@BeforeEach
	void setup() {
		fetchPatientPersonById = new FetchPatientPersonById(externalPatientStorage, patientInformationPublicApiPermission);
	}

	@Test
	void fetchPatientSuccess() {
		String patientId = "123";
		PersonBo expectedPerson = new PersonBo("firstName", "middleNames", "lastName", "otherLastNames", (short) 1, "DNI", "identificationNumber", (short) 1, "genderDescription", LocalDate.of(2024, 6, 1), "cuil", "selfDeterminationName", (short) 1, "selfDeterminationGenderDescription");

		when(patientInformationPublicApiPermission.canAccessPersonFromIdPatient()).thenReturn(true);
		when(externalPatientStorage.getPersonDataById(patientId)).thenReturn(Optional.of(expectedPerson));

		PersonBo result = fetchPatientPersonById.run(patientId);

		assertThat(result).isNotNull();
		assertThat(result.getFirstName()).isEqualTo(expectedPerson.getFirstName());
	}

	@Test
	void fetchPatientNotFound() {
		String patientId = "123";

		when(patientInformationPublicApiPermission.canAccessPersonFromIdPatient()).thenReturn(true);
		when(externalPatientStorage.getPersonDataById(patientId)).thenReturn(Optional.empty());

		Assertions.assertThrows(PatientNotExistsException.class, () -> {
			fetchPatientPersonById.run(patientId);
		});
	}

	@Test
	void fetchPatientAccessDenied() {
		String patientId = "123";

		when(patientInformationPublicApiPermission.canAccessPersonFromIdPatient()).thenReturn(false);

		Assertions.assertThrows(PatientPersonAccessDeniedException.class, () -> {
			fetchPatientPersonById.run(patientId);
		});
	}
}
