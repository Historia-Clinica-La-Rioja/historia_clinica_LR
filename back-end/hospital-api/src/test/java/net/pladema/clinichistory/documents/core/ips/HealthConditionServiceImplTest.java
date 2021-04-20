package net.pladema.clinichistory.documents.core.ips;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.core.cie10.CalculateCie10Facade;
import net.pladema.clinichistory.documents.repository.ips.GetLastHealthConditionRepository;
import net.pladema.clinichistory.documents.repository.ips.GetLastHealthConditionRepositotyImpl;
import net.pladema.clinichistory.documents.repository.ips.HealthConditionRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.HealthCondition;
import net.pladema.clinichistory.documents.repository.ips.masterdata.ConditionClinicalStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.ConditionVerificationStatusRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.ips.HealthConditionService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class HealthConditionServiceImplTest extends UnitRepository {

    HealthConditionService healthConditionService;

    @Autowired
    private HealthConditionRepository healthConditionRepository;

    @MockBean
    private ConditionVerificationStatusRepository conditionVerificationStatusRepository;

    @MockBean
    private ConditionClinicalStatusRepository conditionClinicalStatusRepository;

    @MockBean
    private SnomedService snomedService;

    @MockBean
    private CalculateCie10Facade calculateCie10Facade;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private NoteService noteService;

    @MockBean
    private DateTimeProvider dateTimeProvider;


    @Autowired
    private EntityManager entityManager;

    @Before
    public void setUp(){

        GetLastHealthConditionRepository getLastHealthConditionRepository;

        getLastHealthConditionRepository = new GetLastHealthConditionRepositotyImpl(entityManager);
        healthConditionService = new HealthConditionServiceImpl(
                healthConditionRepository,
                conditionVerificationStatusRepository,
                conditionClinicalStatusRepository,
                snomedService,
                calculateCie10Facade,
                documentService,
                noteService,
                dateTimeProvider,
                getLastHealthConditionRepository
        );
    }

    @Test
    public void test_getLastHealthCondition_success(){

        HealthCondition hc1 = new HealthCondition();
        hc1.setPatientId(1);
        hc1.setSnomedId(1);
        hc1.setStatusId(ConditionClinicalStatus.ACTIVE);
        hc1.setVerificationStatusId(ConditionVerificationStatus.CONFIRMED);
        hc1.setProblemId("pro123");

        HealthCondition hc2 = new HealthCondition();
        hc2.setPatientId(1);
        hc2.setSnomedId(1);
        hc2.setStatusId(ConditionClinicalStatus.SOLVED);
        hc2.setVerificationStatusId(ConditionVerificationStatus.CONFIRMED);
        hc2.setProblemId("pro123");

        Integer hc1Id = save(hc1).getId();
        Integer hc2Id = save(hc2).getId();

        var resultMap = healthConditionService.getLastHealthCondition(1, List.of(hc1Id));

        Assertions.assertThat(resultMap.get(hc1Id).getId()).isEqualTo(hc2Id);
        Assertions.assertThat(resultMap.get(hc1Id).getStatusId()).isEqualTo(ConditionClinicalStatus.SOLVED);

        HealthCondition hc3 = new HealthCondition();
        hc3.setPatientId(1);
        hc3.setSnomedId(1);
        hc3.setStatusId(ConditionClinicalStatus.ACTIVE);
        hc3.setVerificationStatusId(ConditionVerificationStatus.CONFIRMED);
        hc3.setProblemId("pro123");

        Integer hc3Id = save(hc3).getId();

        resultMap = healthConditionService.getLastHealthCondition(1, List.of(hc2Id, hc2Id));

        Assertions.assertThat(resultMap.get(hc2Id).getId()).isEqualTo(hc3Id);
        Assertions.assertThat(resultMap.get(hc2Id).getStatusId()).isEqualTo(ConditionClinicalStatus.ACTIVE);
        Assertions.assertThat(resultMap).hasSize(1);
    }
}
