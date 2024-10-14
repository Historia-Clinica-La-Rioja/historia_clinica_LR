package ar.lamansys.sgh.publicapi.prescription.input.rest;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.prescription.application.fetchMultipleCommercialPrescriptionsByIdAndDni.FetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber;
import ar.lamansys.sgh.publicapi.prescription.application.port.out.PrescriptionStorage;
import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.BadPrescriptionIdFormatException;

import ar.lamansys.sgh.publicapi.prescription.domain.exceptions.PrescriptionNotFoundException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.PrescriptionPublicApiPermissions;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.FetchMultipleCommercialPrescriptionByIdController;

import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions.PrescriptionRequestAccessDeniedException;
import ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.mapper.PrescriptionMapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchMultipleCommercialPrescriptionByIdControllerTest {

	@Mock
	private PrescriptionPublicApiPermissions prescriptionPublicApiPermissions;

	@Mock
	private LocalDateMapper localDateMapper;

	@Mock
	private PrescriptionStorage prescriptionStorage;

	private FetchMultipleCommercialPrescriptionByIdController fetchMultipleCommercialPrescriptionByIdController;

	@BeforeEach
	public void setup() {
		PrescriptionMapper prescriptionMapper = new PrescriptionMapper(localDateMapper);
		FetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber fetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber = new FetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber(prescriptionPublicApiPermissions, prescriptionStorage);
		fetchMultipleCommercialPrescriptionByIdController = new FetchMultipleCommercialPrescriptionByIdController(fetchMultipleCommercialPrescriptionsByIdAndIdentificationNumber, prescriptionMapper);
	}

	@Test
	void failDomainNumberParsing(){
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		TestUtils.shouldThrow(BadPrescriptionIdFormatException.class,
				() -> fetchMultipleCommercialPrescriptionByIdController.run("2-0-100", "1"));
	}

	@Test
	void failDomainNumberNotMatching(){
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(true);
		TestUtils.shouldThrow(PrescriptionNotFoundException.class,
				() -> fetchMultipleCommercialPrescriptionByIdController.run("89562-100", "1"));
	}

	@Test
	void failAccessDeniedException() {
		when(prescriptionPublicApiPermissions.canAccess()).thenReturn(false);
		TestUtils.shouldThrow(PrescriptionRequestAccessDeniedException.class,
				() -> fetchMultipleCommercialPrescriptionByIdController.run("0-100", "1"));
	}

}
