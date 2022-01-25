package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.HealthConditionTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class LoadDiagnosticReportsTest extends UnitRepository {

    private LoadDiagnosticReports loadDiagnosticReports;

    @Mock
    private NoteService noteService;

    @Autowired
    private DiagnosticReportRepository diagnosticReportRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private SnomedService snomedService;

    @Mock
    private CalculateCie10Facade calculateCie10Facade;

    @BeforeEach
    void setUp() {
        loadDiagnosticReports = new LoadDiagnosticReports(
                diagnosticReportRepository,
                documentService,
                noteService,
                snomedService,
                calculateCie10Facade
        );
    }

    @Test
    void test_note_flow_delete_dr_use_case(){

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

        Integer drId = loadDiagnosticReports.run(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drDeleteUseCase)
                ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isEqualTo(noteId);


        DiagnosticReportBo drDeleteUseCaseWithoutNote = new DiagnosticReportBo();
        drDeleteUseCaseWithoutNote.setHealthConditionId(angina_id);
        drDeleteUseCaseWithoutNote.setStatusId(DiagnosticReportStatus.CANCELLED);
        drDeleteUseCaseWithoutNote.setSnomed(new SnomedBo(snomed_radiografia));

        drId = loadDiagnosticReports.run(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drDeleteUseCaseWithoutNote)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isNull();
    }


    @Test
    void test_note_flow_complete_or_create_dr_use_case(){

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

        Integer drId = loadDiagnosticReports.run(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drCompleteOrCreateUseCase)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isNotNull();

        DiagnosticReportBo drCompleteOrCreateUseCaseWithoutObservations = new DiagnosticReportBo();
        drCompleteOrCreateUseCaseWithoutObservations.setHealthConditionId(angina_id);
        drCompleteOrCreateUseCaseWithoutObservations.setStatusId(DiagnosticReportStatus.FINAL);
        drCompleteOrCreateUseCaseWithoutObservations.setSnomed(new SnomedBo(snomed_radiografia));

        drId = loadDiagnosticReports.run(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drCompleteOrCreateUseCaseWithoutObservations)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getNoteId()).isNull();
    }

    @Test
    void test_default_status_id(){
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

        Integer drId = loadDiagnosticReports.run(
                order1_doc_id,
                new PatientInfoBo(1, (short) 1, (short) 25),
                List.of(drBo)
        ).get(0);

        Assertions.assertThat(diagnosticReportRepository.findById(drId).get().getStatusId()).isEqualTo(DiagnosticReportStatus.REGISTERED);

    }
}
