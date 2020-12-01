package net.pladema.clinichistory.hospitalization.service.documents.anamnesis.impl;

import net.pladema.clinichistory.documents.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.anamnesis.impl.CreateAnamnesisServiceImpl;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CreateAnamnesisServiceImplTest {

	private CreateAnamnesisServiceImpl createAnamnesisServiceImpl;

	@Autowired
	private InternmentEpisodeRepository internmentEpisodeRepository;

	@Autowired
	private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

	@Autowired
	private PatientDischargeRepository patientDischargeRepository;

	@MockBean
	private DocumentService documentService;

	@MockBean
	private DocumentFactory documentFactory;

	@MockBean
	private CreateDocumentFile createDocumentFile;

	@MockBean
	private FeatureFlagsService featureFlagsService;

	@Before
	public void setUp() {
		var internmentEpisodeService = new InternmentEpisodeServiceImpl(
				internmentEpisodeRepository,
				evolutionNoteDocumentRepository,
				patientDischargeRepository,
				documentService
		);
		createAnamnesisServiceImpl = new CreateAnamnesisServiceImpl(documentFactory, internmentEpisodeService, createDocumentFile, featureFlagsService);
	}

	@Test(expected = NotFoundException.class)
	public void createDocument_withEpisodeThatNotExists() throws IOException, PDFDocumentException {
		var anamnesis = validAnamnesis();
		createAnamnesisServiceImpl.createDocument(8, anamnesis);
	}

	@Test(expected = ConstraintViolationException.class)
	public void createDocument_withAnamnesisAlreadyDone() throws IOException, PDFDocumentException {
		var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithAnamnesis(1l));
		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisode.getId());
		createAnamnesisServiceImpl.createDocument(internmentEpisode.getInstitutionId(), anamnesis);
	}

	//TODO: la PK de internación es institucion_id + algo mas
	@Test(expected = NotFoundException.class)
	public void createDocument_withInternmentInOtherInstitution() throws IOException, PDFDocumentException {
		var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithAnamnesis(null));
		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisode.getId());
		createAnamnesisServiceImpl.createDocument(internmentEpisode.getInstitutionId()+1, anamnesis);
	}

	@Test
	public void createDocument_withoutMainDiagnosis() {
		var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithAnamnesis(null));
		when(featureFlagsService.isOn(any())).thenReturn(true);
		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisode.getId());
		anamnesis.setMainDiagnosis(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisode.getInstitutionId(), anamnesis)
		);
		String expectedMessage = "Diagnóstico principal obligatorio";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void createDocument_withMainDiagnosisDuplicated() {
		var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithAnamnesis(null));
		var anamnesis  = validAnamnesis();
		anamnesis.setMainDiagnosis(new HealthConditionBo(new SnomedBo("SECONDARY", "SECONDARY")));
		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("SECONDARY", "SECONDARY"))));
		anamnesis.setEncounterId(internmentEpisode.getId());

		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisode.getInstitutionId(), anamnesis)
		);
		String expectedMessage = "Diagnostico principal duplicado en los secundarios";
		String actualMessage = exception.getMessage();

		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void createDocument_withInvalidDiagnosis() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setDiagnosis(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("diagnosis: {value.mandatory}"));

		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);


		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED")),
				new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED"))));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Diagnósticos secundarios repetidos"));

	}

	@Test
	public void createDocument_withInvalidPersonalHistories() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setPersonalHistories(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("personalHistories: {value.mandatory}"));

		anamnesis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED")),
				new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED"))));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Antecedentes personales repetidos"));

	}

	@Test
	public void createDocument_withInvalidFamilyHistories() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setFamilyHistories(null);
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("familyHistories: {value.mandatory}"));

		anamnesis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED")),
				new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED"))));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Antecedentes familiares repetidos"));
	}

	@Test
	public void createDocument_withInvalidProcedures() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setProcedures(null);

		anamnesis.setProcedures(List.of(new ProcedureBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setProcedures(List.of(new ProcedureBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setProcedures(List.of(new ProcedureBo(new SnomedBo("REPEATED", "REPEATED")),
				new ProcedureBo(new SnomedBo("REPEATED", "REPEATED"))));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Procedimientos repetidos"));
	}

	@Test
	public void createDocument_withInvalidMedications() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setMedications(null);
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setMedications(List.of(new MedicationBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setMedications(List.of(new MedicationBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
	}

	@Test
	public void createDocument_withInvalidImmunizations() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		anamnesis.setImmunizations(null);
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo("", ""))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);

		anamnesis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo(null, null))));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
	}

	@Test
	public void createDocument_withInvalidAnthropometricData() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());

		LocalDateTime localDateTime = LocalDateTime.of(
				LocalDate.of(2020, 10,29),
				LocalTime.of(11,20));

		anamnesis.setAnthropometricData(newAnthropometricData("10001", localDateTime));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0 y 1000"));

		anamnesis.setAnthropometricData(newAnthropometricData("-50", null));
		Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0 y 1000"));
	}

	@Test
	public void createDocument_withInvalidVitalSign() {
		var internmentEpisode = newInternmentEpisodeWithAnamnesis(null);
		internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
		var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

		var anamnesis = validAnamnesis();
		anamnesis.setEncounterId(internmentEpisodeSaved.getId());
		LocalDateTime localDateTime = LocalDateTime.of(
				LocalDate.of(2020, 10,29),
				LocalTime.of(11,20));
		anamnesis.setVitalSigns(newVitalSigns(null, localDateTime));
		Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("vitalSigns.bloodOxygenSaturation.value: {value.mandatory}"));

	/*	anamnesis.setVitalSigns(newVitalSigns("Value", null));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
			createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe es obligatoria"));*/


		anamnesis.setVitalSigns(newVitalSigns("Value", LocalDateTime.of(2020,9,9,1,5,6)));
		exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
				createAnamnesisServiceImpl.createDocument(internmentEpisodeSaved.getInstitutionId(), anamnesis)
		);
		Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe ser posterior a la fecha de internación"));
	}


	private AnamnesisBo validAnamnesis(){
		var anamnesis = new AnamnesisBo();
		anamnesis.setConfirmed(true);
		anamnesis.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
		anamnesis.setDiagnosis(Lists.emptyList());
		anamnesis.setPersonalHistories(Lists.emptyList());
		anamnesis.setFamilyHistories(Lists.emptyList());
		anamnesis.setMedications(Lists.emptyList());
		anamnesis.setImmunizations(Lists.emptyList());
		anamnesis.setAllergies(Lists.emptyList());
		return anamnesis;
	}

	private VitalSignBo newVitalSigns(String value, LocalDateTime time) {
		var vs = new VitalSignBo();
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
