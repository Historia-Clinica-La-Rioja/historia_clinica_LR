package ar.lamansys.sgh.clinichistory.infrastructure.application.ips;

import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.CalculateCie10Facade;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.GetLastHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.GetLastHealthConditionRepositotyImpl;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ConditionClinicalStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.ConditionVerificationStatusRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest(showSql = false)
class HealthConditionServiceTest extends UnitRepository {

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

	@MockBean
	private SnomedRepository snomedRepository;


    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp(){

        GetLastHealthConditionRepository getLastHealthConditionRepository;

        getLastHealthConditionRepository = new GetLastHealthConditionRepositotyImpl(entityManager);
        healthConditionService = new HealthConditionService(
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
    void test_getLastHealthCondition_success(){

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
