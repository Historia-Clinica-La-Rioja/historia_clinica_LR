package net.pladema.staff.repository;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthcareProfessionalRepository extends SGXAuditableEntityJPARepository<HealthcareProfessional, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT new net.pladema.staff.service.domain.HealthcarePersonBo(hp.id, hp.licenseNumber,p.id, p)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id"
			+ " INNER JOIN UserRole ur ON up.pk.userId = ur.userRolePK.userId"
			+ " WHERE ur.userRolePK.roleId = 3 " // Role 'Especialista Medico'
			+ " AND ur.userRolePK.institutionId = :institutionId "
			+ " AND hp.deleteable.deleted = false")
	List<HealthcarePersonBo> getAllDoctors(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT(hp.id) "
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id) "
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id"
			+ " WHERE up.pk.userId = :userId "
			+ " AND hp.deleteable.deleted = false")
    Integer getProfessionalId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id"
			+ " INNER JOIN UserRole ur ON up.pk.userId = ur.userRolePK.userId"
			+ " WHERE ur.userRolePK.institutionId = :institutionId "
			+ " AND hp.deleteable.deleted = false "
			+ " ORDER BY p.lastName, p.firstName")
    List<HealthcareProfessionalVo> findAllByInstitution(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id)"
			+ " WHERE hp.id = :id"
			+ " AND hp.deleteable.deleted = false")
	Optional<HealthcareProfessionalVo> findProfessionalById(@Param("id") Integer id);


	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT hp.id FROM HealthcareProfessional hp WHERE hp.personId = :personId AND hp.deleteable.deleted = false")
	Optional<Integer> findProfessionalByPersonId(@Param("personId") Integer personId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " AND hp.deleteable.deleted = false "
			+ " ORDER BY p.lastName, p.firstName")
	List<HealthcareProfessionalVo> getAllProfessional();
}
