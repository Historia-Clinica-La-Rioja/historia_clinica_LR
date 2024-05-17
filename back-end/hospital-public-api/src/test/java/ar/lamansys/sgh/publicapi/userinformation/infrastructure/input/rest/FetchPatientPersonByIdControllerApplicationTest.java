package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.FetchPatientPersonById;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientNotExistsException;
import ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception.PatientAccessDeniedException;
import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;

import ar.lamansys.sgh.publicapi.patient.domain.PersonBo;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.FetchPatientPersonByIdController;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;

import ar.lamansys.sgh.shared.infrastructure.input.service.person.PersonDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchPatientPersonByIdControllerApplicationTest {
	private static final String patientId = "1";

	private FetchPatientPersonByIdController fetchPatientPersonByIdController;

	@Mock
	private ExternalPatientStorage externalPatientStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@BeforeEach
	public void setUp() {
		FetchPatientPersonById fetchPatientPersonById = new FetchPatientPersonById(externalPatientStorage, patientInformationPublicApiPermission);
		this.fetchPatientPersonByIdController = new FetchPatientPersonByIdController(fetchPatientPersonById);
	}

	@Test
	void successFetchPatientPersonById(){
		var personInfo = fabricatePerson("Lara",1);
		var person = personIsFetched(personInfo);
		shouldBeSamePerson(personInfo, person);
	}

	@Test
	void failFetchPatientPersonByIdPatientNotExists(){
		allowAccessPermission(true);
		userIsNotFound();
		TestUtils.shouldThrow(PatientNotExistsException.class,
				() -> fetchPatientPersonByIdController.getPersonInfo(patientId));
	}

	@Test
	void failFetchPatientPersonByIdUserInformationAccessDeniedException() {
		allowAccessPermission(false);
		TestUtils.shouldThrow(PatientAccessDeniedException.class,
				() -> fetchPatientPersonByIdController.getPersonInfo(patientId));
	}

	public PersonBo fabricatePerson(String name, Integer id) {
		PersonBo person = new PersonBo();

		person.setFirstName(name);
		person.setMiddleNames(name + "_MiddleNames");
		person.setLastName(name + "_LastName");
		person.setOtherLastNames(name + "_OtherLastNames");
		person.setIdentificationTypeId((short) 1);
		person.setIdentificationTypeDescription(name + "_IdTypeDesc");
		person.setIdentificationNumber(name + "_IdNumber");
		person.setGenderId((short) 1);
		person.setGenderDescription("Female");
		person.setBirthDate(LocalDate.of(1990, 1, 1));
		person.setCuil(name + "_cuil");
		person.setSelfDeterminationName(name + "_SelfDeterminationName");
		person.setSelfDeterminationGender((short) 1);
		person.setSelfDeterminationGenderDescription("_SelfDeterminationGenderDescription");
		return person;
	}

	private PersonDto personIsFetched(PersonBo person) {
		when(patientInformationPublicApiPermission.canAccessPersonFromIdPatient()).thenReturn(true);
		when(externalPatientStorage.getPersonDataById(patientId))
				.thenReturn(Optional.of(person));
		return fetchPatientPersonByIdController.getPersonInfo(patientId);
	}

	private void shouldBeSamePerson(PersonBo personInfo, PersonDto personDto) {
		Assertions.assertEquals(personDto.getFirstName(), personInfo.getFirstName());
		Assertions.assertEquals(personDto.getMiddleNames(), personInfo.getMiddleNames());
		Assertions.assertEquals(personDto.getLastName(), personInfo.getLastName());
		Assertions.assertEquals(personDto.getOtherLastNames(), personInfo.getOtherLastNames());
		Assertions.assertEquals(personDto.getIdentificationTypeId(), personInfo.getIdentificationTypeId());
		Assertions.assertEquals(personDto.getIdentificationTypeDescription(), personInfo.getIdentificationTypeDescription());
		Assertions.assertEquals(personDto.getIdentificationNumber(), personInfo.getIdentificationNumber());
		Assertions.assertEquals(personDto.getGenderId(), personInfo.getGenderId());
		Assertions.assertEquals(personDto.getGenderDescription(), personInfo.getGenderDescription());
		Assertions.assertEquals(personDto.getBirthDate(), personInfo.getBirthDate());
		Assertions.assertEquals(personDto.getCuil(), personInfo.getCuil());
		Assertions.assertEquals(personDto.getSelfDeterminationName(), personInfo.getSelfDeterminationName());
		Assertions.assertEquals(personDto.getSelfDeterminationGender(), personInfo.getSelfDeterminationGender());
		Assertions.assertEquals(personDto.getSelfDeterminationGenderDescription(), personInfo.getSelfDeterminationGenderDescription());
	}

	private void allowAccessPermission(boolean canAccess) {
		when(patientInformationPublicApiPermission.canAccessPersonFromIdPatient()).thenReturn(canAccess);
	}

	private void userIsNotFound() {
		when(externalPatientStorage.getPersonDataById(any())).thenReturn(Optional.empty());
	}


}
