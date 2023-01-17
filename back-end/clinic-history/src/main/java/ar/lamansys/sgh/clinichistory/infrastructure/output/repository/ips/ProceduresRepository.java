package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Procedure;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProceduresRepository extends SGXAuditableEntityJPARepository<Procedure, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE Procedure AS p " +
			"SET p.patientId = :newPatientId " +
			"WHERE p.id IN :pIds")
	void updatePatient(@Param("pIds")List<Integer> pIds, @Param("newPatientId") Integer newPatientId);

}
