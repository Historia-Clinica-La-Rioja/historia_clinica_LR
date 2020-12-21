package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.*;
import net.pladema.clinichistory.mocks.*;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepositoryImpl;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.clinichistory.requests.servicerequests.service.impl.ListDiagnosticReportInfoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ListDiagnosticReportInfoServiceImplTest extends UnitRepository {

    private ListDiagnosticReportInfoService listDiagnosticReportInfoService;
    private ListDiagnosticReportRepository listDiagnosticReportRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp() {
        listDiagnosticReportRepository = new ListDiagnosticReportRepositoryImpl(entityManager);
        listDiagnosticReportInfoService = new ListDiagnosticReportInfoServiceImpl(listDiagnosticReportRepository);

        save(new DiagnosticReportStatus(DiagnosticReportStatus.REGISTERED, "Registrado"));
        save(new DiagnosticReportStatus(DiagnosticReportStatus.FINAL, "Final"));
    }

    @Test
    public void execute_success() {

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_radiografia = save(SnomedTestMocks.createSnomed("RADIOGRAFIA")).getId();

        Integer sr1_id = save(new ServiceRequest(1, patientId, 1, 1, "category")).getId();
        Integer sr2_id = save(new ServiceRequest(1, patientId, 1, null, "category")).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();
        Long order2_doc_id = save(DocumentsTestMocks.createDocument(sr2_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));


        Integer diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, "", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, "", null, papera_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, "", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order2_doc_id, diagnosticReportId));

        DiagnosticReportFilterBo diagnosticReportFilterBo = new DiagnosticReportFilterBo(patientId,null, null, null);
        List<DiagnosticReportBo> result = listDiagnosticReportInfoService.execute(diagnosticReportFilterBo);
        Assertions.assertThat(result)
                .hasSize(2);
    }
}

