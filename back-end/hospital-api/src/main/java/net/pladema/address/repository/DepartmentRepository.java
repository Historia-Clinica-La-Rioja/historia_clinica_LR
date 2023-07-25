package net.pladema.address.repository;

import net.pladema.address.repository.entity.Department;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

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
	@Query("SELECT DISTINCT d From Department d " +
			"JOIN Province p ON (p.id = d.provinceId) " +
			"JOIN Address a ON (a.provinceId = p.id) " +
			"JOIN Institution i ON (i.addressId = a.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"JOIN DiaryCareLine dcl ON (dcl.pk.diaryId = d.id) " +
			"WHERE p.id = :provinceId " +
			"AND di.active = true AND di.clinicalSpecialtyId = :clinicalSpecialtyId AND di.deleteable.deleted = FALSE " +
			"AND dcl.pk.careLineId = :careLineId ")
	<T> Collection<T> findDepartmentsByProvinceIdHavingActiveDiaryWithCareLineClinicalSpecialty(@Param("provinceId") Short provinceId, @Param("careLineId") Integer careLineId, @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT d FROM Department d " +
			"JOIN Province p ON (p.id = d.provinceId) " +
			"JOIN Address a ON (a.provinceId = p.id) " +
			"JOIN Institution i ON (i.addressId = a.id) " +
			"JOIN DoctorsOffice do ON (do.institutionId = i.id) " +
			"JOIN Diary di ON (di.doctorsOfficeId = do.id) " +
			"WHERE p.id = :provinceId " +
			"AND di.active = TRUE AND di.clinicalSpecialtyId = :clinicalSpecialtyId AND di.deleteable.deleted = FALSE")
	<T> Collection<T> findDepartmentsByProvinceIdHavingActiveDiaryWithClinicalSpecialty(@Param("provinceId") Short provinceId, @Param("clinicalSpecialtyId") Integer clinicalSpecialtyId, Class<T> clazz);

}
