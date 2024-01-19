package net.pladema.address.repository;

import net.pladema.address.repository.entity.Department;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Short> {

	@Transactional(readOnly = true)
	<T> Collection<T> findAllProjectedBy(Sort by, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT d FROM Department as d WHERE d.provinceId = :provinceId")
	<T> Collection<T> findByProvince(@Param("provinceId") Short provinceId, Sort by, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT d.provinceId FROM Department as d WHERE d.id = :departmentId")
	Short findProvinceByDepartment(@Param("departmentId") Short departmentId);

	@Transactional (readOnly = true)
	@Query(value = "SELECT exists (SELECT 1 FROM Department as d WHERE d.province_id = :provinceId and d.id = :departmentId)", nativeQuery = true)
	boolean existDepartmentInProvince(@Param("provinceId") Short provinceId, @Param("departmentId") Short departmentId);

	@Transactional(readOnly = true)
	@Query("SELECT d FROM Department as d WHERE d.id = :departmentId")
	Department findDepartmentById(@Param("departmentId") Short departmentId);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId)" +
			"JOIN Address a ON (c.id = a.cityId) " +
			"JOIN Institution i ON (i.addressId = a.id) " +
			"JOIN CareLineInstitution cli ON (i.id = cli.institutionId) " +
			"JOIN CareLineInstitutionSpecialty clis ON (cli.id = clis.careLineInstitutionId) " +
			"WHERE clis.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND cli.careLineId = :careLineId " +
			"AND cli.deleted IS FALSE ")
	<T> Collection<T> findAllByCareLineIdAndClinicalSpecialtyId(@Param("careLineId") Integer careLineId,
																@Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId) " +
			"JOIN Address a ON (c.id = a.cityId) " +
			"JOIN Institution i ON (i.addressId = a.id) " +
			"JOIN UserRole ur ON (i.id = ur.institutionId )" +
			"JOIN UserPerson up ON (ur.userId = up.pk.userId) " +
			"JOIN HealthcareProfessional hp ON (up.pk.personId = hp.personId) " +
			"JOIN ProfessionalProfessions pp ON (hp.id = pp.healthcareProfessionalId) " +
			"JOIN HealthcareProfessionalSpecialty hps ON (pp.id = hps.professionalProfessionId) " +
			"WHERE hps.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND hp.deleteable.deleted IS FALSE " +
			"AND pp.deleteable.deleted IS FALSE " +
			"AND hps.deleteable.deleted IS FALSE ")
	<T> Collection<T> findAllByProfessionalsWithClinicalSpecialtyId(@Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId)" +
			"JOIN Address a ON (c.id = a.cityId) " +
			"JOIN Institution i ON (a.id = i.addressId) " +
			"JOIN CareLineInstitution cli ON (i.id = cli.institutionId) " +
			"JOIN CareLineInstitutionPractice clip ON (cli.id = clip.careLineInstitutionId) " +
			"JOIN SnomedRelatedGroup srg ON (clip.snomedRelatedGroupId = srg.id)  " +
			"WHERE cli.careLineId = :careLineId " +
			"AND srg.snomedId = :practiceSnomedId " +
			"AND cli.deleted = FALSE")
	<T> Collection<T> findAllByCareLineIdAndPracticeSnomedId(@Param("careLineId") Integer careLineId,
															 @Param("practiceSnomedId") Integer practiceSnomedId, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId)" +
			"JOIN Address a ON (c.id = a.cityId) " +
			"JOIN Institution i ON (a.id = i.addressId) " +
			"JOIN CareLineInstitution cli ON (i.id = cli.institutionId) " +
			"JOIN CareLineInstitutionSpecialty clis ON (cli.id = clis.careLineInstitutionId) " +
			"JOIN CareLineInstitutionPractice clip ON (cli.id = clip.careLineInstitutionId) " +
			"JOIN SnomedRelatedGroup srg ON (clip.snomedRelatedGroupId = srg.id)  " +
			"WHERE cli.careLineId = :careLineId " +
			"AND srg.snomedId = :practiceSnomedId " +
			"AND clis.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND cli.deleted = FALSE")
	<T> Collection<T> findAllByCareLineIdClinicalSpecialtyIdAndPracticeSnomedId(@Param("careLineId") Integer careLineId,
																				@Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																				@Param("practiceSnomedId") Integer practiceSnomedId , Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId)" +
			"JOIN Address a ON (c.id = a.cityId) " +
			"JOIN Institution i ON (a.id = i.addressId) " +
			"JOIN UserRole ur ON (i.id = ur.institutionId )" +
			"JOIN UserPerson up ON (ur.userId = up.pk.userId) " +
			"JOIN HealthcareProfessional hp ON (up.pk.personId = hp.personId) " +
			"JOIN ProfessionalProfessions pp ON (hp.id = pp.healthcareProfessionalId) " +
			"JOIN HealthcareProfessionalSpecialty hps ON (pp.id = hps.professionalProfessionId) " +
			"JOIN SnomedGroup sg ON (i.id = sg.institutionId) " +
			"JOIN SnomedGroup baseGroup ON (sg.groupId = baseGroup.id) " +
			"JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			"WHERE srg.snomedId = :practiceSnomedId " +
			"AND hps.clinicalSpecialtyId IN :clinicalSpecialtyIds " +
			"AND baseGroup.description = :sgDescription " +
			"AND sg.groupType = :sgTypeId " +
			"AND ur.deleteable.deleted IS FALSE " +
			"AND hp.deleteable.deleted IS FALSE " +
			"AND pp.deleteable.deleted IS FALSE " +
			"AND hps.deleteable.deleted IS FALSE")
	<T> Collection<T> findAllByClinicalSpecialtyIdAndPracticeSnomedId(@Param("clinicalSpecialtyIds") List<Integer> clinicalSpecialtyIds,
																	  @Param("practiceSnomedId") Integer practiceSnomedId ,
																	  @Param("sgDescription") String sgDescription,
																	  @Param("sgTypeId") Short sgTypeId, Class<T> clazz);
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId)" +
			"JOIN Address a ON (c.id = a.cityId) " +
			"JOIN Institution i ON (a.id = i.addressId) " +
			"JOIN SnomedGroup sg ON (i.id = sg.institutionId) " +
			"JOIN SnomedGroup baseGroup ON (sg.groupId = baseGroup.id) " +
			"JOIN SnomedRelatedGroup srg ON (sg.id = srg.groupId) " +
			"WHERE srg.snomedId = :practiceSnomedId " +
			"AND baseGroup.description = :sgDescription " +
			"AND sg.groupType = :sgTypeId ")
	<T> Collection<T> findAllByPractice(@Param("practiceSnomedId") Integer practiceSnomedId ,
										@Param("sgDescription") String sgDescription,
										@Param("sgTypeId") Short sgTypeId, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d " +
			"FROM Department d " +
			"JOIN City c ON (d.id = c.departmentId) " +
			"JOIN Address a ON (c.id = a.cityId) "+
			"JOIN Institution i ON (a.id = i.addressId) ")
	<T> Collection<T> findAllByInstitutions();
}
