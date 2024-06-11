package ar.lamansys.sgh.publicapi.prescription.input.rest;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProfessionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionProfessionalRegistrationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionSpecialtyDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.PrescriptionsDataDto;
import ar.lamansys.sgh.publicapi.patient.application.fetchprescriptionsdatabydni.FetchPrescriptionsDataByDni;
import ar.lamansys.sgh.publicapi.patient.application.fetchprescriptionsdatabydni.exception.PatientPrescriptionsAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.PatientPrescriptionsDataController;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionsDataBo;
import ar.lamansys.sgh.publicapi.prescription.domain.ProfessionalPrescriptionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionSpecialtyBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionBo;
import ar.lamansys.sgh.publicapi.prescription.domain.PrescriptionProfessionalRegistrationBo;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ProfessionalPrescriptionDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientPrescriptionsDataControllerTest {

	private static final String identificationNumber = "12345678";

	private PatientPrescriptionsDataController patientPrescriptionsDataController;

	@Mock
	private PrescriptionStorage prescriptionStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@Mock
	private PrescriptionMapper prescriptionMapper;

	@BeforeEach
	public void setUp() {
		FetchPrescriptionsDataByDni fetchPrescriptionsDataByDni = new FetchPrescriptionsDataByDni(prescriptionStorage, patientInformationPublicApiPermission);
		this.patientPrescriptionsDataController = new PatientPrescriptionsDataController(fetchPrescriptionsDataByDni, prescriptionMapper);
	}

	@Test
	void successFetchPrescriptionsDataByDni() {
		var prescriptions = fabricatePrescriptionsData();
		var prescriptionDtoList = fabricatePrescriptionsDataDto();

		when(prescriptionStorage.getPrescriptionsDataByDni(identificationNumber))
				.thenReturn(Optional.of(prescriptions));
		when(patientInformationPublicApiPermission.canAccessPrescriptionDataFromPatientIdNumber()).thenReturn(true);
		when(prescriptionMapper.mapToPrescriptionsDataDto(prescriptions.get(0))).thenReturn(prescriptionDtoList.get(0));

		List<PrescriptionsDataDto> response = patientPrescriptionsDataController.getPrescriptions(identificationNumber);
		Assertions.assertEquals(prescriptionDtoList, response);
	}

	@Test
	void failFetchPrescriptionsDataByDniAccessDenied() {
		when(patientInformationPublicApiPermission.canAccessPrescriptionDataFromPatientIdNumber()).thenReturn(false);

		TestUtils.shouldThrow(PatientPrescriptionsAccessDeniedException.class,
				() -> patientPrescriptionsDataController.getPrescriptions(identificationNumber));
	}

	private List<PrescriptionsDataBo> fabricatePrescriptionsData() {
		PrescriptionProfessionBo profession = new PrescriptionProfessionBo(
				"Asistente Dental",
				"snomedId"
		);

		PrescriptionProfessionalRegistrationBo registration = new PrescriptionProfessionalRegistrationBo(
				"registrationNumber",
				"registrationType"
		);

		ProfessionalPrescriptionBo professional = new ProfessionalPrescriptionBo(
				"Name",
				"lastName",
				"DNI",
				"123456",
				"123-456-7890",
				"email@example.com",
				Collections.singletonList(profession),
				Collections.singletonList(registration)
		);

		PrescriptionSpecialtyBo specialty = new PrescriptionSpecialtyBo(
				"specialty",
				"snomedId"
		);

		PrescriptionsDataBo prescription = new PrescriptionsDataBo(
				"1",
				"1",
				LocalDateTime.now(),
				LocalDateTime.now(),
				"http://example.com/link",
				professional,
				specialty
		);

		return Collections.singletonList(prescription);
	}

	private List<PrescriptionsDataDto> fabricatePrescriptionsDataDto() {
		PrescriptionProfessionDto profession = new PrescriptionProfessionDto(
				"profession",
				"snomedId"
		);

		PrescriptionProfessionalRegistrationDto registration = new PrescriptionProfessionalRegistrationDto(
				"registrationNumber",
				"registrationType"
		);

		ProfessionalPrescriptionDto professional = new ProfessionalPrescriptionDto(
				"Name",
				"lastName",
				"DNI",
				"123456",
				"123-456-7890",
				"email@example.com",
				Collections.singletonList(profession),
				Collections.singletonList(registration)
		);

		PrescriptionSpecialtyDto specialty = new PrescriptionSpecialtyDto(
				"specialty",
				"snomedId"
		);

		PrescriptionsDataDto prescription = new PrescriptionsDataDto(
				"1",
				"1",
				LocalDateTime.now(),
				LocalDateTime.now(),
				"http://example.com/link",
				professional,
				specialty
		);

		return Collections.singletonList(prescription);
	}
}