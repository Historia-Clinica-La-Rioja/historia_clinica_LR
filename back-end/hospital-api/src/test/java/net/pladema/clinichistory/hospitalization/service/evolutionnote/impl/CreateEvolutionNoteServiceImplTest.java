package net.pladema.clinichistory.hospitalization.service.evolutionnote.impl;

import net.pladema.clinichistory.documents.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.generalstate.HealthConditionGeneralStateService;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.CreateEvolutionNoteService;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(showSql = false)
class CreateEvolutionNoteServiceImplTest {

    private CreateEvolutionNoteService createEvolutionNoteService;

    @Autowired
    private InternmentEpisodeRepository internmentEpisodeRepository;

    @Autowired
    private EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    @Autowired
    private PatientDischargeRepository patientDischargeRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentFactory documentFactory;

    @Mock
    private CreateDocumentFile createDocumentFile;

    @Mock
    private HealthConditionGeneralStateService healthConditionGeneralStateService;

    @BeforeEach
    void setUp(){
        var internmentEpisodeService = new InternmentEpisodeServiceImpl(
                internmentEpisodeRepository,
                evolutionNoteDocumentRepository,
                patientDischargeRepository,
                documentService
        );
        createEvolutionNoteService = new CreateEvolutionNoteServiceImpl(
                documentFactory,
                internmentEpisodeService,
                createDocumentFile,
                healthConditionGeneralStateService);
    }


    @Test
    void createDocumentWithEpisodeThatNotExists() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEvolutionNoteService.execute(9, validEvolutionNote())
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInternmentInOtherInstitution() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(null));
        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisode.getId());
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                createEvolutionNoteService.execute(internmentEpisode.getInstitutionId()+1, evolutionNote)
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithEpicrisis() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(1l));
        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisode.getId());
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(8, evolutionNote)
        );
        String expectedMessage = "Esta internación ya posee una epicrisis";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocument_withMainDiagnosisDuplicated() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(null));
        var evolutionNote  = validEvolutionNote();
        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("SECONDARY", "SECONDARY"))));
        evolutionNote.setEncounterId(internmentEpisode.getId());

        when(healthConditionGeneralStateService.getMainDiagnosisGeneralState(any())).thenReturn(new HealthConditionBo(new SnomedBo("SECONDARY", "SECONDARY")));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisode.getInstitutionId(), evolutionNote)
        );
        String expectedMessage = "Diagnostico principal duplicado en los secundarios";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createDocumentWithInvalidDiagnosis() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisodeSaved.getId());

        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );


        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );

        evolutionNote.setDiagnosis(List.of(new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED")),
                new DiagnosisBo(new SnomedBo("REPEATED", "REPEATED"))));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
        String expectedMessage = "Diagnósticos secundarios repetidos";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidImmunizations() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisodeSaved.getId());

        evolutionNote.setImmunizations(List.of(new ImmunizationBo(new SnomedBo("", ""))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );

        evolutionNote.setImmunizations(List.of(new ImmunizationBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
    }

    @Test
    void createDocumentWithInvalidProcedures() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisodeSaved.getId());

        evolutionNote.setProcedures(List.of(new ProcedureBo(new SnomedBo(null, null))));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );

        evolutionNote.setProcedures(List.of(new ProcedureBo(new SnomedBo("REPEATED", "REPEATED")),
                new ProcedureBo(new SnomedBo("REPEATED", "REPEATED"))));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("Procedimientos repetidos"));
    }

    @Test
    void createDocumentWithInvalidAnthropometricData() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisodeSaved.getId());

        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));

        evolutionNote.setAnthropometricData(newAnthropometricData("10001", localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0 y 1000"));

        evolutionNote.setAnthropometricData(newAnthropometricData("-50", null));
        Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("peso: La medición debe estar entre 0 y 1000"));
    }

    @Test
    void createDocumentWithInvalidVitalSign() {
        var internmentEpisode = newInternmentEpisodeWithEpicrisis(null);
        internmentEpisode.setEntryDate(LocalDate.of(2020,10,10));
        var internmentEpisodeSaved = internmentEpisodeRepository.saveAndFlush(internmentEpisode);

        var evolutionNote = validEvolutionNote();
        evolutionNote.setEncounterId(internmentEpisodeSaved.getId());
        LocalDateTime localDateTime = LocalDateTime.of(
                LocalDate.of(2020, 10,29),
                LocalTime.of(11,20));
        evolutionNote.setVitalSigns(newVitalSigns(null, localDateTime));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("vitalSigns.bloodOxygenSaturation.value: {value.mandatory}"));

        evolutionNote.setVitalSigns(newVitalSigns("Value", LocalDateTime.of(2020,9,9,1,5,6)));
        exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                createEvolutionNoteService.execute(internmentEpisodeSaved.getInstitutionId(), evolutionNote)
        );
        Assertions.assertTrue(exception.getMessage().contains("Saturación de oxigeno: La fecha de medición debe ser posterior a la fecha de internación"));
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

    private EvolutionNoteBo validEvolutionNote(){
        var result = new EvolutionNoteBo();
        result.setConfirmed(true);
        result.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        result.setDiagnosis(Lists.emptyList());
        result.setImmunizations(Lists.emptyList());
        result.setAllergies(Lists.emptyList());
        return result;
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