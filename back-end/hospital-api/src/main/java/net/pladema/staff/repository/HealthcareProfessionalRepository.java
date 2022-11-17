package net.pladema.staff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.shared.auditable.repository.SGXAuditableEntityJPARepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.service.domain.HealthcarePersonBo;

@Repository
public interface HealthcareProfessionalRepository extends SGXAuditableEntityJPARepository<HealthcareProfessional, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT new net.pladema.staff.service.domain.HealthcarePersonBo(hp.id, hp.licenseNumber,p.id, p, pe.nameSelfDetermination)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " LEFT JOIN PersonExtended pe ON p.id = pe.id"
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
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id)"
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id"
			+ " INNER JOIN UserRole ur ON up.pk.userId = ur.userRolePK.userId"
			+ " WHERE ur.userRolePK.institutionId = :institutionId "
			+ "	AND ur.userRolePK.roleId IN (:professionalERolIds) "
			+ " AND hp.deleteable.deleted = false "
			+ " ORDER BY p.lastName, p.firstName")
    List<HealthcareProfessionalVo> findAllByInstitution(@Param("institutionId") Integer institutionId,
														@Param("professionalERolIds") List<Short> professionalERoleIds);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id)"
			+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id)"
			+ " WHERE hp.id = :id"
			+ " AND hp.deleteable.deleted = false")
	Optional<HealthcareProfessionalVo> findActiveProfessionalById(@Param("id") Integer id);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id)"
			+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id)"
			+ " WHERE hp.id = :id")
	Optional<HealthcareProfessionalVo> findFromAllProfessionalsById(@Param("id") Integer id);


	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT hp.id FROM HealthcareProfessional hp WHERE hp.personId = :personId AND hp.deleteable.deleted = false")
	Optional<Integer> findProfessionalByPersonId(@Param("personId") Integer personId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id)"
			+ " AND hp.deleteable.deleted = false "
			+ " ORDER BY p.lastName, p.firstName")
	List<HealthcareProfessionalVo> getAllProfessional();
	@Transactional(readOnly = true)
	@Query(value = " SELECT count(DISTINCT hp.id) " +
			"FROM HealthcareProfessional hp " +
			"WHERE hp.personId = :personId " +
			"AND hp.deleteable.deleted = false")
	int countActiveByPersonId(@Param("personId") Integer personId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT hp " +
			"FROM HealthcareProfessional hp " +
			"WHERE hp.personId = :personId " +
			"AND hp.deleteable.deleted = false")
	Optional<HealthcareProfessional> findByPersonId(@Param("personId") Integer personId);
}
