package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.*;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.clinichistory.mocks.HealthConditionTestMocks;
import net.pladema.clinichistory.mocks.MedicationTestMocks;
import net.pladema.clinichistory.mocks.SnomedTestMocks;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.clinichistory.requests.medicationrequests.repository.GetMedicationRequestInfoRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.GetMedicationRequestInfoRepositoryImpl;
import net.pladema.clinichistory.requests.medicationrequests.repository.entity.MedicationRequest;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestInfoService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;


@RunWith(SpringRunner.class)
public class GetMedicationRequestInfoServiceImplTest extends UnitRepository {

    private GetMedicationRequestInfoService getMedicationRequestInfoService;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp(){
        GetMedicationRequestInfoRepository getMedicationRequestInfoRepository = new GetMedicationRequestInfoRepositoryImpl(entityManager);
        getMedicationRequestInfoService = new GetMedicationRequestInfoServiceImpl(getMedicationRequestInfoRepository);
        save(new MedicationStatementStatus(MedicationStatementStatus.ACTIVE, "Activo"));
    }

    @Test
    public void execute_success(){

        Integer patientId = 1;
        Integer sctId_anginas = save(SnomedTestMocks.createSnomed("ANGINAS")).getId();
        Integer sctId_papera = save(SnomedTestMocks.createSnomed("PAPERA")).getId();
        Integer sctId_dolor = save(SnomedTestMocks.createSnomed("DOLOR")).getId();
        Integer sctId_ibuprofeno = save(SnomedTestMocks.createSnomed("IBUPROFENO")).getId();
        Integer sctId_paracetamol = save(SnomedTestMocks.createSnomed("PARACETAMOL")).getId();
        Integer sctId_clonacepan = save(SnomedTestMocks.createSnomed("CLONACEPAN")).getId();

        Integer mr1_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Integer mr2_id = save(new MedicationRequest(patientId, 1, "status", "intent", "category", 1, true)).getId();
        Long outpatient_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.OUTPATIENT, SourceType.OUTPATIENT, DocumentStatus.FINAL)).getId();
        Long hospitalization_doc_id = save(DocumentsTestMocks.createDocument(1, DocumentType.EPICRISIS, SourceType.HOSPITALIZATION, DocumentStatus.FINAL)).getId();
        Long recipe1_doc_id = save(DocumentsTestMocks.createDocument(mr1_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();
        Long recipe2_doc_id = save(DocumentsTestMocks.createDocument(mr2_id, DocumentType.RECIPE, SourceType.RECIPE, DocumentStatus.FINAL)).getId();

        Integer angina_id = save(HealthConditionTestMocks.createPersonalHistoryWithCie10Codes(patientId, sctId_anginas, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED, "213123,56156,6546")).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, angina_id));

        Integer papera_id = save(HealthConditionTestMocks.createPersonalHistoryWithCie10Codes(patientId, sctId_papera, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED,"211113123,56156,65as46")).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(outpatient_doc_id, papera_id));

        Integer dolor_id = save(HealthConditionTestMocks.createPersonalHistoryWithCie10Codes(patientId, sctId_dolor, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED, "213123,56145356,654qw6")).getId();
        save(HealthConditionTestMocks.createHealthConditionDocument(hospitalization_doc_id, dolor_id));


        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
                LocalDate.of(2020,12,16), LocalDate.of(2020,12,23), null, null)).getId();

        Integer mIbuprofenoId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_ibuprofeno, "", MedicationStatementStatus.ACTIVE, null, dolor_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, mIbuprofenoId));

        Integer mParacetamolId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_paracetamol, "", MedicationStatementStatus.ACTIVE, null, angina_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe1_doc_id, mParacetamolId));

        Integer mClonacepanId = save(MedicationTestMocks.createMedicationStatement(patientId, sctId_clonacepan, "", MedicationStatementStatus.ACTIVE, null, papera_id, dosageId)).getId();
        save(MedicationTestMocks.createDocumentMedicationStatement(recipe2_doc_id, mClonacepanId));

        var result = getMedicationRequestInfoService.execute(mr1_id);

        MedicationBo ibuprofeno = new MedicationBo();
        ibuprofeno.setSnomed(new SnomedBo(sctId_ibuprofeno, "IBUPROFENO", "IBUPROFENO"));
        HealthConditionBo dolor = new HealthConditionBo();
        dolor.setId(dolor_id);
        dolor.setSnomed(new SnomedBo(sctId_dolor, "DOLOR", "DOLOR"));
        dolor.setCie10codes("213123,56145356,654qw6");
        ibuprofeno.setHealthCondition(dolor);

        MedicationBo paracetamol = new MedicationBo();
        paracetamol.setSnomed(new SnomedBo(sctId_paracetamol, "PARACETAMOL", "PARACETAMOL"));
        HealthConditionBo angina = new HealthConditionBo();
        angina.setId(angina_id);
        angina.setSnomed(new SnomedBo(sctId_anginas, "ANGINAS", "ANGINAS"));
        angina.setCie10codes("213123,56156,6546");
        paracetamol.setHealthCondition(angina);

        Assertions.assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("medicalCoverageId")
                .hasFieldOrProperty("medications");

        Assertions.assertThat(result.getMedications())
                .isNotEmpty()
                .hasSize(2);

        Assertions.assertThat(contains(result.getMedications(), ibuprofeno))
                    .isTrue();

        Assertions.assertThat(contains(result.getMedications(), paracetamol))
                .isTrue();
    }

    public boolean contains(List<MedicationBo> sources, MedicationBo target) {
        for (var source: sources) {
            if (isEqualTo(source, target))
                return true;
        }
        return false;
    }

    public boolean isEqualTo(MedicationBo source, MedicationBo target){
        if (source == null && target == null)
            return true;
        return source.getSnomed().equals(target.getSnomed()) &&
                isEqualTo(source.getHealthCondition(), target.getHealthCondition());
    }

    public boolean isEqualTo(HealthConditionBo source, HealthConditionBo target){
        if (source == null && target == null)
            return true;
        return source.getId().equals(target.getId()) &&
                source.getSnomed().equals(target.getSnomed()) &&
                source.getCie10codes().equals(target.getCie10codes());
    }
}