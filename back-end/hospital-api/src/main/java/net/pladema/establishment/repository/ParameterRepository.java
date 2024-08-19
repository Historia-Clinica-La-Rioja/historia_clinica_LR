package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.Parameter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Integer> {
	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(p.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM Parameter p " +
			"WHERE p.loincId = :loincId")
	boolean existsParameterByLoincId(@Param("loincId") Integer loincId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT (CASE WHEN count(p.id) > 0 THEN TRUE ELSE FALSE END) " +
			"FROM Parameter p " +
			"WHERE p.description = :description")
	boolean existsParameterByDescription(@Param("description") String description);

	@Transactional(readOnly = true)
	@Query(value = "SELECT p.id " +
					"FROM Parameter p " +
					"WHERE p.loincId = :loincId")
	List<Integer> getParametersIdsByLoincId(@Param("loincId") Integer loincId);

	@Transactional(readOnly = true)
	@Query(value = "SELECT p.id " +
			"FROM Parameter p " +
			"WHERE p.description = :description ")
	List<Integer> getParametersIdsByDescription(@Param("description") String description);
}
