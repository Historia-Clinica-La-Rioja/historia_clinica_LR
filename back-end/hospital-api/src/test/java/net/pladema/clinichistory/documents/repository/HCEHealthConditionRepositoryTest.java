package net.pladema.clinichistory.documents.repository;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.entity.Document;
import net.pladema.clinichistory.documents.repository.hce.HCEHealthConditionRepository;
import net.pladema.clinichistory.documents.repository.hce.HCEHealthConditionRepositoryImpl;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.documents.repository.ips.entity.HealthCondition;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionClinicalStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.mocks.DocumentsTestMocks;
import net.pladema.clinichistory.mocks.HealthConditionTestMocks;
import net.pladema.clinichistory.mocks.SnomedTestMocks;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = false)
public class HCEHealthConditionRepositoryTest extends UnitRepository {

	private final Short AMBULATORIA = 4;

	private HCEHealthConditionRepository hCEHealthConditionRepository;

	@Autowired
	private EntityManager entityManager;

	@Before
	public void setUp() {
		hCEHealthConditionRepository = new HCEHealthConditionRepositoryImpl(entityManager);
	}

	@Test
	public void test_hce_health_condition_success() {
		Integer patientId = 1;

		createFirstDocument(patientId);
		createSecondDocument(patientId);
		createThirdDocument(patientId);

		List<HCEHealthConditionVo> resultQuery = hCEHealthConditionRepository.getPersonalHistories(patientId);

		assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(3);

		// El diagnostico principal queda inactivo por lo que se descarta
		assertThat(resultQuery.stream().anyMatch(HCEHealthConditionVo::isMain))
				.isFalse();

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isFamilyHistory))
				.hasSize(0);

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isPersonalHistory))
				.hasSize(1);

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isChronic))
				.hasSize(2);

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isSecondaryDiagnosis))
				.hasSize(0);

	}

	@Test
	public void test_hce_family_history_success() {
		Integer patientId = 1;

		createFirstDocument(patientId);
		createSecondDocument(patientId);
		createThirdDocument(patientId);

		List<HCEHealthConditionVo> resultQuery = hCEHealthConditionRepository.getFamilyHistories(patientId);

		assertThat(resultQuery)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2);

		// El diagnostico principal queda inactivo por lo que se descarta
		assertThat(resultQuery.stream().anyMatch(HCEHealthConditionVo::isMain))
				.isFalse();

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isFamilyHistory))
				.hasSize(2);

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isPersonalHistory))
				.hasSize(0);

		assertThat(resultQuery.stream().filter(HCEHealthConditionVo::isSecondaryDiagnosis))
				.hasSize(0);

	}

	private void createFirstDocument(Integer patientId){
		Document document = DocumentsTestMocks.createDocument(1, AMBULATORIA, SourceType.OUTPATIENT, DocumentStatus.FINAL);
		document = save(document);

		String familyCode = "familyCode";
		save(SnomedTestMocks.createSnomed(familyCode));
		HealthCondition familyhistory1 = HealthConditionTestMocks.createFamilyHistory(patientId,familyCode, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(familyhistory1).getId()));

		String personalCode = "personal1";
		save(SnomedTestMocks.createSnomed(personalCode));
		HealthCondition personalHistory1 = HealthConditionTestMocks.createPersonalHistory(patientId, personalCode, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory1).getId()));

		String personalCode1 = "personal2";
		save(SnomedTestMocks.createSnomed(personalCode1));
		HealthCondition personalHistory2 = HealthConditionTestMocks.createPersonalHistory(patientId,personalCode1, ConditionClinicalStatus.INACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory2).getId()));

		String diagnose1Code = "diagnose1";
		save(SnomedTestMocks.createSnomed(diagnose1Code));
		HealthCondition diagnose1 = HealthConditionTestMocks.createDiagnose(patientId,diagnose1Code, ConditionClinicalStatus.INACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose1).getId()));

		String mainDiagnosesCode = "mainDiagnose1";
		HealthCondition mainDiagnoses = HealthConditionTestMocks.createMainDiagnose(patientId,mainDiagnosesCode, ConditionClinicalStatus.ACTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(mainDiagnoses).getId()));
	}

	private void createSecondDocument(Integer patientId){
		Document document = DocumentsTestMocks.createDocument(1, AMBULATORIA, SourceType.OUTPATIENT, DocumentStatus.FINAL);
		document = save(document);

		String diagnose2Code = "diagnose2";
		save(SnomedTestMocks.createSnomed(diagnose2Code));
		HealthCondition diagnose2 = HealthConditionTestMocks.createDiagnose(patientId,diagnose2Code, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose2).getId()));

		String diagnose3Code = "diagnose3";
		save(SnomedTestMocks.createSnomed(diagnose3Code));
		HealthCondition diagnose3 = HealthConditionTestMocks.createDiagnose(patientId,diagnose3Code, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose3).getId()));

		HealthCondition diagnose4 = HealthConditionTestMocks.createDiagnose(patientId,diagnose3Code, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose4).getId()));

	}


	private void createThirdDocument(Integer patientId){
		Document document = DocumentsTestMocks.createDocument(1, AMBULATORIA, SourceType.OUTPATIENT, DocumentStatus.FINAL);
		document = save(document);

		String familyCode2 = "familyCode2";
		save(SnomedTestMocks.createSnomed(familyCode2));
		HealthCondition familyhistory2 = HealthConditionTestMocks.createFamilyHistory(patientId,familyCode2, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(familyhistory2).getId()));

		String personal3Code = "personal3";
		save(SnomedTestMocks.createSnomed(personal3Code));
		HealthCondition personalHistory3 = HealthConditionTestMocks.createPersonalHistory(patientId,personal3Code, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory3).getId()));

		String diagnose4Code = "diagnose4";
		save(SnomedTestMocks.createSnomed(diagnose4Code));
		HealthCondition diagnose5 = HealthConditionTestMocks.createDiagnose(patientId,diagnose4Code, ConditionClinicalStatus.ACTIVE, ConditionVerificationStatus.ERROR);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(diagnose5).getId()));

		String mainDiagnose1 = "mainDiagnose1";
		save(SnomedTestMocks.createSnomed(mainDiagnose1));
		HealthCondition mainDiagnoses1 = HealthConditionTestMocks.createMainDiagnose(patientId,mainDiagnose1, ConditionClinicalStatus.INACTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(mainDiagnoses1).getId()));

		String personalCode = "personal1";
		HealthCondition personalHistory1 = HealthConditionTestMocks.createChronicPersonalHistory(patientId, personalCode, ConditionVerificationStatus.PRESUMPTIVE);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory1).getId()));

		String personalCode1 = "personal2";
		HealthCondition personalHistory2 = HealthConditionTestMocks.createChronicPersonalHistory(patientId,personalCode1, ConditionVerificationStatus.CONFIRMED);
		save(HealthConditionTestMocks.createHealthConditionDocument(document.getId(), save(personalHistory2).getId()));
	}

}
