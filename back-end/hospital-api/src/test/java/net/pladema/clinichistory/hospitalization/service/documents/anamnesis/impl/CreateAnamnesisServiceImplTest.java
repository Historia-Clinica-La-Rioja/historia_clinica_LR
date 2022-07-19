package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.createDocumentFile.CreateDocumentFile;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.anamnesis.AnamnesisValidator;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.CreateAnamnesisServiceImpl;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;


class CreateAnamnesisServiceImplTest extends UnitRepository {

	private CreateAnamnesisServiceImpl createAnamnesisServiceImpl;

	@Autowired
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@Mock
	private DateTimeProvider dateTimeProvider;

	@Autowired
	private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

	@Autowired
	private PatientDischargeRepository patientDischargeRepository;

	@Autowired
	private MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	@Mock
	private DocumentService documentService;

	@Mock
	private DocumentFactory documentFactory;

	@Mock
	private CreateDocumentFile createDocumentFile;

	private InternmentEpisodeStorage internmentEpisodeStorage;
	@Mock
	private FeatureFlagsService featureFlagsService;

	private AnamnesisValidator anamnesisValidator;

	@BeforeEach
	public void setUp() {
		var internmentEpisodeService = new InternmentEpisodeServiceImpl(
				internmentEpisodeRepository,
                dateTimeProvider, evolutionNoteDocumentRepository,
				patientDischargeRepository,
				documentService,
                medicalCoveragePlanRepository,
                internmentEpisodeStorage, featureFlagsService);
		createAnamnesisServiceImpl =
				new CreateAnamnesisServiceImpl(documentFactory, internmentEpisodeService, dateTimeProvider,
						new AnamnesisValidator(featureFlagsService));
	}

