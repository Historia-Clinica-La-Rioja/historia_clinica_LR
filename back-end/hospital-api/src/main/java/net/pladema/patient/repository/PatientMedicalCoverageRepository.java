package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PatientMedicalCoverageVo;
import net.pladema.patient.repository.entity.PatientMedicalCoverageAssn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PatientMedicalCoverageRepository extends JpaRepository<PatientMedicalCoverageAssn, Integer>, PatientMedicalCoverageRepositoryCustom{

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW net.pladema.patient.repository.domain.PatientMedicalCoverageVo(pmc.id as pmcid, pmc.affiliateNumber, " +
			"pmc.vigencyDate, pmc.active, mc.id, mc.name, hi.rnos, hi.acronym, phi.plan, phid as phid)"+
			"FROM PatientMedicalCoverageAssn pmc " +
			"JOIN MedicalCoverage mc ON (pmc.medicalCoverageId = mc.id) " +
			"LEFT JOIN HealthInsurance hi ON (mc.id = hi.id) " +
			"LEFT JOIN PrivateHealthInsurance phi ON (mc.id = phi.id) "+
			"LEFT JOIN PrivateHealthInsuranceDetails phid ON (pmc.privateHealthInsuranceDetailsId = phid.id) "+
			"WHERE pmc.id = :patientMedicalCoverageId ")
	Optional<PatientMedicalCoverageVo> getPatientCoverage(@Param("patientMedicalCoverageId") Integer patientMedicalCoverageId);

}
