package net.pladema.clinichistory.hospitalization.service.epicrisis.impl;

import net.pladema.clinichistory.documents.repository.DocumentRepository;
import net.pladema.clinichistory.documents.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.entity.EvolutionNoteDocument;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.SourceType;
import net.pladema.sgx.exceptions.NotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(showSql = false)
class CreateEpicrisisServiceImplTest {


    private CreateEpicrisisService createEpicrisisService;

    @Autowired
    private InternmentEpisodeRepository internmentEpisodeRepository;

    @Autowired
    private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    @Autowired
    private PatientDischargeRepository patientDischargeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentFactory documentFactory;

    @BeforeEach
    void setUp(){
        var internmentEpisodeService = new InternmentEpisodeServiceImpl(
                internmentEpisodeRepository,
                evolutionNoteDocumentRepository,
                patientDischargeRepository,
                documentService
        );
        createEpicrisisService = new CreateEpicrisisServiceImpl(
                documentFactory,
                internmentEpisodeService);
    }

    @Test
    void createDocumentWithEpisodeThatNotExists() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEpicrisisService.execute(validEpicrisis(9, -14))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidInstitutionId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(validEpicrisis(null, 8))
        );
        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidEpisodeId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(validEpicrisis(8, null))
        );
        String expectedMessage = "El id del encuentro asociado es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInternmentInOtherInstitution() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(null));
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEpicrisisService.execute(validEpicrisis(internmentEpisode.getInstitutionId()+1, internmentEpisode.getId()))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithEpicrisis() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(1l));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(validEpicrisis(8, internmentEpisode.getId()))
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
                createEpicrisisService.execute(epicrisis)
        );
        String expectedMessage = "mainDiagnosis: {value.mandatory}";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void createDocumentWithInvalidDiagnosis() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());

        epicrisis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );


        epicrisis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED")),
                new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED"))));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        String expectedMessage = "Diagnósticos secundarios repetidos";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidImmunizations() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());

        epicrisis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setImmunizations(List.of(new ImmunizationBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
    }

    @Test
    void createDocumentWithInvalidAnthropometricData() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());

        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));

        epicrisis.setAnthropometricData(newAnthropometricData("10001", localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0 y 1000"));

        epicrisis.setAnthropometricData(newAnthropometricData("-50", null));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0 y 1000"));
    }

    @Test
    void createDocumentWithInvalidVitalSign() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));
        epicrisis.setVitalSigns(newVitalSigns(null, localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("vitalSigns.bloodOxygenSaturation.value: {value.mandatory}"));

        epicrisis.setVitalSigns(newVitalSigns("Value", LocalDateTime.of(2020,9,9,1,5,6)));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe ser posterior a la fecha de internación"));
    }

    @Test
    void createDocumentWithInvalidPersonalHistories() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setPersonalHistories(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("personalHistories: {value.mandatory}"));

        epicrisis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setPersonalHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED")),
                new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED"))));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("Antecedentes personales repetidos"));

    }

    @Test
    void createDocument_withInvalidFamilyHistories() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setFamilyHistories(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("familyHistories: {value.mandatory}"));

        epicrisis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setFamilyHistories(List.of(new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED")),
                new HealthHistoryConditionBo(new SnomedBo("REPEATED", "REPEATED"))));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
        Assertions.assertTrue(exception.getMessage().contains("Antecedentes familiares repetidos"));
    }

    @Test
    void createDocumentWithInvalidMedications() {
        var internmentEpisode = newValidInternmentEpisodeToCreateEpicrisis();
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var epicrisis = validEpicrisis(internmentEpisodeSaved.getInstitutionId(), internmentEpisode.getId());
        epicrisis.setMedications(null);
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setMedications(List.of(new MedicationBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );

        epicrisis.setMedications(List.of(new MedicationBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEpicrisisService.execute(epicrisis)
        );
    }

    private EpicrisisBo validEpicrisis(Integer institutionId, Integer encounterId){
        var result = new EpicrisisBo();
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        result.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        result.setPersonalHistories(Lists.emptyList());
        result.setFamilyHistories(Lists.emptyList());
        result.setMedications(Lists.emptyList());
        result.setDiagnosis(Lists.emptyList());
        result.setImmunizations(Lists.emptyList());
        result.setAllergies(Lists.emptyList());
        return result;
    }

    private InternmentEpisode newValidInternmentEpisodeToCreateEpicrisis(){
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(null));
        var anamanesis = documentRepository.save(new Document(internmentEpisode.getId(), DocumentStatus.FINAL, DocumentType.ANAMNESIS, SourceType.HOSPITALIZATION));
        internmentEpisode.setAnamnesisDocId(anamanesis.getId());
        internmentEpisodeRepository.save(internmentEpisode);
        var evolutionNote = documentRepository.save(new Document(internmentEpisode.getId(), DocumentStatus.FINAL, DocumentType.EVALUATION_NOTE, SourceType.HOSPITALIZATION));
        evolutionNoteDocumentRepository.save(new EvolutionNoteDocument(evolutionNote.getId(), internmentEpisode.getId()));
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

}