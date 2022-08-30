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
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepositoryImpl;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.clinichistory.requests.servicerequests.service.impl.ListDiagnosticReportInfoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

class ListDiagnosticReportInfoServiceImplTest extends UnitRepository {

    private ListDiagnosticReportInfoService listDiagnosticReportInfoService;
    private ListDiagnosticReportRepository listDiagnosticReportRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        listDiagnosticReportRepository = new ListDiagnosticReportRepositoryImpl(entityManager);
        listDiagnosticReportInfoService = new ListDiagnosticReportInfoServiceImpl(listDiagnosticReportRepository);

        save(new DiagnosticReportStatus(DiagnosticReportStatus.REGISTERED, "Registrado"));
        save(new DiagnosticReportStatus(DiagnosticReportStatus.FINAL,   "Final"));
    }

    @Test
    void execute_success() {

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_radiografia = save(SnomedTestMocks.createSnomed("RADIOGRAFIA")).getId();

		ServiceRequest sr1 = new ServiceRequest(1, patientId, 1, 1, ServiceRequestCategory.LABORATORY_PROCEDURE);
		ServiceRequest sr2 = new ServiceRequest(1, patientId, 1, null, ServiceRequestCategory.LABORATORY_PROCEDURE);

		sr1.setSourceTypeId((short) 0);
		sr2.setSourceTypeId((short) 0);

        Integer sr1_id = save(sr1).getId();
        Integer sr2_id = save(sr2).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();
        Long order2_doc_id = save(DocumentsTestMocks.createDocument(sr2_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

		ServiceRequestCategory serviceRequestCategory = new ServiceRequestCategory();
		serviceRequestCategory.setId(ServiceRequestCategory.LABORATORY_PROCEDURE);
		serviceRequestCategory.setDescription("Description");
		save(serviceRequestCategory);

		SourceType sourceType = new SourceType();
		sourceType.setId((short) 0);
		sourceType.setDescription("Description");
		save(sourceType);

        Integer diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED, "", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED,"", null, papera_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED,"", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order2_doc_id, diagnosticReportId));

        DiagnosticReportFilterBo diagnosticReportFilterBo = new DiagnosticReportFilterBo(patientId,null, null, null, null);
        List<DiagnosticReportBo> result = listDiagnosticReportInfoService.execute(diagnosticReportFilterBo);
        Assertions.assertThat(result)
                .hasSize(3);
    }

    @Test
    @Disabled
    void execute_filters_success() {

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_radiografia = save(SnomedTestMocks.createSnomed("RadioGrafia")).getId();
        Integer sctId_resonancia = save(SnomedTestMocks.createSnomed("ResONancia")).getId();
        Integer sctId_endoscopia = save(SnomedTestMocks.createSnomed("Endoscopia")).getId();

        Integer sr1_id = save(new ServiceRequest(1, patientId, 1, 1, ServiceRequestCategory.LABORATORY_PROCEDURE)).getId();
        Integer sr2_id = save(new ServiceRequest(1, patientId, 1, null, ServiceRequestCategory.COUNSELLING)).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long order1_doc_id = save(DocumentsTestMocks.createDocument(sr1_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();
        Long order2_doc_id = save(DocumentsTestMocks.createDocument(sr2_id, DocumentType.ORDER, SourceType.ORDER, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED, "", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED, "", null, papera_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order1_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_radiografia, DiagnosticReportStatus.REGISTERED,"", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order2_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_resonancia, DiagnosticReportStatus.REGISTERED,"", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order2_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_resonancia, DiagnosticReportStatus.FINAL,"", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order2_doc_id, diagnosticReportId));

        diagnosticReportId = save(DiagnosticReportTestMocks.createDiagnosticReport(patientId, sctId_endoscopia, DiagnosticReportStatus.ERROR,"", null, angina_id)).getId();
        save(DiagnosticReportTestMocks.createDocumentDiagnosticReport(order2_doc_id, diagnosticReportId));

        List<DiagnosticReportBo> result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,null, null, null, null));
        Assertions.assertThat(result)
                .hasSize(4);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,null, null, "Angi", null));
        Assertions.assertThat(result)
                .hasSize(3);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,null, null, "Sarampión", null));
        Assertions.assertThat(result).isEmpty();

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,null, "Radio", "Angi", null));
        Assertions.assertThat(result)
                .hasSize(2);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,DiagnosticReportStatus.FINAL, null, null, null));
        Assertions.assertThat(result)
                .hasSize(1);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,DiagnosticReportStatus.REGISTERED, null, null, null));
        Assertions.assertThat(result)
                .hasSize(3);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,DiagnosticReportStatus.REGISTERED, null, "Pape", null));
        Assertions.assertThat(result)
                .hasSize(1);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId, null, null, null, ServiceRequestCategory.LABORATORY_PROCEDURE));
        Assertions.assertThat(result)
                .hasSize(2);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId, null, null, null, ServiceRequestCategory.COUNSELLING));
        Assertions.assertThat(result)
                .hasSize(2);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId, DiagnosticReportStatus.FINAL, null, null, ServiceRequestCategory.COUNSELLING));
        Assertions.assertThat(result)
                .hasSize(1);

        result = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(patientId,DiagnosticReportStatus.ERROR, null, null, null));
        Assertions.assertThat(result).isEmpty();
    }
}

