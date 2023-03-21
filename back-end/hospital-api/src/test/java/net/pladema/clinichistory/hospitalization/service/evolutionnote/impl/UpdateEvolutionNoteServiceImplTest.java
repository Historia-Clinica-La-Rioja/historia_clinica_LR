package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.EvolutionNoteValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.UpdateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentDocumentModificationValidatorImpl;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;

import net.pladema.sgx.session.infrastructure.input.service.FetchLoggedUserRolesExternalService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


class UpdateEvolutionNoteServiceImplTest extends UnitRepository {

	private final Long OLD_DOCUMENT_ID = 1l;
	private final Integer INTERNMET_EPISODE_ID = 8;
	private final Integer INSTITUTION_ID = 1;
	private final Short DOCUMENT_TYPE_ID = DocumentType.EVALUATION_NOTE;

	private UpdateEvolutionNoteService updateEvolutionNoteService;

	@Mock
	private DateTimeProvider dateTimeProvider;

	@Mock
	private DocumentFactory documentFactory;

	@Mock
	private FeatureFlagsService featureFlagsService;

	private InternmentDocumentModificationValidator documentModificationValidator;

	@Mock
	private SharedDocumentPort sharedDocumentPort;

	@Mock
	private InternmentEpisodeService internmentEpisodeService;

	@Mock
	private EvolutionNoteService evolutionNoteService;

	@Mock
	private FetchLoggedUserRolesExternalService fetchLoggedUserRolesExternalService;

	private EvolutionNoteValidator evolutionNoteValidator;

	@BeforeEach
	public void setUp() {
		evolutionNoteValidator = new EvolutionNoteValidator(fetchLoggedUserRolesExternalService, internmentEpisodeService);
		documentModificationValidator = new InternmentDocumentModificationValidatorImpl(sharedDocumentPort, internmentEpisodeService);
		updateEvolutionNoteService = new UpdateEvolutionNoteServiceImpl(documentModificationValidator, sharedDocumentPort, internmentEpisodeService, dateTimeProvider, documentFactory, evolutionNoteService, evolutionNoteValidator);
	}

	@Test
	void updateDocumentWithInvalidInstitutionId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEvolutionNote(null, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "El id de la institución es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutSourceId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEvolutionNote(INSTITUTION_ID, null, null)));
		String expectedMessage = "El id del encuentro asociado es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutReason() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, basicEvolutionNoteInfo(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "Para llevar a cabo la acción el motivo es un campo requerido";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidUser() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(2, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "El documento únicamente puede ser intervenido por el usuario que lo ha creado";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidDocumentType() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, (short) 3));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "No es posible llevar a cabo la acción dado que el id ingresado no coincide con el tipo de documento";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidInternmentEpisodeId() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEvolutionNoteService.execute(10, OLD_DOCUMENT_ID, validUpdateEvolutionNote(INSTITUTION_ID, 10, null)));
		String expectedMessage = "El id del episodio de internación ingresado no coincide con el id de episodio de internacion del documento";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithMedicalDischarged() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(anyInt())).thenReturn(true);
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "No es posible llevar a cabo la acción dado que se ha realizado un alta médica";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithExpiredHour() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(expiredDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(INTERNMET_EPISODE_ID)).thenReturn(false);
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "La acción puede llevarse a cabo únicamente dentro de las 24 hs posteriores a su creación";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithDiagnosisDuplicates() {
		when(evolutionNoteService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(INTERNMET_EPISODE_ID)).thenReturn(false);
		when(featureFlagsService.isOn(any())).thenReturn(true);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateEvolutionNoteService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, invalidUpdateEvolutionNote(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "Diagnostico principal duplicado en los secundarios";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	private EvolutionNoteBo basicEvolutionNoteInfo(Integer institutionId, Integer encounterId, Long id) {
		var evolutionBo = new EvolutionNoteBo();
		evolutionBo.setId(id);
		evolutionBo.setInstitutionId(institutionId);
		evolutionBo.setEncounterId(encounterId);
		evolutionBo.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
		evolutionBo.setDiagnosis(Collections.emptyList());
		evolutionBo.setImmunizations(Collections.emptyList());
		evolutionBo.setAllergies(Collections.emptyList());
		evolutionBo.setIsNursingEvolutionNote(false);
		return evolutionBo;
	}

	private EvolutionNoteBo validUpdateEvolutionNote(Integer institutionId, Integer encounterId, Long id) {
		EvolutionNoteBo result = basicEvolutionNoteInfo(institutionId, encounterId, id);
		result.setModificationReason("Test");
		return result;
	}

	private EvolutionNoteBo invalidUpdateEvolutionNote(Integer institutionId, Integer encounterId, Long id) {
		EvolutionNoteBo result = validUpdateEvolutionNote(institutionId, encounterId, id);
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
