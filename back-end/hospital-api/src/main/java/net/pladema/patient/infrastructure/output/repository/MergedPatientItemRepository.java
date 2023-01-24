package net.pladema.patient.infrastructure.output.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.patient.infrastructure.output.repository.entity.MergedPatientItem;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MergedPatientItemRepository extends SGXAuditableEntityJPARepository<MergedPatientItem, Integer> {

	@Transactional(readOnly = true)
	@Query("SELECT mpi " +
			"FROM MergedPatientItem mpi " +
			"WHERE mpi.oldPatientId = :inactivePatientId " +
			"AND mpi.deleteable.deleted = false ")
	List<MergedPatientItem> findAllByInactivePatientId(@Param("inactivePatientId") Integer inactivePatientId);

}
