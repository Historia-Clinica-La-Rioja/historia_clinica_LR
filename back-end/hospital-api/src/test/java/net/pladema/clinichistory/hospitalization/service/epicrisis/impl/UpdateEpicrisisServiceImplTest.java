package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentDocumentModificationValidatorImpl;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;


class UpdateEpicrisisServiceImplTest extends UnitRepository {

	private final Long OLD_DOCUMENT_ID = 1l;
	private final Integer INTERNMET_EPISODE_ID = 8;
	private final Integer INSTITUTION_ID = 1;
	private final Short DOCUMENT_TYPE_ID = DocumentType.EPICRISIS;

	private UpdateEpicrisisService updateEpicrisisService;

	@Mock
	private DateTimeProvider dateTimeProvider;

	@Mock
	private DocumentFactory documentFactory;

	private InternmentDocumentModificationValidator documentModificationValidator;

	@Mock
	private SharedDocumentPort sharedDocumentPort;

	@Mock
	private InternmentEpisodeService internmentEpisodeService;

	@Mock
	private EpicrisisService epicrisisService;

	private EpicrisisValidator epicrisisValidator;

	@BeforeEach
	public void setUp() {
		epicrisisValidator = new EpicrisisValidator(internmentEpisodeService);
		documentModificationValidator = new InternmentDocumentModificationValidatorImpl(sharedDocumentPort, internmentEpisodeService);
		updateEpicrisisService = new UpdateEpicrisisServiceImpl(documentModificationValidator, sharedDocumentPort, epicrisisValidator, internmentEpisodeService, dateTimeProvider, documentFactory, epicrisisService);
	}

	@Test
	void updateDocumentWithInvalidInstitutionId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEpicrisis(null, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "El id de la institución es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutSourceId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEpicrisis(INSTITUTION_ID, null, null)));
		String expectedMessage = "El id del encuentro asociado es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutReason() {
		when(epicrisisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, basicEpicrisisInfo(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "Para llevar a cabo la acción el motivo es un campo requerido";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidUser() {
		when(epicrisisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(2, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "El documento únicamente puede ser intervenido por el usuario que lo ha creado";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidDocumentType() {
		when(epicrisisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, (short) 4));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "No es posible llevar a cabo la acción dado que el id ingresado no coincide con el tipo de documento";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidInternmentEpisodeId() {
		when(epicrisisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEpicrisisService.execute(10, OLD_DOCUMENT_ID, validUpdateEpicrisis(INSTITUTION_ID, 10, null)));
		String expectedMessage = "El id del episodio de internación ingresado no coincide con el id de episodio de internacion del documento";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithMedicalDischarged() {
		when(epicrisisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(anyInt())).thenReturn(true);
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "No es posible llevar a cabo la acción dado que se ha realizado un alta médica";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithExpiredHour() {
		when(epicrisisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(expiredDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(INTERNMET_EPISODE_ID)).thenReturn(false);
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEpicrisisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEpicrisis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "La acción puede llevarse a cabo únicamente dentro de las 24 hs posteriores a su creación";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	private EpicrisisBo basicEpicrisisInfo(Integer institutionId, Integer encounterId, Long id) {
		var epicrisisBo = new EpicrisisBo();
		epicrisisBo.setId(id);
		epicrisisBo.setInstitutionId(institutionId);
		epicrisisBo.setEncounterId(encounterId);
		epicrisisBo.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
		epicrisisBo.setDiagnosis(Collections.emptyList());
		epicrisisBo.setImmunizations(Collections.emptyList());
		epicrisisBo.setFamilyHistories(Collections.emptyList());
		epicrisisBo.setPersonalHistories(Collections.emptyList());
		epicrisisBo.setMedications(Collections.emptyList());
		epicrisisBo.setAllergies(Collections.emptyList());
		return epicrisisBo;
	}

	private EpicrisisBo validUpdateEpicrisis(Integer institutionId, Integer encounterId, Long id) {
		EpicrisisBo result = basicEpicrisisInfo(institutionId, encounterId, id);
		result.setModificationReason("Test");
		return result;
	}

	private EpicrisisBo invalidUpdateEpicrisis(Integer institutionId, Integer encounterId, Long id) {
		EpicrisisBo result = validUpdateEpicrisis(institutionId, encounterId, id);
		List<DiagnosisBo> diagnosis = new ArrayList<>();
		diagnosis.add(new DiagnosisBo(new SnomedBo("MAIN", "MAIN")));
		result.setDiagnosis(diagnosis);
		return result;
	}

	private DocumentReduceInfoDto validDocumentReduceInfoDto(Integer userId, Short typeId) {
		DocumentReduceInfoDto result = new DocumentReduceInfoDto();
		result.setSourceId(INTERNMET_EPISODE_ID);
		result.setTypeId(typeId);
		result.setCreatedBy(userId);
		result.setCreatedOn(LocalDateTime.now());
		return result;
	}

	private DocumentReduceInfoDto expiredDocumentReduceInfoDto(Integer userId, Short typeId) {
		DocumentReduceInfoDto result = new DocumentReduceInfoDto();
		result.setSourceId(INTERNMET_EPISODE_ID);
		result.setTypeId(typeId);
		result.setCreatedBy(userId);
		result.setCreatedOn(LocalDateTime.now().minusDays(1).minusHours(1));
		return result;
	}

}
