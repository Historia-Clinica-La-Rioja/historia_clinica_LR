package ar.lamansys.sgh.shared.infrastructure.output.repository;

import ar.lamansys.sgh.shared.domain.medicalcoverage.SharedPatientMedicalCoverageBo;

import ar.lamansys.sgh.shared.infrastructure.output.entities.SharedPatientMedicalCoverageAssn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SharedPatientMedicalCoverageRepository extends JpaRepository<SharedPatientMedicalCoverageAssn, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT NEW ar.lamansys.sgh.shared.domain.medicalcoverage.SharedPatientMedicalCoverageBo(pmc.affiliateNumber, mcp.plan, " +
			"mc.id, mc.name, mc.cuit, mc.type, hi.rnos, hi.acronym) "+
			"FROM SharedPatientMedicalCoverageAssn pmc " +
			"JOIN SharedMedicalCoverage mc ON (pmc.medicalCoverageId = mc.id) " +
			"LEFT JOIN SharedHealthInsurance hi ON (mc.id = hi.id) " +
			"LEFT JOIN SharedPrivateHealthInsurance phi ON (mc.id = phi.id) " +
			"LEFT JOIN SharedMedicalCoveragePlan mcp ON (mcp.id = pmc.planId) " +
			"WHERE pmc.id = :patientMedicalCoverageId ")
	Optional<SharedPatientMedicalCoverageBo> getPatientMedicalCoverageById(@Param("patientMedicalCoverageId") Integer medicalCoverageId);

}
