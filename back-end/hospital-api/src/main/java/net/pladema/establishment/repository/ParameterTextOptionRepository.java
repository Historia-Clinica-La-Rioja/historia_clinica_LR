package net.pladema.establishment.repository;

import net.pladema.establishment.repository.entity.ParameterTextOption;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ParameterTextOptionRepository extends JpaRepository<ParameterTextOption, Integer> {

	@Transactional(readOnly = true)
	@Query(value = "SELECT pto.description " +
			"FROM ParameterTextOption pto " +
			"WHERE pto.parameterId = :parameterId and pto.deleteable.deleted = false")
	List<String> getDescriptionsFromParameterId(@Param("parameterId") Integer parameterId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ParameterTextOption pto " +
					"WHERE pto.parameterId = :parameterId")
	void deleteTextOptionFromParameterId(@Param("parameterId") Integer parameterId);

}