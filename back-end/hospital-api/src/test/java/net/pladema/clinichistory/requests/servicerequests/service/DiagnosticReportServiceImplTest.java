package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.core.cie10.CalculateCie10Facade;
import net.pladema.clinichistory.documents.core.ips.DiagnosticReportServiceImpl;
import net.pladema.clinichistory.documents.repository.ips.DiagnosticReportRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.*;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.DiagnosticReportService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.clinichistory.mocks.HealthConditionTestMocks;
import net.pladema.clinichistory.mocks.SnomedTestMocks;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class DiagnosticReportServiceImplTest extends UnitRepository {

    private DiagnosticReportService diagnosticReportService;


    @MockBean
    private NoteService noteService;

    @Autowired
    private DiagnosticReportRepository diagnosticReportRepository;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private SnomedService snomedService;

    @MockBean
    private CalculateCie10Facade calculateCie10Facade;

    @Before
    public void setUp() {
        diagnosticReportService = new DiagnosticReportServiceImpl(
                diagnosticReportRepository,
                documentService,
                noteService,
                snomedService,
                calculateCie10Facade
        );
    }

    @Test
    public void test_note_flow_delete_dr_use_case(){

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Snomed snomed_radiografia = save(SnomedTestMocks.createSnomed("RADIOGRAFIA"));

        Integer sr1_id = save(new ServiceRequest(1, patientId, 1, 1, "category")).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Long noteId = 1L;

        DiagnosticReportBo drDeleteUseCase = new DiagnosticReportBo();
        drDeleteUseCase.setHealthConditionId(angina_id);
        drDeleteUseCase.setStatusId(DiagnosticReportStatus.CANCELLED);
        drDeleteUseCase.setNoteId(noteId);
        drDeleteUseCase.setSnomed(new SnomedBo(snomed_radiografia));

        Integer drId = diagnosticReportService.loadDiagnosticReport(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drDeleteUseCase)
                ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isEqualTo(noteId);


        DiagnosticReportBo drDeleteUseCaseWithoutNote = new DiagnosticReportBo();
        drDeleteUseCaseWithoutNote.setHealthConditionId(angina_id);
        drDeleteUseCaseWithoutNote.setStatusId(DiagnosticReportStatus.CANCELLED);
        drDeleteUseCaseWithoutNote.setSnomed(new SnomedBo(snomed_radiografia));

        drId = diagnosticReportService.loadDiagnosticReport(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drDeleteUseCaseWithoutNote)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isNull();
    }


    @Test
    public void test_note_flow_complete_or_create_dr_use_case(){

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Snomed snomed_radiografia = save(SnomedTestMocks.createSnomed("RADIOGRAFIA"));

        Integer sr1_id = save(new ServiceRequest(1, patientId, 1, 1, "category")).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        String observation = "Observation example Observation example Observation example Observation example Observation example.";
        DiagnosticReportBo drCompleteOrCreateUseCase = new DiagnosticReportBo();
        drCompleteOrCreateUseCase.setHealthConditionId(angina_id);
        drCompleteOrCreateUseCase.setStatusId(DiagnosticReportStatus.REGISTERED);
        drCompleteOrCreateUseCase.setObservations(observation);
        drCompleteOrCreateUseCase.setSnomed(new SnomedBo(snomed_radiografia));

        Integer drId = diagnosticReportService.loadDiagnosticReport(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drCompleteOrCreateUseCase)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isNotNull();

        DiagnosticReportBo drCompleteOrCreateUseCaseWithoutObservations = new DiagnosticReportBo();
        drCompleteOrCreateUseCaseWithoutObservations.setHealthConditionId(angina_id);
        drCompleteOrCreateUseCaseWithoutObservations.setStatusId(DiagnosticReportStatus.FINAL);
        drCompleteOrCreateUseCaseWithoutObservations.setSnomed(new SnomedBo(snomed_radiografia));

        drId = diagnosticReportService.loadDiagnosticReport(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drCompleteOrCreateUseCaseWithoutObservations)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isNull();
    }

    @Test
    public void test_default_status_id(){
        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Snomed snomed_radiografia = save(SnomedTestMocks.createSnomed("RADIOGRAFIA"));

        Integer sr1_id = save(new ServiceRequest(1, patientId, 1, 1, "category")).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        String observation = "Observation example Observation example Observation example Observation example Observation example.";
        DiagnosticReportBo drBo = new DiagnosticReportBo();
        drBo.setHealthConditionId(angina_id);
        drBo.setObservations(observation);
        drBo.setSnomed(new SnomedBo(snomed_radiografia));

        Integer drId = diagnosticReportService.loadDiagnosticReport(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drBo)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getStatusId()).isEqualTo(DiagnosticReportStatus.REGISTERED);

    }
}
