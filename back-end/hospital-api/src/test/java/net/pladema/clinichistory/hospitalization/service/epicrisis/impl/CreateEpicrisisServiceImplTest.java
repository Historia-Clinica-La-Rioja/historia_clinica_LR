package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolationException;

import ar.lamansys.sgh.clinichistory.application.document.validators.AnthropometricDataValidator;
import ar.lamansys.sgh.clinichistory.application.document.validators.EffectiveRiskFactorTimeValidator;
import ar.lamansys.sgh.clinichistory.application.document.validators.GeneralDocumentValidator;
import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.GeneratedPdfResponseService;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentTypeById.FetchEpisodeDocumentTypeById;
import net.pladema.clinichistory.hospitalization.application.getanestheticreportdraft.GetLastAnestheticReportDraftFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.application.port.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.application.validateadministrativedischarge.ValidateAdministrativeDischarge;
import net.pladema.clinichistory.hospitalization.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisValidator;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.patient.service.PatientService;
import net.pladema.person.service.PersonService;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class CreateEpicrisisServiceImplTest extends UnitRepository {

    private CreateEpicrisisService createEpicrisisService;

    @Autowired
    private InternmentEpisodeRepository internmentEpisodeRepository;

    @Autowired
    private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    @Autowired
    private PatientDischargeRepository patientDischargeRepository;

    @Autowired
    private DocumentRepository documentRepository;

	@Autowired
	private MedicalCoveragePlanRepository medicalCoveragePlanRepository;

	@Mock
	private DateTimeProvider dateTimeProvider;

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentFactory documentFactory;

	@Mock
	private FeatureFlagsService featureFlagsService;

	private InternmentEpisodeService internmentEpisodeService;

	@Mock
	private InternmentEpisodeStorage internmentEpisodeStorage;

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@Mock
	private GeneratedPdfResponseService generatedPdfResponseService;

	@Mock
	private PatientService patientService;

	@Mock
	private PersonService personService;

	@Mock
	private InstitutionService institutionService;

	@Mock
	private FetchEpisodeDocumentTypeById fetchEpisodeDocumentTypeById;

	@Mock
	private HealthcareProfessionalService healthcareProfessionalService;

	@Mock
	private GetLicenseNumberByProfessional getLicenseNumberByProfessional;

	@Mock
    private GetLastAnestheticReportDraftFromInternmentEpisode getLastAnestheticReportDraftFromInternmentEpisode;

    @Mock
    private ValidateAdministrativeDischarge validateAdministrativeDischarge;

    @BeforeEach
    void setUp(){
        var internmentEpisodeService = new InternmentEpisodeServiceImpl(
				internmentEpisodeRepository,
				evolutionNoteDocumentRepository,
				patientDischargeRepository,
				medicalCoveragePlanRepository,
				documentService,
				internmentEpisodeStorage,
				featureFlagsService,
				generatedPdfResponseService,
				patientService,
				personService,
				institutionService,
				fetchEpisodeDocumentTypeById,
				healthcareProfessionalService,
				getLicenseNumberByProfessional,
                validateAdministrativeDischarge,
                getLastAnestheticReportDraftFromInternmentEpisode
                );

        var generalDocumentValidator = new GeneralDocumentValidator(
                new AnthropometricDataValidator(),
                new EffectiveRiskFactorTimeValidator()
        );
        createEpicrisisService = new CreateEpicrisisServiceImpl(
                documentFactory,
                internmentEpisodeService,
                dateTimeProvider,
				new EpicrisisValidator(internmentEpisodeService, generalDocumentValidator)
		);
    }

    @Test
    void createDocumentWithEpisodeThatNotExists() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEpicrisisService.execute(validEpicrisis(9, -14), false)
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidInstitutionId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(validEpicrisis(null, 8), false)
        );
        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidEpisodeId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(validEpicrisis(8, null), false)
        );
        String expectedMessage = "El id del encuentro asociado es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInternmentInOtherInstitution() {
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(null));
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEpicrisisService.execute(validEpicrisis(internmentEpisode.getInstitutionId()+1, internmentEpisode.getId()), false)
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithEpicrisis() {
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(1l));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(validEpicrisis(8, internmentEpisode.getId()), false)
        );
        String expectedMessage = "Esta internación no puede crear una epicrisis";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }


    @Test
    void createDocumentWithoutMainDiagnosis() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        var epicrisis = validEpicrisis(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setMainDiagnosis(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, false)
        );
        String expectedMessage = "mainDiagnosis: {value.mandatory}";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void createDocumentWithInvalidDiagnosis() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());

        epicrisis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );


        epicrisis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED")),
                new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED"))));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        String expectedMessage = "Diagnósticos secundarios repetidos";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidImmunizations() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());

        epicrisis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
    }

    @Test
    void createDocumentWithInvalidAnthropometricData() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());

        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));

        epicrisis.setAnthropometricData(newAnthropometricData("10001", localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0.0 y 1000.0"));

        epicrisis.setAnthropometricData(newAnthropometricData("-50", null));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0.0 y 1000.0"));
    }

    @Test
    void createDocumentWithInvalidRiskFactor() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));
        epicrisis.setRiskFactors(newRiskFactors(null, localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("riskFactors.bloodOxygenSaturation.value: {value.mandatory}"));

        epicrisis.setRiskFactors(newRiskFactors("Value", LocalDateTime.of(2020,9,9,1,5,6)));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe ser posterior a la fecha de internación"));
    }

    @Test
    void createDocumentWithInvalidPersonalHistories() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setPersonalHistories(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("personalHistories: {value.mandatory}"));

        epicrisis.setPersonalHistories(new ReferableItemBo<>(List.of(new PersonalHistoryBo(new SnomedBo("", ""))), true));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setPersonalHistories(new ReferableItemBo<>(List.of(new PersonalHistoryBo(new SnomedBo(null, null))), true));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setPersonalHistories(new ReferableItemBo<>(List.of(new PersonalHistoryBo(new SnomedBo("REPEATED", "REPEATED")),
                new PersonalHistoryBo(new SnomedBo("REPEATED", "REPEATED"))), true));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("Antecedentes personales repetidos"));

    }

    @Test
    void createDocument_withInvalidFamilyHistories() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setFamilyHistories(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("familyHistories: {value.mandatory}"));

        epicrisis.setFamilyHistories(new ReferableItemBo<>(List.of(new FamilyHistoryBo(new SnomedBo("", ""))), true));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setFamilyHistories(new ReferableItemBo<>(List.of(new FamilyHistoryBo(new SnomedBo(null, null))), true));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setFamilyHistories(new ReferableItemBo<>(List.of(new FamilyHistoryBo(new SnomedBo("REPEATED", "REPEATED")),
                new FamilyHistoryBo(new SnomedBo("REPEATED", "REPEATED"))), true));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
        Assertions.assertTrue(exception.getMessage().contains("Antecedentes familiares repetidos"));
    }

    @Test
    void createDocumentWithInvalidMedications() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDateTime.of(2020,10,10,00,00,00));
        var internmentEpisodeSaved = save(internmentEpisode);
		boolean draft = false;
        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setMedications(null);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setMedications(List.of(new MedicationBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );

        epicrisis.setMedications(List.of(new MedicationBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis, draft)
        );
    }

    private EpicrisisBo validEpicrisis(Integer institutionId, Integer encounterId){
        var result = new EpicrisisBo();
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        result.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        result.setPersonalHistories(new ReferableItemBo<>());
        result.setFamilyHistories(new ReferableItemBo<>());
        result.setMedications(Collections.emptyList());
        result.setDiagnosis(Collections.emptyList());
		result.setProcedures(Collections.emptyList());
        result.setImmunizations(Collections.emptyList());
        result.setAllergies(new ReferableItemBo<>());
        return result;
    }

    private InternmentEpisode newValidInternmentEpisodeToCreateEpicrisis(){
        var internmentEpisode = save(newInternmentEpisodeWithEpicrisis(null));
        var anamanesis = save(new Document(internmentEpisode.getId(), DocumentStatus.FINAL, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION, -1, -1));
        internmentEpisode.setAnamnesisDocId(anamanesis.getId());
        save(internmentEpisode);
        var evolutionNote = save(new Document(internmentEpisode.getId(), DocumentStatus.FINAL, DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION, -1, -1));
        save(new EvolutionNoteDocument(evolutionNote.getId(), internmentEpisode.getId()));
        return internmentEpisode;
    }

    private InternmentEpisode newInternmentEpisodeWithEpicrisis(Long epicrisisId) {
        InternmentEpisode internmentEpisode = new InternmentEpisode();
        internmentEpisode.setPatientId(1);
        internmentEpisode.setBedId(1);
        internmentEpisode.setStatusId((short) 1);
        internmentEpisode.setEpicrisisDocId(epicrisisId);
        internmentEpisode.setInstitutionId(8);
        return internmentEpisode;
    }

    private RiskFactorBo newRiskFactors(String value, LocalDateTime time) {
        var vs = new RiskFactorBo();
        vs.setBloodOxygenSaturation(new ClinicalObservationBo(null, value, time, null));
        return vs;
    }

    private AnthropometricDataBo newAnthropometricData(String value, LocalDateTime time) {
        var adb = new AnthropometricDataBo();
        adb.setBloodType(new ClinicalObservationBo(null, value, time, null));
        adb.setWeight(new ClinicalObservationBo(null, value, time, null));
        return adb;
    }

}