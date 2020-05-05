package net.pladema.staff.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.pladema.staff.repository.entity.HealthcareProfessional;
import net.pladema.staff.service.domain.HealthcarePerson;

@Repository
public interface HealthcareProfessionalRepository extends JpaRepository<HealthcareProfessional, Integer> {

	@Query(value = " SELECT new net.pladema.staff.service.domain.HealthcarePerson(hp.id, hp.licenseNumber, p)"
			+ " FROM  HealthcareProfessional hp "
			+ " INNER JOIN Person p ON hp.personId = p.id"
			+ " INNER JOIN User u ON u.personId = p.id"
			+ " INNER JOIN UserRole ur ON u.id = ur.userRolePK.userId"
			+ " WHERE ur.userRolePK.roleId = 3 " // Role 'Especialista Medico'
			+ " AND ur.userRolePK.institutionId = :institutionId ")
	List<HealthcarePerson> getAllDoctors(@Param("institutionId") Integer institutionId);
	
}
