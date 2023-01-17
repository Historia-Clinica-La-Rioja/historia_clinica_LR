package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.OdontologyProcedure;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OdontologyProcedureRepository extends SGXAuditableEntityJPARepository<OdontologyProcedure, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE OdontologyProcedure op " +
			"SET op.patientId = :newPatientId " +
			"WHERE op.id IN :opIds")
	void updatePatient(@Param("opIds")List<Integer> opIds, @Param("newPatientId") Integer newPatientId);
}
