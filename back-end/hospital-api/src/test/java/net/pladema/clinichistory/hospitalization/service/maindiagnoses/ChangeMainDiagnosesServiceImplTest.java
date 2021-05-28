package net.pladema.clinichistory.hospitalization.service.maindiagnoses;

import net.pladema.clinichistory.documents.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.DocumentFactory;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.generalstate.HealthConditionGeneralStateService;
import net.pladema.clinichistory.documents.service.ips.domain.DocumentObservationsBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.impl.InternmentEpisodeServiceImpl;
import net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain.MainDiagnosisBo;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest(showSql = false)
class ChangeMainDiagnosesServiceImplTest {

    private ChangeMainDiagnosesService changeMainDiagnosesService;

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
        changeMainDiagnosesService = new ChangeMainDiagnosesServiceImpl(
                documentFactory,
                internmentEpisodeService,
                createDocumentFile,
                healthConditionGeneralStateService);
    }

    @Test
    void createDocumentWithEpisodeThatNotExists() {
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(9, 10))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidInstitutionId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(null, 8))
        );
        String expectedMessage = "El id de la institución es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInvalidEpisodeId() {
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(8, null))
        );
        String expectedMessage = "El id del encuentro asociado es obligatorio";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithInternmentInOtherInstitution() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(null));
        Exception exception = Assertions.assertThrows(NotFoundException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(internmentEpisode.getInstitutionId()+1, internmentEpisode.getId()))
        );
        String expectedMessage = "internmentepisode.not.found";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocumentWithEpicrisis() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(1l));
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(validMainDiagnosisBo(8, internmentEpisode.getId()))
        );
        String expectedMessage = "Esta internación ya posee una epicrisis";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);
    }

    @Test
    void createDocument_withoutMainDiagnosis() {
        var internmentEpisode = internmentEpisodeRepository.saveAndFlush(newInternmentEpisodeWithEpicrisis(null));
        var mainDiagnosisBo = validMainDiagnosisBo(internmentEpisode.getInstitutionId(), internmentEpisode.getId());
        mainDiagnosisBo.setMainDiagnosis(null);
        Exception exception = Assertions.assertThrows(ConstraintViolationException.class, () ->
                changeMainDiagnosesService.execute(mainDiagnosisBo)
        );
        String expectedMessage = "mainDiagnosis: {diagnosis.mandatory}";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage,expectedMessage);
    }


    private MainDiagnosisBo validMainDiagnosisBo(Integer institutionId, Integer encounterId){
        var result = new MainDiagnosisBo();
        result.setConfirmed(true);
        result.setInstitutionId(institutionId);
        result.setEncounterId(encounterId);
        result.setNotes(new DocumentObservationsBo());
        result.setMainDiagnosis(new HealthConditionBo(new SnomedBo("MAIN", "MAIN")));
        result.setDiagnosis(Lists.emptyList());
        return result;
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
}