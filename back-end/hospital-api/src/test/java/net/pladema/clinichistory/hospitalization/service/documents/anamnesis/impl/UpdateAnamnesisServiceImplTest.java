package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;

import javax.validation.ConstraintViolationException;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.service.InternmentDocumentModificationValidator;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisService;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisValidator;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.UpdateAnamnesisServiceImpl;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentDocumentModificationValidatorImpl;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;


class UpdateAnamnesisServiceImplTest extends UnitRepository {

	private final Long OLD_DOCUMENT_ID = 1l;
	private final Integer INTERNMET_EPISODE_ID = 8;
	private final Integer INSTITUTION_ID = 1;
	private final Short DOCUMENT_TYPE_ID = DocumentType.ANAMNESIS;

	private UpdateAnamnesisServiceImpl updateAnamnesisService;

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
	private AnamnesisService anamnesisService;

	private AnamnesisValidator anamnesisValidator;

	@BeforeEach
	public void setUp() {
		anamnesisValidator = new AnamnesisValidator(featureFlagsService);
		documentModificationValidator = new InternmentDocumentModificationValidatorImpl(sharedDocumentPort, internmentEpisodeService);
		updateAnamnesisService = new UpdateAnamnesisServiceImpl(documentModificationValidator, sharedDocumentPort, anamnesisValidator, internmentEpisodeService, dateTimeProvider, documentFactory, anamnesisService);
	}

	@Test
	void updateDocumentWithInvalidInstitutionId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateAnamnesis(null, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "El id de la institución es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutSourceId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateAnamnesis(INSTITUTION_ID, null, null)));
		String expectedMessage = "El id del encuentro asociado es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutReason() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, basicAnamnesisInfo(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "Para llevar a cabo la acción el motivo es un campo requerido";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidUser() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(2, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "El documento únicamente puede ser intervenido por el usuario que lo ha creado";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidDocumentType() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, (short) 2));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "No es posible llevar a cabo la acción dado que el id ingresado no coincide con el tipo de documento";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithInvalidInternmentEpisodeId() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateAnamnesisService.execute(10, OLD_DOCUMENT_ID, validUpdateAnamnesis(INSTITUTION_ID, 10, null)));
		String expectedMessage = "El id del episodio de internación ingresado no coincide con el id de episodio de internacion del documento";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithMedicalDischarged() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(anyInt())).thenReturn(true);
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "No es posible llevar a cabo la acción dado que se ha realizado un alta médica";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithExpiredHour() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(expiredDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(INTERNMET_EPISODE_ID)).thenReturn(false);
		Exception exception = Assertions.assertThrows(InternmentDocumentException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "La acción puede llevarse a cabo únicamente dentro de las 24 hs posteriores a su creación";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	@Test
	void updateDocumentWithoutMainDiagnosis() {
		when(anamnesisService.getDocument(OLD_DOCUMENT_ID)).thenReturn(validUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID));
		when(sharedDocumentPort.getDocument(OLD_DOCUMENT_ID)).thenReturn(validDocumentReduceInfoDto(-1, DOCUMENT_TYPE_ID));
		when(internmentEpisodeService.haveMedicalDischarge(INTERNMET_EPISODE_ID)).thenReturn(false);
		when(featureFlagsService.isOn(any())).thenReturn(true);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () -> updateAnamnesisService.execute(INTERNMET_EPISODE_ID, OLD_DOCUMENT_ID, invalidUpdateAnamnesis(INSTITUTION_ID, INTERNMET_EPISODE_ID, null)));
		String expectedMessage = "Diagnóstico principal obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage, expectedMessage);
	}

	private AnamnesisBo basicAnamnesisInfo(Integer institutionId, Integer encounterId, Long id) {
		var anamnesis = new AnamnesisBo();
		anamnesis.setId(id);
		anamnesis.setInstitutionId(institutionId);
		anamnesis.setEncounterId(encounterId);
		anamnesis.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
		anamnesis.setDiagnosis(Collections.emptyList());
		anamnesis.setPersonalHistories(Collections.emptyList());
		anamnesis.setFamilyHistories(Collections.emptyList());
		anamnesis.setMedications(Collections.emptyList());
		anamnesis.setImmunizations(Collections.emptyList());
		anamnesis.setAllergies(Collections.emptyList());
		return anamnesis;
	}

	private AnamnesisBo validUpdateAnamnesis(Integer institutionId, Integer encounterId, Long id) {
		AnamnesisBo result = basicAnamnesisInfo(institutionId, encounterId, id);
		result.setModificationReason("Test");
		return result;
	}

	private AnamnesisBo invalidUpdateAnamnesis(Integer institutionId, Integer encounterId, Long id) {
		AnamnesisBo result = validUpdateAnamnesis(institutionId, encounterId, id);
		result.setMainDiagnosis(null);
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
