package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyDiagnostic;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OdontologyDiagnosticRepository extends SGXAuditableEntityJPARepository<OdontologyDiagnostic, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE OdontologyDiagnostic od " +
			"SET od.patientId = :newPatientId " +
			"WHERE od.id IN :odIds")
	void updatePatient(@Param("odIds") List<Integer> odIds, @Param("newPatientId") Integer newPatientId);
}
