package ar.lamansys.sgh.publicapi.prescription.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.ChangePrescriptionStateMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto.DispensedMedicationDto;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.validators.PrescriptionStatusValidator;

@ExtendWith(MockitoExtension.class)
public class DispensePrescriptionControllerTest {

	@Mock
	private ConstraintValidatorContext mockContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder mockConstraintViolationBuilder;

	private PrescriptionStatusValidator prescriptionStatusValidator;

	@BeforeEach
	void setUp(){
		prescriptionStatusValidator = new PrescriptionStatusValidator();
	}

	@Test
	void validNotNullFieldsDto(){
		Assertions.assertTrue(prescriptionStatusValidator.isValid(
				fabricateMock("TestPharmacistName",
						"TestBrand", "TestPresent", "TestComName", "",
						(short)1, 1.0, 1.0, 2.0, 1),
				mockContext));
	}

	@Test
	void failPharmacistNameBlank(){
		when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(prescriptionStatusValidator.isValid(fabricateMock("",
						"TestBrand", "TestPresent", "TestComName", "",
						(short)1, 1.0, 1.0, 2.0, 2),
				mockContext));
	}

	@Test
	void failEmptyFieldLines(){
		when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(prescriptionStatusValidator.isValid(fabricateMock("TestPharmacistName",
						"", "", "", "asdasd", (short)1,
						1.0, 1.0, 2.0, 1),
				mockContext));
	}

	@Test
	void failCanceledLinesWithoutObservations(){
		when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(prescriptionStatusValidator.isValid(fabricateMock("TestPharmacistName",
						"TestBrand", "TestPresent", "TestName", null,
						(short)5, 1.0, 1.0, 2.0, 1),
				mockContext));
	}

	@Test
	void failIncorrectPayments(){
		when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(prescriptionStatusValidator.isValid(fabricateMock("TestPharmacistName",
						"TestBrand", "TestPresent", "TestName", "",
						(short)1, 1.0, 1.0, 1.0, 1),
				mockContext));
	}

	@Test
	void failSoldUnitsInvalid(){
		when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockConstraintViolationBuilder);
		Assertions.assertFalse(prescriptionStatusValidator.isValid(fabricateMock("TestPharmacistName",
						"TestBrand", "TestPresent", "TestName", "",
						(short)1, 2.0, 2.0, 2.0, 0),
				mockContext));
	}

	private ChangePrescriptionStateDto fabricateMock(String pharmacistName,
													 String brand,
													 String commercialPresentation,
													 String commercialName,
													 String observations,
													 Short status,
													 Double affiliatePayment,
													 Double medicalCoveragePayment,
													 Double price,
													 Integer soldUnits){
		return new ChangePrescriptionStateDto(
				"1",
				"TestPharmacy",
				pharmacistName,
				"TestPharmacistRegistration",
				LocalDateTime.now(),
				List.of(new ChangePrescriptionStateMedicationDto(
								1, status,
								new DispensedMedicationDto(
										"111111", commercialName, commercialPresentation,
										soldUnits, brand, price, affiliatePayment, medicalCoveragePayment,
										"TestPharmacyName", "TestPharmacistName", "obs"
								),
								observations
						)
				)
		);
	}
}
