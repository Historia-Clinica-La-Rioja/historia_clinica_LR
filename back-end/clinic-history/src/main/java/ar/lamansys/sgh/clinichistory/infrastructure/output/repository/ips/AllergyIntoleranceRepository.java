package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.AllergyIntolerance;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AllergyIntoleranceRepository extends SGXAuditableEntityJPARepository<AllergyIntolerance, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE AllergyIntolerance AS ai " +
			"SET ai.patientId = :newPatientId " +
			"WHERE ai.id IN (:aiIds)")
	void updatePatient(@Param("aiIds") List<Integer> aiIds, @Param("newPatientId") Integer newPatientId);

}
