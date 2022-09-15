package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.UnitRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.SnomedRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.mocks.DocumentsTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.HealthConditionTestMocks;
import ar.lamansys.sgh.clinichistory.mocks.SnomedTestMocks;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HCEHealthConditionRepositoryTest extends UnitRepository {

	private final Short AMBULATORIA = 4;

	@MockBean
	private SnomedRepository snomedRepository;

	private HCEHealthConditionRepository hCEHealthConditionRepository;

	@Autowired
	private EntityManager entityManager;

	@BeforeEach
	void setUp() {
		hCEHealthConditionRepository = new HCEHealthConditionRepositoryImpl(entityManager);
	}

	@Test
	void test_hce_health_condition_success() {
		Integer patientId = 1;

		Snomed personalSnomed1 = mockSnomed("personal1");
		Snomed personalSnomed2 = mockSnomed("personal2");
		Snomed mainDiagnose1 = mockSnomed("mainDiagnose1");

		createFirstDocument(patientId, personalSnomed1, personalSnomed2, mainDiagnose1);
		createSecondDocument(patientId);
		createThirdDocument(patientId, personalSnomed1, personalSnomed2, mainDiagnose1);

		List<HCEHealthConditionVo> resultQuery = hCEHealthConditionRepository.getPersonalHistories(patientId);

		Assertions.assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(3);

		// El diagnostico principal queda inactivo por lo que se descarta
		assertThat(resultQuery.stream().anyMatch(HCEHealthConditionVo::isMain))
				.isFalse();

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isFamilyHistory))
				.hasSize(0);

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isPersonalHistory))
				.hasSize(1);

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isChronic))
				.hasSize(2);

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isSecondaryDiagnosis))
				.hasSize(0);

	}

	@Test
	void test_hce_family_history_success() {
		Integer patientId = 1;

		Snomed personalSnomed1 = mockSnomed("personal1");
		Snomed personalSnomed2 = mockSnomed("personal2");
		Snomed mainDiagnose1 = mockSnomed("mainDiagnose1");

		createFirstDocument(patientId, personalSnomed1, personalSnomed2, mainDiagnose1);
		createSecondDocument(patientId);
		createThirdDocument(patientId, personalSnomed1, personalSnomed2, mainDiagnose1);

		List<HCEHealthConditionVo> resultQuery = hCEHealthConditionRepository.getFamilyHistories(patientId);

		Assertions.assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2);

		// El diagnostico principal queda inactivo por lo que se descarta
		assertThat(resultQuery.stream().anyMatch(HCEHealthConditionVo::isMain))
				.isFalse();

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isFamilyHistory))
				.hasSize(2);

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isPersonalHistory))
				.hasSize(0);

		Assertions.assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isSecondaryDiagnosis))
				.hasSize(0);

	}

	private void createFirstDocument(Integer patientId, Snomed personalSnomed1, Snomed personalSnomed2, Snomed mainDiagnose){
		Document document = DocumentsTestMocks.createDocument(1, AMBULATORIA, SourceType.OUTPATIENT, DocumentStatus.FINAL);
		document = save(document);

		Snomed familySnomed = mockSnomed("familyCode");
		HealthCondition familyhistory1 = HealthConditionTestMocks.createFamilyHistory(patientId,familySnomed.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(familyhistory1).getId()));

		HealthCondition personalHistory1 = HealthConditionTestMocks.createPersonalHistory(patientId, personalSnomed1.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory1).getId()));

		HealthCondition personalHistory2 = HealthConditionTestMocks.createPersonalHistory(patientId,personalSnomed2.getId(), ConditionClinicalStatus.INACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory2).getId()));

		Snomed diagnose1Snomed = mockSnomed("diagnose1");
		HealthCondition diagnose1 = HealthConditionTestMocks.createDiagnose(patientId,diagnose1Snomed.getId(), ConditionClinicalStatus.INACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose1).getId()));

		HealthCondition mainDiagnoses = HealthConditionTestMocks.createMainDiagnose(patientId,mainDiagnose.getId(), ConditionClinicalStatus.ACTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(mainDiagnoses).getId()));
	}

	private Snomed mockSnomed(String personal1) {
		String personalCode = personal1;
		return save(SnomedTestMocks.createSnomed(personalCode));
	}

	private void createSecondDocument(Integer patientId){
		Document document = DocumentsTestMocks.createDocument(1, AMBULATORIA, SourceType.OUTPATIENT, DocumentStatus.FINAL);
		document = save(document);

		Snomed diagnose2Snomed = mockSnomed("diagnose2");
		HealthCondition diagnose2 = HealthConditionTestMocks.createDiagnose(patientId,diagnose2Snomed.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose2).getId()));

		Snomed diagnose3Snomed = mockSnomed("diagnose3");
		HealthCondition diagnose3 = HealthConditionTestMocks.createDiagnose(patientId,diagnose3Snomed.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose3).getId()));

		HealthCondition diagnose4 = HealthConditionTestMocks.createDiagnose(patientId,diagnose3Snomed.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose4).getId()));

	}


	private void createThirdDocument(Integer patientId, Snomed personalSnomed1, Snomed personalSnomed2, Snomed mainDiagnose){
		Document document = DocumentsTestMocks.createDocument(1, AMBULATORIA, SourceType.OUTPATIENT, DocumentStatus.FINAL);
		document = save(document);

		Snomed familySnomed2 = mockSnomed("familyCode2");
		HealthCondition familyhistory2 = HealthConditionTestMocks.createFamilyHistory(patientId,familySnomed2.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(familyhistory2).getId()));

		Snomed personal3Snomed = mockSnomed("personal3");
		HealthCondition personalHistory3 = HealthConditionTestMocks.createPersonalHistory(patientId,personal3Snomed.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory3).getId()));

		Snomed diagnose4Snomed = mockSnomed("diagnose4");
		HealthCondition diagnose5 = HealthConditionTestMocks.createDiagnose(patientId,diagnose4Snomed.getId(), ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.ERROR);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose5).getId()));

		HealthCondition mainDiagnoses1 = HealthConditionTestMocks.createMainDiagnose(patientId,mainDiagnose.getId(), ConditionClinicalStatus.INACTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(mainDiagnoses1).getId()));

		HealthCondition personalHistory1 = HealthConditionTestMocks.createChronicPersonalHistory(patientId, personalSnomed1.getId(), ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory1).getId()));

		HealthCondition personalHistory2 = HealthConditionTestMocks.createChronicPersonalHistory(patientId,personalSnomed2.getId(), ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory2).getId()));
	}

}