	@Test
	void createDocumentWithInvalidInstitutionId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(validAnamnesis(null, 8))
		);
		String expectedMessage = "El id de la institución es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);
	}

	@Test
	void createDocumentWithInvalidEpisodeId() {
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(validAnamnesis(8, null))
		);
		String expectedMessage = "El id del encuentro asociado es obligatorio";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);
	}

	@Test
	void createDocumentWithEpisodeThatNotExists() {
		Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
				createAnamnesisServiceImpl.execute(validAnamnesis(8, 10))
		);
		String expectedMessage = "internmentepisode.not.found";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);
	}

	//TODO: la PK de internación es institucion_id + algo mas
	@Test
	void createDocumentWithInternmentInOtherInstitution() {
		var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithAnamnesis(null));
		Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
				createAnamnesisServiceImpl.execute(validAnamnesis(internmentEpisode.getInstitutionId()+1 , internmentEpisode.getId()))
		);
		String expectedMessage = "internmentepisode.not.found";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);
	}

	@Test
	void createDocumentWithAnamnesisAlreadyDone() {
		var internmentEpisode = save(newInternmentEpisodeWithAnamnesis(1l));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.
					execute(validAnamnesis(internmentEpisode.getInstitutionId(), internmentEpisode.getId()))
		);
		String expectedMessage = "Esta internación ya posee una anamnesis";
		String actualMessage = exception.getMessage();
		assertEquals(actualMessage,expectedMessage);
	}

	@Test
	void createDocumentWithoutMainDiagnosis() {
		var internmentEpisode = save(newInternmentEpisodeWithAnamnesis(null));
		when(featureFlagsService.isOn(any())).thenReturn(true);
		var anamnesis = validAnamnesis(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
		anamnesis.setMainDiagnosis(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		String expectedMessage = "Diagnóstico principal obligatorio";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void createDocumentWithMainDiagnosisDuplicated() {
		var internmentEpisode = save(newInternmentEpisodeWithAnamnesis(null));
		var anamnesis  = validAnamnesis(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
		anamnesis.setMainDiagnosis(new HealthConditionBo(new SnomedBo("SECONDARY", "SECONDARY")));
		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("SECONDARY", "SECONDARY"))));
		anamnesis.setEncounterId(internmentEpisode.getId());

		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		String expectedMessage = "Diagnostico principal duplicado en los secundarios";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void createDocument_withInvalidDiagnosis() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setDiagnosis(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("diagnosis: {value.mandatory}"));

		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);


		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED")),
				new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED"))));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Diagnósticos secundarios repetidos"));

	}

	@Test
	void createDocumentWithInvalidPersonalHistories() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setPersonalHistories(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("personalHistories: {value.mandatory}"));

		anamnesis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED")),
				new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED"))));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Antecedentes personales repetidos"));

	}

	@Test
	void createDocumentWithInvalidFamilyHistories() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setFamilyHistories(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("familyHistories: {value.mandatory}"));

		anamnesis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED")),
				new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED"))));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Antecedentes familiares repetidos"));
	}

	@Test
	void createDocumentWithInvalidProcedures() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setProcedures(null);

		anamnesis.setProcedures(List.of(new ProcedureBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setProcedures(List.of(new ProcedureBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setProcedures(List.of(new ProcedureBo(new SnomedBo("REPEATED", "REPEATED")),
				new ProcedureBo(new SnomedBo("REPEATED", "REPEATED"))));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Procedimientos repetidos"));
	}

	@Test
	void createDocumentWithInvalidMedications() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setMedications(null);
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setMedications(List.of(new MedicationBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setMedications(List.of(new MedicationBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);
	}

	@Test
	void createDocumentWithInvalidImmunizations() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setImmunizations(null);
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);

		anamnesis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);
	}

	@Test
	void createDocumentWithInvalidAnthropometricData() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());

		LocalDateTime localDateTime = LocalDateTime.of(
				LocalDate.of(2020, 10,29),
				LocalTime.of(11,20));

		anamnesis.setAnthropometricData(newAnthropometricData("10001", localDateTime));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0.0 y 1000.0"));

		anamnesis.setAnthropometricData(newAnthropometricData("-50", null));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0.0 y 1000.0"));
	}

	@Test
	void createDocumentWithInvalidRiskFactor() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
		var internmentEpisodeSaved = save(internmentEpisode);

		var anamnesis = validAnamnesis(internmentEpisodeSaved.getInstitutionId(), internmentEpisodeSaved.getId());
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		LocalDateTime localDateTime = LocalDateTime.of(
				LocalDate.of(2020, 10,29),
				LocalTime.of(11,20));
		anamnesis.setRiskFactors(newRiskFactors(null, localDateTime));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("riskFactors.bloodOxygenSaturation.value: {value.mandatory}"));

		anamnesis.setRiskFactors(newRiskFactors("Value", LocalDateTime.of(2020,9,9,1,5,6)));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.execute(anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe ser posterior a la fecha de internación"));
	}


	private AnamnesisBo validAnamnesis(Integer institutionId, Integer encounterId){
		var anamnesis = new AnamnesisBo();
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

	private RiskFactorBo newRiskFactors(String value, LocalDateTime time) {
		var vs = new RiskFactorBo();
		vs.setBloodOxygenSaturation(new ClinicalObservationBo(null, value, time));
		return vs;
	}

	private AnthropometricDataBo newAnthropometricData(String value, LocalDateTime time) {
		var adb = new AnthropometricDataBo();
		adb.setBloodType(new ClinicalObservationBo(null, value, time));
		adb.setWeight(new ClinicalObservationBo(null, value, time));
		return adb;
	}


	private InternmentEpisode newInternmentEpisodeWithAnamnesis(Long anamnesisId) {
		InternmentEpisode internmentEpisode = new InternmentEpisode();
		internmentEpisode.setPatientId(1);
		internmentEpisode.setBedId(1);
		internmentEpisode.setStatusId((short) 1);
		internmentEpisode.setAnamnesisDocId(anamnesisId);
		internmentEpisode.setInstitutionId(8);
		return internmentEpisode;
	}

}
