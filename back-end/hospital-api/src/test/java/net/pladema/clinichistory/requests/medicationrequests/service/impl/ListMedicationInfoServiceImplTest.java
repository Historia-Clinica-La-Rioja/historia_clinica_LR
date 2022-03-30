package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.MedicationCalculateStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.HealthConditionTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.MedicationTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.ListMedicationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.ListMedicationRepositoryImpl;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

class ListMedicationInfoServiceImplTest extends UnitRepository {

    private ListMedicationInfoService listMedicationInfoService;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    @Autowired
    private EntityManager entityManager;


    @BeforeEach
    public void setUp(){
        MedicationCalculateStatus medicationCalculateStatus = new MedicationCalculateStatus(dateTimeProvider);
        ListMedicationRepository listMedicationRepository = new ListMedicationRepositoryImpl(entityManager);
        listMedicationInfoService = new ListMedicationInfoServiceImpl(listMedicationRepository, medicationCalculateStatus);

        save(new MedicationStatementStatus(MedicationStatementStatus.ACTIVE, "Activo"));
        save(new MedicationStatementStatus(MedicationStatementStatus.SUSPENDED, "Suspendido"));
    }

    @Test
    @Disabled
    public void executeFilterActiveSuccess(){

        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2021,01,9));

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_dolor = save(SnomedTestMocks.createSnomed("DOLOR")).getId();
        Integer sctId_ibuprofeno = save(SnomedTestMocks.createSnomed("IBUPROFENO")).getId();

        Integer mr1_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr2_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr3_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();
        Integer mr4_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long outpatient2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long hospitalization_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long hospitalization2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long recipe1_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe2_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe3_doc_id = save(DocumentsTestMocks.createDocument(mr2_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe4_doc_id = save(DocumentsTestMocks.createDocument(mr3_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe5_doc_id = save(DocumentsTestMocks.createDocument(mr4_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer dolor_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_dolor, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(hospitalization_doc_id, dolor_id));


        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
                LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, medicationId));


        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(outpatient2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), LocalDate.of(2020,12,18), LocalDate.of(2020,12,20),null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, medicationId));

        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.SUSPENDED, null, angina_id, null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(5d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,20,0,0,0), LocalDateTime.of(2020,12,25,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe3_doc_id, medicationId));

        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(hospitalization2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(9d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,21,0,0,0), LocalDateTime.of(2020,12,30,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, dolor_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe4_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));

        MedicationFilterBo medicationFilterBo1 = new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, null, null);
        List<MedicationBo> result = listMedicationInfoService.execute(medicationFilterBo1);
        Assertions.assertThat(result)
                .hasSize(4);

        result.forEach(r -> {
            Assertions.assertThat(r.getStatusId())
                    .isEqualTo(medicationFilterBo1.getStatusId());

            if (mr1_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr2_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr3_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (mr4_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (r.getEncounterId() == null)
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();
        });
    }

    @Test
    @Disabled
    public void executeFilterSuspendedSuccess(){

        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020,12,19));
        Integer patientId = 1;

        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_dolor = save(SnomedTestMocks.createSnomed("DOLOR")).getId();
        Integer sctId_ibuprofeno = save(SnomedTestMocks.createSnomed("IBUPROFENO")).getId();

        Integer mr1_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr2_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr3_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long outpatient2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long hospitalization_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long hospitalization2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long recipe1_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe2_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe3_doc_id = save(DocumentsTestMocks.createDocument(mr2_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe4_doc_id = save(DocumentsTestMocks.createDocument(mr3_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer dolor_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_dolor, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(hospitalization_doc_id, dolor_id));


        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, medicationId));


        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(outpatient2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), LocalDate.of(2020,12,18), LocalDate.of(2020,12,20), null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.SUSPENDED, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(5d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,20,0,0,0), LocalDateTime.of(2020,12,25,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe3_doc_id, medicationId));

        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.SUSPENDED, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(hospitalization2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(9d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,21,0,0,0), LocalDateTime.of(2020,12,30,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, dolor_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe4_doc_id, medicationId));

        MedicationFilterBo medicationFilterBo = new MedicationFilterBo(patientId, MedicationStatementStatus.SUSPENDED, null, null);
        List<MedicationBo> result = listMedicationInfoService.execute(medicationFilterBo);
        Assertions.assertThat(result)
                .hasSize(2);

        result.forEach(r -> {
            Assertions.assertThat(r.getStatusId())
                    .isEqualTo(medicationFilterBo.getStatusId());

            if (mr1_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr2_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr3_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (r.getEncounterId() == null)
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();
        });
    }

    @Test
    public void executeFilterActiveAndMedicationSuccess(){

        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2021,01,9));

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_dolor = save(SnomedTestMocks.createSnomed("DOLOR")).getId();
        Integer sctId_ibuprofeno = save(SnomedTestMocks.createSnomed("IBUPROFENO 20 mg")).getId();
        Integer sctId_paracetamol = save(SnomedTestMocks.createSnomed("PARAcEtAMOL 100 mg")).getId();
        Integer sctId_clonacepan = save(SnomedTestMocks.createSnomed("CLONaCEPAN 80mg")).getId();

        Integer mr1_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr2_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr3_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();
        Integer mr4_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long outpatient2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long hospitalization_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long hospitalization2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long recipe1_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe2_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe3_doc_id = save(DocumentsTestMocks.createDocument(mr2_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe4_doc_id = save(DocumentsTestMocks.createDocument(mr3_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe5_doc_id = save(DocumentsTestMocks.createDocument(mr4_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer dolor_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_dolor, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(hospitalization_doc_id, dolor_id));


        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, medicationId));


        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(outpatient2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), LocalDate.of(2020,12,18), LocalDate.of(2020,12,20), null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(5d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,20,0,0,0), LocalDateTime.of(2020,12,25,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe3_doc_id, medicationId));

        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(hospitalization2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(9d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,21,0,0,0), LocalDateTime.of(2020,12,30,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, dolor_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe4_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_paracetamol, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_clonacepan, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));

        List<MedicationBo> result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, "IBuPR", null));
        Assertions.assertThat(result)
                .hasSize(4);

        result.forEach(r -> {
            Assertions.assertThat(r.getStatusId())
                    .isEqualTo(MedicationStatementStatus.ACTIVE);

            if (mr1_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr2_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr3_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (mr4_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (r.getEncounterId() == null)
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();
        });

        result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, "PAR", null));
        Assertions.assertThat(result)
                .hasSize(1);

        result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, "CEPAN", null));
        Assertions.assertThat(result)
                .hasSize(1);
    }

    @Test
    public void executeFilterActiveAndHealthconditionSuccess(){

        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2021,01,9));

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_dolor = save(SnomedTestMocks.createSnomed("DOLOR")).getId();
        Integer sctId_ibuprofeno = save(SnomedTestMocks.createSnomed("IBUPROFENO 20 mg")).getId();
        Integer sctId_paracetamol = save(SnomedTestMocks.createSnomed("PARAcEtAMOL 100 mg")).getId();
        Integer sctId_clonacepan = save(SnomedTestMocks.createSnomed("CLONaCEPAN 80mg")).getId();

        Integer mr1_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr2_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr3_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();
        Integer mr4_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long outpatient2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long hospitalization_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long hospitalization2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long recipe1_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe2_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe3_doc_id = save(DocumentsTestMocks.createDocument(mr2_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe4_doc_id = save(DocumentsTestMocks.createDocument(mr3_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe5_doc_id = save(DocumentsTestMocks.createDocument(mr4_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer dolor_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_dolor, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(hospitalization_doc_id, dolor_id));


        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, medicationId));


        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(outpatient2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), LocalDate.of(2020,12,18), LocalDate.of(2020,12,20), null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(5d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,20,0,0,0), LocalDateTime.of(2020,12,25,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe3_doc_id, medicationId));

        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(hospitalization2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(9d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,21,0,0,0), LocalDateTime.of(2020,12,30,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, dolor_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe4_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_paracetamol, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_clonacepan, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));

        List<MedicationBo> result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, null, "Angi"));
        Assertions.assertThat(result)
                .hasSize(2  );

        result.forEach(r -> {
            Assertions.assertThat(r.getStatusId())
                    .isEqualTo(MedicationStatementStatus.ACTIVE);

            if (mr1_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr2_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isTrue();

            if (mr3_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (mr4_id.equals(r.getEncounterId()))
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();

            if (r.getEncounterId() == null)
                Assertions.assertThat(r.isHasRecipe())
                        .isFalse();
        });

        result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, null, "PAPE"));
        Assertions.assertThat(result)
                .hasSize(2);

        result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, null, "DOL"));
        Assertions.assertThat(result)
                .hasSize(1);
    }

    @Test
    public void executeFilterMixerSuccess(){

        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2021,01,9));

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_dolor = save(SnomedTestMocks.createSnomed("DOLOR")).getId();
        Integer sctId_ibuprofeno = save(SnomedTestMocks.createSnomed("IBUPROFENO 20 mg")).getId();
        Integer sctId_paracetamol = save(SnomedTestMocks.createSnomed("PARAcEtAMOL 100 mg")).getId();
        Integer sctId_clonacepan = save(SnomedTestMocks.createSnomed("CLONaCEPAN 80mg")).getId();

        Integer mr1_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr2_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr3_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();
        Integer mr4_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, false)).getId();

        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long outpatient2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long hospitalization_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long hospitalization2_doc_id = save(DocumentsTestMocks.createDocument(2, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long recipe1_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe2_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe3_doc_id = save(DocumentsTestMocks.createDocument(mr2_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe4_doc_id = save(DocumentsTestMocks.createDocument(mr3_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe5_doc_id = save(DocumentsTestMocks.createDocument(mr4_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer dolor_id = save(HealthConditionTestMocks.createPersonalHistory(patientId, sctId_dolor, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED)).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(hospitalization_doc_id, dolor_id));


        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
                LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, medicationId));


        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(outpatient2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), LocalDate.of(2020,12,18), LocalDate.of(2020,12,20), null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(5d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,20,0,0,0), LocalDateTime.of(2020,12,25,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe3_doc_id, medicationId));

        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, null,null)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(hospitalization2_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(9d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,21,0,0,0), LocalDateTime.of(2020,12,30,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, dolor_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe4_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));

        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_paracetamol, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));


        dosageId = save(MedicationTestMocks.createDosage(11d,"d", 8, "h", false,
				LocalDateTime.of(2021,01,01,0,0,0), LocalDateTime.of(2021,01,11,0,0,0), null, null, null)).getId();
        medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_clonacepan, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe5_doc_id, medicationId));
        List<MedicationBo> result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, "PARA", "Angi"));
        Assertions.assertThat(result)
                .hasSize(1);

        result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, "IBU", "Angi"));
        Assertions.assertThat(result)
                .hasSize(1);

        result = listMedicationInfoService.execute(new MedicationFilterBo(patientId, MedicationStatementStatus.ACTIVE, "PARA", "DOL"));
        Assertions.assertThat(result)
                .hasSize(0);
    }
}