package ar.lamansys.sgh.shared.infrastructure.output.repository;

import ar.lamansys.sgh.shared.infrastructure.output.entities.SharedHealthcareProfessional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface SharedHealthcareProfessionalRepository extends SGXAuditableEntityJPARepository<SharedHealthcareProfessional, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT(hp.id) "
			+ " FROM HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id) "
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id"
			+ " WHERE up.pk.userId = :userId "
			+ " AND hp.deleteable.deleted = false")
	Integer getProfessionalId(@Param("userId") Integer userId);
}
