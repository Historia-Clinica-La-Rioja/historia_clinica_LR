package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.repository.entity.PatientMedicalCoverageAssn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientMedicalCoverageRepository extends JpaRepository<PatientMedicalCoverageAssn, Integer>, PatientMedicalCoverageRepositoryCustom{

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW net.pladema.patient.repository.domain.PatientMedicalCoverageVo(pmc.id as pmcid, pmc.affiliateNumber, " +
			"pmc.vigencyDate, pmc.active, mc.id, mc.name, mc.cuit, mc.type, hi.rnos, hi.acronym, pmc.conditionId, pmc.startDate, pmc.endDate, pmc.planId)"+
			"FROM PatientMedicalCoverageAssn pmc " +
			"JOIN MedicalCoverage mc ON (pmc.medicalCoverageId = mc.id) " +
			"LEFT JOIN HealthInsurance hi ON (mc.id = hi.id) " +
			"LEFT JOIN PrivateHealthInsurance phi ON (mc.id = phi.id) "+
			"WHERE pmc.id = :patientMedicalCoverageId ")
	Optional<PatientMedicalCoverageVo> getPatientCoverage(@Param("patientMedicalCoverageId") Integer patientMedicalCoverageId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT pmc FROM PatientMedicalCoverageAssn pmc WHERE pmc.patientId=:patientId AND pmc.medicalCoverageId =:medicalCoverageId")
	Optional<PatientMedicalCoverageAssn> getByPatientAndMedicalCoverage(@Param("patientId") Integer patientId, @Param("medicalCoverageId") Integer medicalCoverageId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT pmc FROM PatientMedicalCoverageAssn pmc WHERE pmc.medicalCoverageId =:medicalCoverageId")
	List<PatientMedicalCoverageAssn> getByMedicalCoverageId(@Param("medicalCoverageId") Integer medicalCoverageId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT pmc FROM PatientMedicalCoverageAssn pmc WHERE pmc.planId =:planId")
	List<PatientMedicalCoverageAssn> findByPlanId(@Param("planId") Integer planId);

}
