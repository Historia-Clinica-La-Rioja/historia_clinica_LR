package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Inmunization;
import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImmunizationRepository extends SGXAuditableEntityJPARepository<Inmunization, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE Inmunization AS i " +
			"SET i.patientId = :newPatientId " +
			"WHERE i.id IN :iIds")
	void updatePatient(@Param("iIds") List<Integer> iIds, @Param("newPatientId") Integer newPatientId);

}
