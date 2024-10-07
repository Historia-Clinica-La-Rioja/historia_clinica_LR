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
			+ " INNER JOIN UserRole ur ON up.pk.userId = ur.userId"
			+ " WHERE ur.roleId IN :roles "
			+ " AND ur.institutionId = :institutionId "
			+ " AND hp.deleteable.deleted = false "
			+ " GROUP BY hp.id, hp.licenseNumber,p.id, p, pe.nameSelfDetermination")
	List<HealthcarePersonBo> getAllDoctors(@Param("institutionId") Integer institutionId, @Param("roles") List<Short> roles);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT(hp.id) "
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN UserPerson up ON up.pk.personId = hp.personId"
			+ " WHERE up.pk.userId = :userId "
			+ " AND hp.deleteable.deleted = false")
    Integer getProfessionalId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT up.pk.userId "
			+ " FROM HealthcareProfessional hp " 
			+ " INNER JOIN UserPerson up ON up.pk.personId = hp.personId " 
			+ "WHERE hp.id = :HealthcareProfessionalProfessionalId "
			+ "AND hp.deleteable.deleted = false")
    Integer getUserIdByHealthcareProfessionalId(@Param("HealthcareProfessionalProfessionalId") Integer HealthcareProfessionalProfessionalId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination, p.middleNames, p.otherLastNames)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id "
			+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id) "
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id "
			+ " INNER JOIN UserRole ur ON up.pk.userId = ur.userId "
			+ " WHERE ur.institutionId = :institutionId "
			+ "	AND ur.roleId IN (:professionalERolIds) "
			+ " AND hp.deleteable.deleted = false "
			+ " AND ur.deleteable.deleted = false "
			+ " ORDER BY p.lastName, p.firstName ")
    List<HealthcareProfessionalVo> findAllByInstitution(@Param("institutionId") Integer institutionId,
														@Param("professionalERolIds") List<Short> professionalERoleIds);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination, p.middleNames, p.otherLastNames)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id "
			+ " LEFT JOIN PersonExtended pe ON (p.id = pe.id) "
			+ " INNER JOIN UserPerson up ON up.pk.personId = p.id "
			+ " INNER JOIN UserRole ur ON up.pk.userId = ur.userId "
			+ "	AND ur.roleId IN (:professionalERoleIds) "
			+ " AND hp.deleteable.deleted IS false "
			+ " AND ur.deleteable.deleted IS false "
			+ " ORDER BY p.lastName, p.firstName ")
	List<HealthcareProfessionalVo> findAllByRoleIds(@Param("professionalERoleIds") List<Short> professionalERoleIds);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber,p.id, pe.nameSelfDetermination, p.middleNames, p.otherLastNames)"
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

	@Transactional(readOnly = true)
	@Query(" SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo(hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber, p.id, pe.nameSelfDetermination) " +
			"FROM HealthcareProfessional hp " +
			"JOIN Person p ON (p.id = hp.personId) " +
			"JOIN PersonExtended pe ON (pe.id = p.id) " +
			"JOIN VirtualConsultation vc ON (vc.responsibleHealthcareProfessionalId = hp.id) " +
			"WHERE vc.institutionId = :institutionId")
	List<HealthcareProfessionalVo> getVirtualConsultationProfessionalsByInstitutionId(@Param("institutionId") Integer institutionId);


	@Transactional(readOnly = true)
	@Query(value = "SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo( " +
			"hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber, p.id, pe.nameSelfDetermination) " +
			"FROM HealthcareProfessional hp " +
			"JOIN Person p ON (hp.personId = p.id) " +
			"JOIN UserPerson up ON (p.id = up.pk.personId) " +
			"JOIN UserRole ur ON (up.pk.userId = ur.userId) " +
			"JOIN Institution i ON (ur.institutionId = i.id) " +
			"JOIN Address a ON (i.addressId = a.id) " +
			"JOIN City c ON (a.cityId = c.id) " +
			"LEFT JOIN PersonExtended pe ON (p.id = pe.id) " +
			"WHERE c.departmentId = :departmentId " +
			"AND ur.roleId IN (:professionalERoleIds) " +
			"AND hp.deleteable.deleted = false " +
			"AND ur.deleteable.deleted = false ")
	List<HealthcareProfessionalVo> getAllProfessionalsByDepartment(@Param("departmentId") Short departmentId,
																   @Param("professionalERoleIds") List<Short> professionalERoleIds);
}
