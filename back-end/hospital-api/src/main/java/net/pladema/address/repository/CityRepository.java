package net.pladema.address.repository;

import net.pladema.address.repository.entity.City;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

	@Transactional(readOnly = true)
	<T> Collection<T> findAllProjectedBy(Sort by, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT c FROM City as c " +
			"JOIN Department as d ON (d.id = c.departmentId) " +
			"WHERE d.provinceId = :provinceId")
	<T> Collection<T> findByProvince(@Param("provinceId") Short provinceId, Sort by, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT c FROM City as c " +
			"WHERE c.departmentId = :departmentId AND c.active = true AND (c.cityType <> net.pladema.address.repository.entity.City.BARRIO or c.cityType is null) ")
	<T> Collection<T> findByDepartment(@Param("departmentId") Short departmentId, Sort by, Class<T> clazz);

	@Transactional(readOnly = true)
	@Query("SELECT c.departmentId FROM City as c WHERE c.id = :cityId")
	Short findDepartmentByCity(@Param("cityId") Integer cityId);

	@Transactional (readOnly = true)
	@Query(value = "SELECT exists (SELECT 1 FROM City as c WHERE c.department_id = :departmentId and c.id = :cityId)", nativeQuery = true)
	boolean existCityInDepartment(@Param("departmentId") Short departmentId, @Param("cityId") Integer cityId);
}
