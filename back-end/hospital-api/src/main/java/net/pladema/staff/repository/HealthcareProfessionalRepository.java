package net.pladema.staff.repository;

import net.pladema.staff.repository.domain.HealthcareProfessionalVo;
import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.service.domain.HealthcarePersonBo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthcareProfessionalRepository extends JpaRepository<HealthcareProfessional, Integer> {

	@Transactional(readOnly = true)
	@Query(value = " SELECT new net.pladema.staff.service.domain.HealthcarePersonBo(hp.id, hp.licenseNumber, p)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " INNER JOIN User u ON u.personId = p.id"
			+ " INNER JOIN UserRole ur ON u.id = ur.userRolePK.userId"
			+ " WHERE ur.userRolePK.roleId = 3 " // Role 'Especialista Medico'
			+ " AND ur.userRolePK.institutionId = :institutionId ")
	List<HealthcarePersonBo> getAllDoctors(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT hp.id "
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id) "
			+ " INNER JOIN User u ON (u.personId = p.id) "
			+ " WHERE u.id = :userId ")
    Integer getProfessionalId(@Param("userId") Integer userId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " INNER JOIN User u ON u.personId = p.id"
			+ " INNER JOIN UserRole ur ON u.id = ur.userRolePK.userId"
			+ " WHERE ur.userRolePK.institutionId = :institutionId "
			+ " ORDER BY p.lastName, p.firstName")
    List<HealthcareProfessionalVo> findAllByInstitution(@Param("institutionId") Integer institutionId);

	@Transactional(readOnly = true)
	@Query(value = " SELECT DISTINCT new net.pladema.staff.repository.domain.HealthcareProfessionalVo("
			+ " hp.id, hp.licenseNumber, p.firstName, p.lastName, p.identificationNumber)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON (hp.personId = p.id)"
			+ " WHERE hp.id = :id")
	Optional<HealthcareProfessionalVo> findProfessionalById(@Param("id") Integer id);
}
