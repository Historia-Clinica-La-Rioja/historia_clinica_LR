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
}
