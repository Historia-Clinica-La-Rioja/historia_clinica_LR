package ar.lamansys.sgh.publicapi.prescription.input.rest;

import static org.mockito.Mockito.when;

import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.ChangePrescriptionStateMultipleCommercial;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionMedicationBlankFieldException;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionMedicationIncorrectPaymentException;
import ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception.PrescriptionMedicationInvalidSoldUnitsException;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.ChangePrescriptionStateMultipleCommercialPort;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionIdMatchException;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.MultipleDispensePrescriptionController;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMultipleDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMultipleMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.DispensedMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MultipleDispensePrescriptionControllerTest {

	private final String PRESCRIPTION_ID = "0-1";

	private MultipleDispensePrescriptionController multipleDispensePrescriptionController;

	@Mock
	private ChangePrescriptionStateMultipleCommercialPort changePrescriptionStateMultipleCommercialPort;

	@Mock
	private LocalDateMapper localDateMapper;

	@Mock
	private PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	@BeforeEach
	public void setUp() {
		PrescriptionMapper prescriptionMapper = new PrescriptionMapper(localDateMapper);
		ChangePrescriptionStateMultipleCommercial changePrescriptionStateMultipleCommercial = new ChangePrescriptionStateMultipleCommercial(prescriptionPublicApiPermissions, changePrescriptionStateMultipleCommercialPort);
		multipleDispensePrescriptionController = new MultipleDispensePrescriptionController(prescriptionMapper, changePrescriptionStateMultipleCommercial);
	}

	@Test
	void failsWhenCommercialWithEmptyAttributes() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		ChangePrescriptionStateMultipleDto inputData = getBlankAttributesMockedPrescription();
		Assertions.assertThrows(PrescriptionMedicationBlankFieldException.class, () -> multipleDispensePrescriptionController.run(PRESCRIPTION_ID, "1234567", inputData));
	}

	@Test
	void failsWhenCommercialWithNoSoldUnits() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		ChangePrescriptionStateMultipleDto inputData = getNonSoldUnitsMockedPrescription();
		Assertions.assertThrows(PrescriptionMedicationInvalidSoldUnitsException.class, () -> multipleDispensePrescriptionController.run(PRESCRIPTION_ID, "1234567", inputData));
	}

	@Test
	void failsWhenIncorrectPaymentAmounts() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		ChangePrescriptionStateMultipleDto inputData = getIncorrectPaymentAmountMockedPrescription();
		Assertions.assertThrows(PrescriptionMedicationIncorrectPaymentException.class, () -> multipleDispensePrescriptionController.run(PRESCRIPTION_ID, "1234567", inputData));
	}

	@Test
	void failsWhenDomainNotExists() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		ChangePrescriptionStateMultipleDto inputData = getDomainNotExistsMockedPrescription();
		Assertions.assertThrows(PrescriptionNotFoundException.class, () -> multipleDispensePrescriptionController.run("1-1", "1234567", inputData));
	}

	@Test
	void failsWhenDomainNotMatch() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		ChangePrescriptionStateMultipleDto inputData = getDomainNotMatchMockedPrescription();
		Assertions.assertThrows(PrescriptionIdMatchException.class, () -> multipleDispensePrescriptionController.run(PRESCRIPTION_ID, "1234567", inputData));
	}

	@Test
	void noValidationErrorWhenPrescriptionMeetsRequirements() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		ChangePrescriptionStateMultipleDto inputData = getValidMockedPrescription();
		multipleDispensePrescriptionController.run(PRESCRIPTION_ID, "1234567", inputData);
	}

	private ChangePrescriptionStateMultipleDto getValidMockedPrescription() {
		DispensedMedicationDto firstCommercialMedication = new DispensedMedicationDto("1", "MED1", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		DispensedMedicationDto secondCommercialMedication = new DispensedMedicationDto("2", "MED2", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		List<DispensedMedicationDto> commercialMedications = List.of(firstCommercialMedication, secondCommercialMedication);
		List<ChangePrescriptionStateMultipleMedicationDto> medications = getMedications(commercialMedications);
		return new ChangePrescriptionStateMultipleDto(PRESCRIPTION_ID, "PHARMACY", "PHARMACIST", "REGISTRATION", LocalDateTime.now().toString(), medications);
	}

	private ChangePrescriptionStateMultipleDto getDomainNotMatchMockedPrescription() {
		DispensedMedicationDto firstCommercialMedication = new DispensedMedicationDto("1", "MED1", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		DispensedMedicationDto secondCommercialMedication = new DispensedMedicationDto("2", "MED2", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		List<DispensedMedicationDto> commercialMedications = List.of(firstCommercialMedication, secondCommercialMedication);
		List<ChangePrescriptionStateMultipleMedicationDto> medications = getMedications(commercialMedications);
		return new ChangePrescriptionStateMultipleDto("1-1", "PHARMACY", "PHARMACIST", "REGISTRATION", LocalDateTime.now().toString(), medications);
	}

	private ChangePrescriptionStateMultipleDto getDomainNotExistsMockedPrescription() {
		DispensedMedicationDto firstCommercialMedication = new DispensedMedicationDto("1", "MED1", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		DispensedMedicationDto secondCommercialMedication = new DispensedMedicationDto("2", "MED2", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		List<DispensedMedicationDto> commercialMedications = List.of(firstCommercialMedication, secondCommercialMedication);
		List<ChangePrescriptionStateMultipleMedicationDto> medications = getMedications(commercialMedications);
		return new ChangePrescriptionStateMultipleDto("1-1", "PHARMACY", "PHARMACIST", "REGISTRATION", LocalDateTime.now().toString(), medications);
	}

	private ChangePrescriptionStateMultipleDto getIncorrectPaymentAmountMockedPrescription() {
		DispensedMedicationDto firstCommercialMedication = new DispensedMedicationDto("1", "MED1", "PRESENTATION", 1, "LAB INC.", 4.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		DispensedMedicationDto secondCommercialMedication = new DispensedMedicationDto("2", "MED2", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		List<DispensedMedicationDto> commercialMedications = List.of(firstCommercialMedication, secondCommercialMedication);
		List<ChangePrescriptionStateMultipleMedicationDto> medications = getMedications(commercialMedications);
		return new ChangePrescriptionStateMultipleDto(PRESCRIPTION_ID, "PHARMACY", "PHARMACIST", "REGISTRATION", LocalDateTime.now().toString(), medications);
	}

	private ChangePrescriptionStateMultipleDto getNonSoldUnitsMockedPrescription() {
		DispensedMedicationDto firstCommercialMedication = new DispensedMedicationDto("1", "MED1", "PRESENTATION", 0, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		DispensedMedicationDto secondCommercialMedication = new DispensedMedicationDto("2", "MED2", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		List<DispensedMedicationDto> commercialMedications = List.of(firstCommercialMedication, secondCommercialMedication);
		List<ChangePrescriptionStateMultipleMedicationDto> medications = getMedications(commercialMedications);
		return new ChangePrescriptionStateMultipleDto(PRESCRIPTION_ID, "PHARMACY", "PHARMACIST", "REGISTRATION", LocalDateTime.now().toString(), medications);
	}

	private ChangePrescriptionStateMultipleDto getBlankAttributesMockedPrescription() {
		DispensedMedicationDto firstCommercialMedication = new DispensedMedicationDto("1", "MED1", "PRESENTATION", 1, "", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		DispensedMedicationDto secondCommercialMedication = new DispensedMedicationDto("2", "", "PRESENTATION", 1, "LAB INC.", 3.0, 2.0, 1.0, "PHARMACY", "PHARMACIST", "obs");
		List<DispensedMedicationDto> commercialMedications = List.of(firstCommercialMedication, secondCommercialMedication);
		List<ChangePrescriptionStateMultipleMedicationDto> medications = getMedications(commercialMedications);
		return new ChangePrescriptionStateMultipleDto(PRESCRIPTION_ID, "PHARMACY", "PHARMACIST", "REGISTRATION", LocalDateTime.now().toString(), medications);
	}

	private List<ChangePrescriptionStateMultipleMedicationDto> getMedications(List<DispensedMedicationDto> commercialMedications) {
		return List.of(new ChangePrescriptionStateMultipleMedicationDto(1, (short) 1, commercialMedications, ""));
	}

}
