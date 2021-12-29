package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgh.clinichistory.mocks.DiagnosticReportTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.HealthConditionTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.GetDiagnosticReportInfoRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.GetDiagnosticReportInfoRepositoryImpl;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFile;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.impl.DiagnosticReportInfoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

class DiagnosticReportInfoServiceImplTest extends UnitRepository {

    private DiagnosticReportInfoService diagnosticReportInfoService;
    private GetDiagnosticReportInfoRepository getDiagnosticReportInfoRepository;

    @Autowired
    DiagnosticReportFileRepository diagnosticReportFileRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp(){
        getDiagnosticReportInfoRepository = new GetDiagnosticReportInfoRepositoryImpl(entityManager);
        diagnosticReportInfoService = new DiagnosticReportInfoServiceImpl(getDiagnosticReportInfoRepository, diagnosticReportFileRepository);

        save(new DiagnosticReportStatus(DiagnosticReportStatus.REGISTERED, "Registrado"));
        save(new DiagnosticReportStatus(DiagnosticReportStatus.FINAL, "Final"));
    }

    @Test
    void execute_success(){

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_radiografia = save(SnomedTestMocks.createSnomed("RADIOGRAFIA")).getId();

        Integer sr1_id = save(new ServiceRequest(1, patientId, 1, 1, "category")).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED, "", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        DiagnosticReportFile drf1 = new DiagnosticReportFile("path", "text/plain", 124142L, "file1.txt");
        DiagnosticReportFile drf2 = new DiagnosticReportFile("path1", "text/pdf", 4125, "file2.pdf");
        DiagnosticReportFile drf3 = new DiagnosticReportFile("path2", "image/jpg", 999L, "file3.jpg");

        drf1.setDiagnosticReportId(diagnosticReportId);
        drf2.setDiagnosticReportId(diagnosticReportId);
        drf3.setDiagnosticReportId(diagnosticReportId);

        save(drf1);
        save(drf2);
        save(drf3);

        DiagnosticReportBo result = diagnosticReportInfoService.run(diagnosticReportId);
        Assertions.assertThat(result.getFiles()).hasSize(3);
    }

}
