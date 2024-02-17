package ar.lamansys.sgh.clinichistory.application.signDocumentFile;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;


import ar.lamansys.sgx.shared.files.FileService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.clinichistory.application.ports.DigitalSignatureStorage;
import ar.lamansys.sgh.clinichistory.application.ports.DocumentFileStorage;
import ar.lamansys.sgh.clinichistory.application.signDocumentFile.exceptions.SignDocumentFileException;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;

@ExtendWith(MockitoExtension.class)
class SignDocumentFileTest {

	private SignDocumentFile signDocumentFile;

	@Mock
	private DocumentFileStorage documentFileStorage;

	@Mock
	private DigitalSignatureStorage digitalSignatureStorage;

	@Mock
	private SharedHospitalUserPort hospitalUserPort;

	@Mock
	private FileService fileService;

	private final Integer institutionId = 1;
	private final Integer userId = 1;
	private final Long documentId = 1L;

	@BeforeEach
	void setUp() {
		signDocumentFile = new SignDocumentFile(documentFileStorage, digitalSignatureStorage,
				hospitalUserPort, fileService);
	}

	@Test
	void test_sign_invalid_document_creator() {
		when(documentFileStorage.isDocumentBelongsToUser(documentId, userId)).thenReturn(false);
		Exception exception = Assertions.assertThrows(SignDocumentFileException.class, () ->
				signDocumentFile.run(institutionId, List.of(1L), userId));
		String expectedMessage = "El usuario no es el creador del documento " + documentId;
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);

	}

	@Test
	void test_sign_invalid_cuil_signer() {
		when(documentFileStorage.isDocumentBelongsToUser(documentId, userId)).thenReturn(true);
		when(hospitalUserPort.getCuitByUserId(userId)).thenReturn(Optional.empty());
		Exception exception = Assertions.assertThrows(SignDocumentFileException.class, () ->
				signDocumentFile.run(institutionId, List.of(1L), userId));
		String expectedMessage = "El profesional no posee cuil";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);
	}

}