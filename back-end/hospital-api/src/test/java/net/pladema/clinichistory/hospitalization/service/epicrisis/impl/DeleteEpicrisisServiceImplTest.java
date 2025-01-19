package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgh.shared.infrastructure.output.entities.ESignatureStatus;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.DeleteEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentDocumentModificationValidatorImpl;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;

import org.springframework.boot.test.mock.mockito.MockBean;


class DeleteEpicrisisServiceImplTest extends UnitRepository {

	private final Long DOCUMENT_ID = 1l;
	private final Integer INTERNMET_EPISODE_ID = 8;
	private final Short DOCUMENT_TYPE_ID = DocumentType.EPICRISIS;

	private DeleteEpicrisisService deleteEpicrisisService;

	@Mock
	private SharedDocumentPort sharedDocumentPort;

	@Mock
	private InternmentEpisodeService internmentEpisodeService;

	private InternmentDocumentModificationValidator internmentDocumentModificationValidator;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@BeforeEach
	public void setUp() {
		internmentDocumentModificationValidator = new InternmentDocumentModificationValidatorImpl(sharedDocumentPort, internmentEpisodeService);
		deleteEpicrisisService = new DeleteEpicrisisServiceImpl(internmentEpisodeService, internmentDocumentModificationValidator, sharedDocumentPort);
	}

	@Test
	void updateDocumentWithInvalidSignatureStatus() {
		when(sharedDocumentPort.getDocument(DOCUMENT_ID)).thenReturn(signedDocumentReduceInfoDto(2, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> deleteEpicrisisService.execute(INTERNMET_EPISODE_ID, DOCUMENT_ID, "Reason"));
		String expectedMessage = "No es posible llevar a cabo la acción dado que el documento fue firmado digitalmente";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	private DocumentReduceInfoDto signedDocumentReduceInfoDto(Integer userId, Short typeId) {
		DocumentReduceInfoDto result = new DocumentReduceInfoDto();
		result.setSourceId(INTERNMET_EPISODE_ID);
		result.setTypeId(typeId);
		result.setCreatedBy(userId);
		result.setCreatedOn(LocalDateTime.now());
		result.setSignatureStatus(ESignatureStatus.SIGNED);
		result.setIsConfirmed(true);
		return result;
	}

}
